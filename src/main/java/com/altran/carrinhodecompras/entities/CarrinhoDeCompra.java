package com.altran.carrinhodecompras.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.altran.carrinhodecompras.mongodb.annotations.Sequence;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Document(collection = "carrinho")
@Getter
@RequiredArgsConstructor()
@NoArgsConstructor
@Sequence(sequence = "carrinho_sequence")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(Include.NON_NULL)
public class CarrinhoDeCompra {
	
	@Id
	private Long id;
	
	@DBRef
	@NonNull
	private Usuario usuario;
	
	private BigDecimal total;
	
	private int quantidadeDeItensNoCarrinho;
	
	private List<Item> itens;

	private BigDecimal calculaTotal() {
		if( itens == null || itens.isEmpty() ) return BigDecimal.ZERO;
		return itens.stream()
					.map((item) -> item.getTotal()).reduce((x,y)-> x.add(y))
					.get();		
	}
	
	public void adicionar(Produto produto) {
		if( itens == null ) itens = new ArrayList<Item>();
		final Item item = new Item(produto);
		if( !itens.contains(item) ) itens.add(item);
		else {
			Item founded = itens.stream().filter(i -> i.equals(item)).findFirst().get();
			founded.aumentar();
		}
		
		this.total = calculaTotal();
		this.quantidadeDeItensNoCarrinho = calculaQuantidadeDeItens();
		
	}

	public int calculaQuantidadeDeItens() {
		if( itens == null ) return 0;
		else return itens.size();
	}

	public void remover(Produto produto) {
		final Item item = new Item(produto);
		if( !itens.contains(item)) return;
		
		Item founded = itens.stream().filter(i -> i.equals(item)).findFirst().get();
		founded.diminuir();
		
		Collection<Item> itensZerados = new ArrayList<>();
		itens.stream()
			.filter(i -> i.isVazio() )
			.forEach(i -> { itensZerados.add(i); });
		
		this.itens.removeAll(itensZerados);
		
		this.total = calculaTotal();
		this.quantidadeDeItensNoCarrinho = calculaQuantidadeDeItens();
		
	}

}
