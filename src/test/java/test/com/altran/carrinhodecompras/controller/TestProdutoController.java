package test.com.altran.carrinhodecompras.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.altran.carrinhodecompras.CarrinhoDeComprasApplication;
import com.altran.carrinhodecompras.entities.Produto;
import com.altran.carrinhodecompras.repositories.ProdutoRepository;

import test.com.altran.carrinhodecompras.builders.ProdutoBuilder;

@SpringBootTest(classes = CarrinhoDeComprasApplication.class, webEnvironment = WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestProdutoController {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProdutoRepository repository;

	@Test
	public void deveRetornarTodosOsRegistros() throws Exception {
		List<Produto> produtos = Arrays.asList(
				ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE),
				ProdutoBuilder.getBuilder().create(2L, "produto 2", BigDecimal.ONE),
				ProdutoBuilder.getBuilder().create(3L, "produto 3", BigDecimal.ONE));

		given(repository.findAll()).willReturn(produtos);

		mvc.perform(get("/api/produtos")).andExpect(status().isOk())
				.andExpect(content().json("[{'id': 1, 'nome': 'produto 1', 'valor':1.00},"
						+ "{'id': 2, 'nome': 'produto 2', 'valor':1.00},"
						+ "{'id': 3, 'nome': 'produto 3', 'valor':1.00}]"));
	}

	@Test
	public void deveRetornarRegistroPelaChave() throws Exception {
		Produto produto = ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE);

		given(repository.findById(1L)).willReturn(Optional.of(produto));

		mvc.perform(get("/api/produtos/1")).andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'nome': 'produto 1', 'valor':1.00}"));
	}

	@Test
	public void naoDeveRetornarRegistroPelaChave() throws Exception {

		given(repository.findById(1L)).willReturn(Optional.empty());

		mvc.perform(get("/api/produtos/1")).andExpect(status().isNotFound());
	}

	@Test
	public void deveraIncluirUmNovoProduto() throws Exception {

		Produto produto = ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE);
		given(repository.save((Produto) any(Produto.class))).willReturn(produto);

		mvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"produto 1\", \"valor\": 1.00}")).andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'nome': 'produto 1', 'valor':1.00}"));

	}

	@Test
	public void deveraSalvarUmProduto() throws Exception {

		Produto produto = ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE);
		given(repository.findById(1L)).willReturn(Optional.of(produto));
		given(repository.save((Produto) any(Produto.class))).willReturn(produto);

		mvc.perform(put("/api/produtos/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"produto 1\", \"valor\": 1.00}")).andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'nome': 'produto 1', 'valor':1.00}"));

	}
	
	@Test
	public void naoDeveraSalvarPoisIDNaoExiste() throws Exception {
		
		given(repository.findById(1L)).willReturn(Optional.empty());
		
		mvc.perform(put("/api/produtos/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"produto 1\", \"valor\": 1.00}"))
				.andExpect(status().isNotFound());
				
	}
	
	@Test
	public void deveraExcluirUmProduto() throws Exception {

		Produto produto = ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE);
		given(repository.findById(1L)).willReturn(Optional.of(produto));
		willDoNothing().given(repository).delete(any(Produto.class));
		
		mvc.perform(delete("/api/produtos/1"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'nome': 'produto 1', 'valor':1.00}"));

	}
	
	@Test
	public void naoDeveraExcluirUmProduto() throws Exception {

		given(repository.findById(1L)).willReturn(Optional.empty());
				
		mvc.perform(delete("/api/produtos/1"))
				.andExpect(status().isNotFound());

	}
	
}
