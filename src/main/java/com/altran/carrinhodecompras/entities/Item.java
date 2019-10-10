package com.altran.carrinhodecompras.entities;

import java.math.BigDecimal;
import java.util.Comparator;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(Include.NON_NULL)
public class Item{

	@NonNull
	@DBRef
	@EqualsAndHashCode.Include
	private Produto produto;
	
	private Long quantidade = 1L;
	
	public BigDecimal getTotal() {
		return produto.getValor().multiply(new BigDecimal(quantidade));
	}

	public void aumentar() {
		this.quantidade++;
	}

	public void diminuir() {
		this.quantidade--;
		if( quantidade < 0L) this.quantidade = 0L;		
	}

	@JsonIgnore
	public boolean isVazio() {
		return this.quantidade == 0L;
	}
}
