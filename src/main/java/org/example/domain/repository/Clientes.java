package org.example.domain.repository;

import org.example.domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface Clientes extends JpaRepository<Cliente, Integer> {

//Consulta abaixo foi feita em hql
    @Query(value = " select c from Cliente c where c.nome like :nome ")
   List<Cliente> encontrarPorNome( @Param("nome") String nome);
//Sql nativo
    @Query(value = " select * from cliente c where c.nome like '%:nome%' ",nativeQuery = true)
    List<Cliente> encontrarPorNomeSql( @Param("nome") String nome);

    @Transactional
    void deleteByNome(String nome);

    //Tambem aceita ordenação
   List<Cliente> findByNomeOrIdOrderById(String nome, Integer id);

    Cliente findOneByNome(String nome);
//consulta jpql
    boolean existsByNome(String nome);
    @Query(" select c from Cliente c left join fetch c.pedidos where c.id =:id ")
    Cliente findClienteFetchPedidos( @Param("id") Integer id);


}
