package org.example.service.impl;


import org.example.domain.entity.Cliente;
import org.example.domain.entity.ItemPedido;
import org.example.domain.entity.Pedido;
import org.example.domain.entity.Produto;
import org.example.domain.enums.StatusPedido;
import org.example.domain.repository.Clientes;
import org.example.domain.repository.ItensPedido;
import org.example.domain.repository.Pedidos;
import org.example.domain.repository.Produtos;
import org.example.exception.PedidoNaoEncontradoExcepetion;
import org.example.exception.RegraNegocioException;
import org.example.rest.dto.ItemPedidoDTO;
import org.example.rest.dto.PedidoDTO;
import org.example.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private Pedidos repository;
    private Clientes clientesRespository;
    private Produtos produtosRepository;
    private ItensPedido itensPedidoRepository;

    public PedidoServiceImpl(@Autowired Pedidos repository,@Autowired Clientes clientes,Produtos produtos,ItensPedido itensPedido) {
        this.repository = repository;
        this.clientesRespository = clientes;
        this.produtosRepository = produtos;
        this.itensPedidoRepository = itensPedido;
    }

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
         Cliente cliente = clientesRespository.findById(idCliente)
                .orElseThrow(()-> new RegraNegocioException("Código de cliente inválido."));

            Pedido pedido = new Pedido();
            pedido.setTotal(dto.getTotal());
            pedido.setDataPedido(LocalDate.now());
            pedido.setCliente(cliente);
            pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedidos = converterItens(pedido, dto.getItens());
        repository.save(pedido);
        itensPedidoRepository.saveAll(itemPedidos);
        pedido.setItens(itemPedidos);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow(()-> new PedidoNaoEncontradoExcepetion() );

    }


    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens){
        if(itens.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens");
        }

            return itens
                    .stream()
                    .map(dto->{
                    Integer idProduto =dto.getProduto();
                 Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(()-> new RegraNegocioException("Código de produto inválido: "+idProduto));

                        ItemPedido itemPedido = new ItemPedido();
                        itemPedido.setQuantidade(dto.getQuantidade());
                        itemPedido.setPedido(pedido);
                        itemPedido.setProduto(produto);
                        return itemPedido;

                    }).collect(Collectors.toList());
    }



}
