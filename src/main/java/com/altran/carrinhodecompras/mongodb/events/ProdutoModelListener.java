package com.altran.carrinhodecompras.mongodb.events;

import org.springframework.stereotype.Component;

import com.altran.carrinhodecompras.entities.Produto;

@Component
public class ProdutoModelListener extends AbstractSequenceListener<Produto> {}
