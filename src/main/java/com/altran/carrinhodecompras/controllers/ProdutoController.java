package com.altran.carrinhodecompras.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.altran.carrinhodecompras.entities.Produto;
import com.altran.carrinhodecompras.entities.Usuario;
import com.altran.carrinhodecompras.repositories.ProdutoRepository;
import com.altran.carrinhodecompras.repositories.UsuarioRepository;
import com.mongodb.DuplicateKeyException;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoRepository repository;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity all() {
		
		List<Produto> produtos = repository.findAll();
		if( produtos.isEmpty() ) return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
		else {
			return ResponseEntity.ok(produtos);
		}
		
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity findById(@PathVariable(name = "id", required = true) Long id ) {
		
		Optional<Produto> produto = repository.findById(id);
		if(produto.isPresent()) return ResponseEntity.ok(produto);
		else return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity insert(@RequestBody Produto produto) {
		
		Produto saved = repository.save(produto);
		return ResponseEntity.ok(saved);
		
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity update(@PathVariable(name = "id", required = true) Long id, @RequestBody Produto produto) {
		
		Optional<Produto> founded = repository.findById(id);		
		
		if( !founded.isPresent() ) return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
		
		Produto beUpdated = founded.get();
		beUpdated.setValor(produto.getValor());
		beUpdated.setNome(produto.getNome());
		repository.save(beUpdated);
			
		return ResponseEntity.ok(beUpdated);
		
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable(name = "id", required = true) Long id) {
		Optional<Produto> founded = repository.findById(id);		
		
		if( !founded.isPresent() ) return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
		
		Produto beDeleted = founded.get();
		repository.delete(beDeleted);
		
		return ResponseEntity.ok(beDeleted);
	}
	
}
