package com.altran.carrinhodecompras.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.altran.carrinhodecompras.mongodb.annotations.Sequence;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.lang.NonNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Document(collection = "usuario")
@Getter
@Setter
@RequiredArgsConstructor()
@NoArgsConstructor
@Sequence(sequence = "usuario_sequence")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(Include.NON_NULL)
public class Usuario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1966917265092673368L;

	public static final String SEQUENCE_NAME = "usuario_sequence";

	@Id
	private Long id;
	
	@NonNull
	@lombok.NonNull
	private String nome;
	
	@NonNull
	@lombok.NonNull
	@Indexed(unique = true)
	@EqualsAndHashCode.Include
	private String email;

	
}
