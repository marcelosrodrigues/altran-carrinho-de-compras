package com.altran.carrinhodecompras.mongodb.events;

import org.springframework.stereotype.Component;

import com.altran.carrinhodecompras.entities.Usuario;

@Component
public class UsuarioModelListener extends AbstractSequenceListener<Usuario> {}
