package com.altran.carrinhodecompras.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.altran.carrinhodecompras.entities.CarrinhoDeCompra;
import com.altran.carrinhodecompras.entities.Produto;
import com.altran.carrinhodecompras.entities.Usuario;
import com.altran.carrinhodecompras.repositories.CarrinhoRepository;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoComprasController {
	
	@Autowired
	private CarrinhoRepository repository;
	
	@RequestMapping(path = "/abrir" , method = RequestMethod.POST)
	public ResponseEntity abrirCarrinho(@RequestBody Usuario usuario) {
		
		CarrinhoDeCompra carrinho = new CarrinhoDeCompra(usuario);
		CarrinhoDeCompra saved = repository.save(carrinho);
		return ResponseEntity.ok(saved);
		
	}
	
	@RequestMapping(path = "/{id}" , method = RequestMethod.GET)
	public ResponseEntity findById(@PathVariable("id") Long id) {
		Optional<CarrinhoDeCompra> carrinho = repository.findById(id);
		if(carrinho.isPresent()) return ResponseEntity.ok(carrinho);
		else return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path = "/{id}/adicionar" , method = RequestMethod.POST)
	public ResponseEntity adicionar(@PathVariable("id") Long id, @RequestBody Produto produto) {
		
		Optional<CarrinhoDeCompra> founded = repository.findById(id);
		
		CarrinhoDeCompra carrinho = founded.get();
		carrinho.adicionar(produto);
		carrinho = repository.save(carrinho);
		
		return ResponseEntity.ok(carrinho);
				
	}
	
	@RequestMapping(path = "/{id}/remover" , method = RequestMethod.POST)
	public ResponseEntity remover(@PathVariable("id") Long id, @RequestBody Produto produto) {
		
		Optional<CarrinhoDeCompra> founded = repository.findById(id);
		
		CarrinhoDeCompra carrinho = founded.get();
		carrinho.remover(produto);
		carrinho = repository.save(carrinho);
		
		return ResponseEntity.ok(carrinho);
				
	}

}
