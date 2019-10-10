package test.com.altran.carrinhodecompras.entities;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.junit.Test;

import com.altran.carrinhodecompras.entities.CarrinhoDeCompra;
import com.altran.carrinhodecompras.entities.Produto;
import com.altran.carrinhodecompras.entities.Usuario;

import test.com.altran.carrinhodecompras.builders.CarrinhoBuilder;
import test.com.altran.carrinhodecompras.builders.UsuarioBuilder;

public class TestCarrinhoDeCompras {

	@Test
	public void adicionarProduto() {

		CarrinhoDeCompra carrinho = new CarrinhoDeCompra(new Usuario("", ""));
		Produto arroz = new Produto("arroz", BigDecimal.ONE);
		Produto feijao = new Produto("feijao", BigDecimal.TEN);
		Produto batata = new Produto("batata", new BigDecimal("3"));

		carrinho.adicionar(arroz);
		carrinho.adicionar(feijao);
		carrinho.adicionar(batata);

		assertEquals(new BigDecimal(14), carrinho.getTotal());

	}

	@Test
	public void deveAtualizarAQuantidadeDeItensCasoAdicioneOMesmoProdutoMaisDeUmaVez() throws Exception {

		CarrinhoDeCompra carrinho = new CarrinhoDeCompra(new Usuario("", ""));
		Produto arroz = new Produto("arroz", BigDecimal.ONE);

		Field id = Produto.class.getDeclaredField("id");
		id.setAccessible(true);
		id.set(arroz, 1L);

		carrinho.adicionar(arroz);
		carrinho.adicionar(arroz);
		carrinho.adicionar(arroz);

		assertEquals(new BigDecimal(3), carrinho.getTotal());
		assertEquals(1, carrinho.getQuantidadeDeItensNoCarrinho());

	}

	@Test
	public void deveAtualizarAQuantidadeDeItensCasoRemovaOProdutoDaLista() throws Exception {

		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		CarrinhoDeCompra carrinho = CarrinhoBuilder.getBuilder().create(1L, usuario);

		Produto arroz = new Produto("arroz", BigDecimal.ONE);

		Field id = Produto.class.getDeclaredField("id");
		id.setAccessible(true);
		id.set(arroz, 1L);

		carrinho.adicionar(arroz);
		carrinho.adicionar(arroz);
		carrinho.adicionar(arroz);

		assertEquals(new BigDecimal(3), carrinho.getTotal());
		assertEquals(1, carrinho.getQuantidadeDeItensNoCarrinho());
		
		carrinho.remover(arroz);
		assertEquals(new BigDecimal(2), carrinho.getTotal());
		assertEquals(1, carrinho.getQuantidadeDeItensNoCarrinho());

	}
	
	@Test
	public void deveRemoverDaListaItensComQuantidadeZeradas() throws Exception {
		
		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		CarrinhoDeCompra carrinho = CarrinhoBuilder.getBuilder().create(1L, usuario);

		Produto arroz = new Produto("arroz", BigDecimal.ONE);

		Field id = Produto.class.getDeclaredField("id");
		id.setAccessible(true);
		id.set(arroz, 1L);

		carrinho.adicionar(arroz);
		
		assertEquals(new BigDecimal(1), carrinho.getTotal());
		assertEquals(1, carrinho.getQuantidadeDeItensNoCarrinho());
		
		carrinho.remover(arroz);
		
		assertEquals(BigDecimal.ZERO, carrinho.getTotal());
		assertEquals(0, carrinho.getQuantidadeDeItensNoCarrinho());
		
	}

}
