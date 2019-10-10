package test.com.altran.carrinhodecompras.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.altran.carrinhodecompras.CarrinhoDeComprasApplication;
import com.altran.carrinhodecompras.entities.CarrinhoDeCompra;
import com.altran.carrinhodecompras.entities.Produto;
import com.altran.carrinhodecompras.entities.Usuario;
import com.altran.carrinhodecompras.repositories.CarrinhoRepository;
import com.altran.carrinhodecompras.repositories.ProdutoRepository;
import com.altran.carrinhodecompras.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import test.com.altran.carrinhodecompras.builders.ProdutoBuilder;
import test.com.altran.carrinhodecompras.builders.UsuarioBuilder;

@SpringBootTest(classes = CarrinhoDeComprasApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestCarrinhoDeComprasController {

	@LocalServerPort
	private int localPort;

	@Autowired
	private CarrinhoRepository repository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	private Usuario usuario;

	private List<Produto> produtos;

	private CarrinhoDeCompra carrinho;

	private String createURLWithPort(String uri) {
		return "http://localhost:" + localPort + uri;
	}

	@Before
	public void before() throws Exception {

		this.usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		this.produtos = Arrays.asList(ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE),
				ProdutoBuilder.getBuilder().create(2L, "produto 2", BigDecimal.ONE),
				ProdutoBuilder.getBuilder().create(3L, "produto 3", BigDecimal.ONE));

		usuarioRepository.save(this.usuario);
		produtoRepository.saveAll(this.produtos);

		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		String body = new ObjectMapper().writerFor(Usuario.class).writeValueAsString(usuario);

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<CarrinhoDeCompra> response = restTemplate.exchange(createURLWithPort("/api/carrinho/abrir"),
				HttpMethod.POST, entity, CarrinhoDeCompra.class);

		this.carrinho = response.getBody();
	}

	@After
	public void after() {

		repository.deleteAll();
		usuarioRepository.deleteAll();
		produtoRepository.deleteAll();

	}

	@Test
	public void deveAbrirCarrinhoDeCompras() throws Exception {

		String body = new ObjectMapper().writerFor(Usuario.class).writeValueAsString(usuario);

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<CarrinhoDeCompra> response = restTemplate.exchange(createURLWithPort("/api/carrinho/abrir"),
				HttpMethod.POST, entity, CarrinhoDeCompra.class);

		CarrinhoDeCompra carrinho = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(carrinho.getId());

	}

	@Test
	public void deveAdicionarItemNoCarrinho() throws Exception {

		String body = new ObjectMapper().writerFor(Produto.class).writeValueAsString(this.produtos.get(0));

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<CarrinhoDeCompra> response = restTemplate.exchange(
				createURLWithPort("/api/carrinho/" + this.carrinho.getId() + "/adicionar"), HttpMethod.POST, entity,
				CarrinhoDeCompra.class);

		CarrinhoDeCompra carrinho = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(carrinho.getItens());
		assertEquals(1, carrinho.getItens().size());
		assertNotNull(carrinho.getTotal());
		assertNotNull(carrinho.getQuantidadeDeItensNoCarrinho());

	}

	@Test
	public void deveAdicionarOMesmoProdutoMaisDeUmaVezNoCarrinho() throws Exception {

		String body = new ObjectMapper().writerFor(Produto.class).writeValueAsString(this.produtos.get(0));

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<CarrinhoDeCompra> response = restTemplate.exchange(
				createURLWithPort("/api/carrinho/" + this.carrinho.getId() + "/adicionar"), HttpMethod.POST, entity,
				CarrinhoDeCompra.class);

		response = restTemplate.exchange(createURLWithPort("/api/carrinho/" + this.carrinho.getId() + "/adicionar"),
				HttpMethod.POST, entity, CarrinhoDeCompra.class);

		CarrinhoDeCompra carrinho = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(carrinho.getItens());
		assertEquals(1, carrinho.getItens().size());
		assertNotNull(carrinho.getTotal());
		assertNotNull(carrinho.getQuantidadeDeItensNoCarrinho());

		assertEquals(new BigDecimal(2), carrinho.getTotal());

	}

	@Test
	public void deveApresentarCarrinhoVazio() throws Exception {

		String body = new ObjectMapper().writerFor(Produto.class).writeValueAsString(this.produtos.get(0));

		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<CarrinhoDeCompra> response = restTemplate.exchange(
				createURLWithPort("/api/carrinho/" + this.carrinho.getId() + "/adicionar"), HttpMethod.POST, entity,
				CarrinhoDeCompra.class);

		response = restTemplate.exchange(createURLWithPort("/api/carrinho/" + this.carrinho.getId() + "/adicionar"),
				HttpMethod.POST, entity, CarrinhoDeCompra.class);

		CarrinhoDeCompra carrinho = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(carrinho.getItens());
		assertEquals(1, carrinho.getItens().size());
		assertNotNull(carrinho.getTotal());
		assertNotNull(carrinho.getQuantidadeDeItensNoCarrinho());

		assertEquals(new BigDecimal(2), carrinho.getTotal());
		
		response = restTemplate.exchange(createURLWithPort("/api/carrinho/" + this.carrinho.getId() + "/remover"),
				HttpMethod.POST, entity, CarrinhoDeCompra.class);
		
		response = restTemplate.exchange(createURLWithPort("/api/carrinho/" + this.carrinho.getId() + "/remover"),
				HttpMethod.POST, entity, CarrinhoDeCompra.class);
		
		carrinho = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(carrinho.getItens());
		assertEquals(BigDecimal.ZERO, carrinho.getTotal());

	}

}
