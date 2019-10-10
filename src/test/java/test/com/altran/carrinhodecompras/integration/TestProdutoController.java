package test.com.altran.carrinhodecompras.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
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
import com.altran.carrinhodecompras.entities.Produto;
import com.altran.carrinhodecompras.repositories.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import test.com.altran.carrinhodecompras.builders.ProdutoBuilder;

@SpringBootTest(classes = CarrinhoDeComprasApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestProdutoController {

	@LocalServerPort
	private int localPort;

	@Autowired
	private ProdutoRepository repository;

	private TestRestTemplate restTemplate = new TestRestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	private String createURLWithPort(String uri) {
		return "http://localhost:" + localPort + uri;
	}

	@Before
	@After
	public void cleanUp() {
		repository.deleteAll();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
	}
	
	@Test
	public void deveRetornarTodosOsRegistros() throws Exception {
		List<Produto> produtos = Arrays.asList(
				ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE),
				ProdutoBuilder.getBuilder().create(2L, "produto 2", BigDecimal.ONE),
				ProdutoBuilder.getBuilder().create(3L, "produto 3", BigDecimal.ONE));

		repository.saveAll(produtos);

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/produtos"), HttpMethod.GET,
				null, String.class);
		
	

		Collection<Produto> produtosList = Arrays.asList(new ObjectMapper()
					.readerFor(Produto[].class)
					.readValue(response.getBody()));
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(produtosList.size(), 3);
	}
	
	@Test
	public void deveRetornarRegistroPelaChave() throws Exception {
		Produto produto = ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE);

		repository.save(produto);

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		
		ResponseEntity<Produto> response = restTemplate.exchange(createURLWithPort("/api/produtos/" + produto.getId()), HttpMethod.GET,
				null, Produto.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(produto, response.getBody());
	}
	
	@Test
	public void naoDeveRetornarRegistroPelaChave() throws Exception {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/produtos/1"), HttpMethod.GET,
				null, String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void deveraIncluirUmNovoProduto() throws Exception {
		
		
		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"produto 1\", \"valor\": 1.00}", headers);
		ResponseEntity<Produto> response = restTemplate.exchange(createURLWithPort("/api/produtos"), HttpMethod.POST,
				entity, Produto.class);

		Produto produto = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(produto.getId());
	}
	
	@Test
	public void deveraSalvarUmProduto() throws Exception {

		Produto produto = ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE);
		repository.save(produto);

		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"produto 2\", \"valor\": 2.00}", headers);
		ResponseEntity<Produto> response = restTemplate.exchange(createURLWithPort("/api/produtos/" + produto.getId()), HttpMethod.PUT,
				entity, Produto.class);

		Produto saved = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotEquals(produto.getNome(), saved.getNome());
		assertNotEquals(produto.getValor(), saved.getValor());

	}
	
	@Test
	public void naoDeveraSalvarPoisIDNaoExiste() throws Exception {
		
		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"produto 2\", \"valor\": 2.00}", headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/produtos/1"), HttpMethod.PUT,
				entity, String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
				
	}
	
	@Test
	public void deveraExcluirUmProduto() throws Exception {

		Produto produto = ProdutoBuilder.getBuilder().create(1L, "produto 1", BigDecimal.ONE);
		repository.save(produto);
		
		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"produto 2\", \"valor\": 2.00}", headers);
		ResponseEntity<Produto> response = restTemplate.exchange(createURLWithPort("/api/produtos/" + produto.getId()), HttpMethod.DELETE,
				entity, Produto.class);

		Produto saved = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}
	
	@Test
	public void naoDeveraExcluirUmProduto() throws Exception {

		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"produto 2\", \"valor\": 2.00}", headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/produtos/1"), HttpMethod.DELETE,
				entity, String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}



}
