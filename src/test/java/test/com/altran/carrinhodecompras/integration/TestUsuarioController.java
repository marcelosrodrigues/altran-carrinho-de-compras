package test.com.altran.carrinhodecompras.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.altran.carrinhodecompras.entities.Usuario;
import com.altran.carrinhodecompras.repositories.UsuarioRepository;

import test.com.altran.carrinhodecompras.builders.UsuarioBuilder;

@SpringBootTest(classes = CarrinhoDeComprasApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestUsuarioController {

	@LocalServerPort
	private int localPort;

	@Autowired
	private UsuarioRepository repository;

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
	public void deveraIncluirUmNovoUsuario() throws Exception {

		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"usuario 1\", \"email\": \"usuario1@usuario.com\"}", headers);
		ResponseEntity<Usuario> response = restTemplate.exchange(createURLWithPort("/api/usuarios"), HttpMethod.POST,
				entity, Usuario.class);

		Usuario usuario = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(usuario.getId());

	}

	@Test
	public void naoDeveIncluirNovosUsuariosComOMesmoEmailJaCadastrado() throws Exception {

		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"usuario 1\", \"email\": \"usuario1@usuario.com\"}", headers);
		ResponseEntity<Usuario> response = restTemplate.exchange(createURLWithPort("/api/usuarios"), HttpMethod.POST,
				entity, Usuario.class);

		Usuario usuario = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(usuario.getId());

		ResponseEntity error = restTemplate.exchange(createURLWithPort("/api/usuarios"), HttpMethod.POST, entity,
				String.class);
		assertEquals(HttpStatus.CONFLICT, error.getStatusCode());

	}

	@Test
	public void deveraSalvarUmUsuario() throws Exception {

		Usuario usuario = UsuarioBuilder.getBuilder().create(null, "usuario 1", "usuario1@usuario.com");
		usuario = repository.save(usuario);

		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"usuario 2\", \"email\": \"usuario2@usuario.com\"}", headers);

		ResponseEntity<Usuario> response = restTemplate.exchange(createURLWithPort("/api/usuarios/" + usuario.getId()),
				HttpMethod.PUT, entity, Usuario.class);

		Usuario salvo = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	public void naoDeveraSalvarPoisIDNaoExiste() throws Exception {

		HttpEntity<String> entity = new HttpEntity<String>(
				"{\"nome\": \"usuario 2\", \"email\": \"usuario2@usuario.com\"}", headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/usuarios/1"), HttpMethod.PUT,
				entity, String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	public void deveraExcluirUmUsuario() throws Exception {

		Usuario usuario = UsuarioBuilder.getBuilder().create(null, "usuario 1", "usuario1@usuario.com");
		usuario = repository.save(usuario);

		HttpEntity<String> entity = new HttpEntity<String>(
				null, headers);

		ResponseEntity<Usuario> response = restTemplate.exchange(createURLWithPort("/api/usuarios/" + usuario.getId()),
				HttpMethod.DELETE, entity, Usuario.class);

		Usuario salvo = response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@Test
	public void naoDeveraExcluirUmUsuario() throws Exception {

		HttpEntity<String> entity = new HttpEntity<String>(
				null, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/usuarios/1"),
				HttpMethod.DELETE, entity, String.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

}
