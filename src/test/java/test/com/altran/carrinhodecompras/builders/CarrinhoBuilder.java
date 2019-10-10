package test.com.altran.carrinhodecompras.builders;

import java.lang.reflect.Field;

import com.altran.carrinhodecompras.entities.CarrinhoDeCompra;
import com.altran.carrinhodecompras.entities.Produto;
import com.altran.carrinhodecompras.entities.Usuario;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CarrinhoBuilder {
	
	private final static CarrinhoBuilder builder = new CarrinhoBuilder();
	
	public static CarrinhoBuilder getBuilder() {
		return builder;
	}
	
	public CarrinhoDeCompra create(Long id, Usuario usuario) throws Exception {
		CarrinhoDeCompra carrinho = new CarrinhoDeCompra(usuario);
		Field field = CarrinhoDeCompra.class.getDeclaredField("id");
		field.setAccessible(true);
		field.set(carrinho, id);
		return carrinho;
		
	}

}
