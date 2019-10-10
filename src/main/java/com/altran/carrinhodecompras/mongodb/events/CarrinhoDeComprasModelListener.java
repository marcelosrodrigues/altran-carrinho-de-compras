package com.altran.carrinhodecompras.mongodb.events;

import org.springframework.stereotype.Component;

import com.altran.carrinhodecompras.entities.CarrinhoDeCompra;

@Component
public class CarrinhoDeComprasModelListener extends AbstractSequenceListener<CarrinhoDeCompra> {}
