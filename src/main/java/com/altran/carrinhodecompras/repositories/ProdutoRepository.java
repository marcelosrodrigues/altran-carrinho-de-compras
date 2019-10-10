package com.altran.carrinhodecompras.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.altran.carrinhodecompras.entities.Produto;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto, Long> {

}
