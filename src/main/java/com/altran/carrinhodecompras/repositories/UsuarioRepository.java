package com.altran.carrinhodecompras.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.altran.carrinhodecompras.entities.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, Long> {
	
	Usuario findByEmail(String email);

}
