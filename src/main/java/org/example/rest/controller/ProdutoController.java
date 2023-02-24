package org.example.rest.controller;


import org.example.domain.entity.Produto;
import org.example.domain.repository.Produtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.xml.ws.http.HTTPException;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private Produtos produtos;

    public ProdutoController(@Autowired Produtos produtos) {
        this.produtos = produtos;
    }

    @GetMapping("{id}")
    public Produto getProdutoById(@PathVariable Integer id){
         return produtos.findById(id)
                 .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não localizado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save(@RequestBody @Valid Produto produto){
        return produtos.save(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        produtos.findById(id).map(produto -> {
            produtos.delete(produto);
            return null;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não localizado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id,@RequestBody @Valid Produto produto ){
        produtos.findById(id).map(produtoExistente -> {
              produto.setId(produtoExistente.getId());
               produtos.save(produto);
                return produto;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não localizado"));
    }

    //Retorna uma lista de acordo com os parâmetros passados
    @GetMapping
    public List<Produto> find(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro,matcher);
        return produtos.findAll(example);
    }




}
