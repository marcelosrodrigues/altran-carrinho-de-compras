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

import com.altran.carrinhodecompras.entities.Usuario;
import com.altran.carrinhodecompras.repositories.UsuarioRepository;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;
		
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity all() {
		
		List<Usuario> usuarios = repository.findAll();
		if( usuarios.isEmpty() ) return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
		else {
			return ResponseEntity.ok(usuarios);
		}
		
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity findById(@PathVariable(name = "id", required = true) Long id ) {
		
		Optional<Usuario> usuario = repository.findById(id);
		if(usuario.isPresent()) return ResponseEntity.ok(usuario);
		else return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity insert(@RequestBody Usuario usuario) {
		
		Usuario existed = repository.findByEmail(usuario.getEmail());
		if( existed != null) return new ResponseEntity("Usuário duplicado",HttpStatus.CONFLICT);
		
		Usuario saved = repository.save(usuario);
		return ResponseEntity.ok(saved);		
		
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity update(@PathVariable(name = "id", required = true) Long id, @RequestBody Usuario usuario) {
		
		Optional<Usuario> founded = repository.findById(id);		
		
		if( !founded.isPresent() ) return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
		
		Usuario beUpdated = founded.get();
		beUpdated.setEmail(usuario.getEmail());
		beUpdated.setNome(usuario.getNome());
		repository.save(beUpdated);
			
		return ResponseEntity.ok(beUpdated);
		
	}
	
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable(name = "id", required = true) Long id) {
		Optional<Usuario> founded = repository.findById(id);		
		
		if( !founded.isPresent() ) return new ResponseEntity("Nenhum registro encontrado", HttpStatus.NOT_FOUND);
		
		Usuario beDeleted = founded.get();
		repository.delete(beDeleted);
		
		return ResponseEntity.ok(beDeleted);
	}
	
}
