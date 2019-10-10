package com.altran.carrinhodecompras.entities;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.altran.carrinhodecompras.mongodb.annotations.Sequence;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Document(collection = "produto")
@RequiredArgsConstructor
@NoArgsConstructor
@Sequence(sequence = "produto_sequence")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@JsonInclude(Include.NON_NULL)
public class Produto {

	@Id
	@EqualsAndHashCode.Include
	private Long id;
	
	@NonNull
	@lombok.NonNull
	private String nome;
	
	@NonNull
	@lombok.NonNull
	@EqualsAndHashCode.Include
	private BigDecimal valor;
}
