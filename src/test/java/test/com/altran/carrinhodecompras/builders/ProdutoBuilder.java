package test.com.altran.carrinhodecompras.builders;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.altran.carrinhodecompras.entities.Produto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProdutoBuilder {
	
	private final static ProdutoBuilder builder = new ProdutoBuilder();
	
	public static ProdutoBuilder getBuilder() {
		return builder;
	}
	
	public Produto create(Long id, String nome, BigDecimal valor) throws Exception {
		Produto produto = new Produto(nome, valor);
		Field field = Produto.class.getDeclaredField("id");
		field.setAccessible(true);
		field.set(produto, id);
		return produto;
		
	}

}
