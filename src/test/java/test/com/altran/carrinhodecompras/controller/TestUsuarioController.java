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
import com.altran.carrinhodecompras.entities.Usuario;
import com.altran.carrinhodecompras.repositories.UsuarioRepository;

import test.com.altran.carrinhodecompras.builders.UsuarioBuilder;

@SpringBootTest(classes = CarrinhoDeComprasApplication.class, webEnvironment = WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestUsuarioController {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsuarioRepository repository;

	@Test
	public void deveRetornarTodosOsRegistros() throws Exception {
		List<Usuario> usuarios = Arrays.asList(
				UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com"),
				UsuarioBuilder.getBuilder().create(2L, "usuario 2", "usuario2@usuario.com"),
				UsuarioBuilder.getBuilder().create(3L, "usuario 3", "usuario3@usuario.com"));

		given(repository.findAll()).willReturn(usuarios);

		mvc.perform(get("/api/usuarios")).andExpect(status().isOk())
				.andExpect(content().json("[{'id': 1, 'nome': 'usuario 1', 'email':'usuario1@usuario.com'},"
						+ "{'id': 2, 'nome': 'usuario 2', 'email':'usuario2@usuario.com'},"
						+ "{'id': 3, 'nome': 'usuario 3', 'email':'usuario3@usuario.com'}]"));
	}

	@Test
	public void deveRetornarRegistroPelaChave() throws Exception {
		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");

		given(repository.findById(1L)).willReturn(Optional.of(usuario));

		mvc.perform(get("/api/usuarios/1")).andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'nome': 'usuario 1', 'email':'usuario1@usuario.com'}"));
	}

	@Test
	public void naoDeveRetornarRegistroPelaChave() throws Exception {

		given(repository.findById(1L)).willReturn(Optional.empty());

		mvc.perform(get("/api/usuarios/1")).andExpect(status().isNotFound());
	}

	@Test
	public void deveraIncluirUmNovoUsuario() throws Exception {

		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		given(repository.findByEmail(any(String.class))).willReturn(null);
		given(repository.save((Usuario) any(Usuario.class))).willReturn(usuario);

		mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"usuario 1\", \"email\": \"usuario1@usuario.com\"}")).andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'nome': 'usuario 1', 'email':'usuario1@usuario.com'}"));

	}

	@Test
	public void naoDeveIncluirNovosUsuariosComOMesmoEmailJaCadastrado() throws Exception {
		
		given(repository.findByEmail(any(String.class))).willReturn(new Usuario());

		mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"usuario 1\", \"email\": \"usuario1@usuario.com\"}"))
				.andExpect(status().isConflict());
	}

	@Test
	public void deveraSalvarUmUsuario() throws Exception {

		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		given(repository.findById(1L)).willReturn(Optional.of(usuario));
		given(repository.save((Usuario) any(Usuario.class))).willReturn(usuario);

		mvc.perform(put("/api/usuarios/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"usuario 1\", \"email\": \"usuario1@usuario.com\"}")).andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'nome': 'usuario 1', 'email':'usuario1@usuario.com'}"));

	}
	
	@Test
	public void naoDeveraSalvarPoisIDNaoExiste() throws Exception {
		
		given(repository.findById(1L)).willReturn(Optional.empty());
		
		mvc.perform(put("/api/usuarios/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"usuario 1\", \"email\": \"usuario1@usuario.com\"}"))
				.andExpect(status().isNotFound());
				
	}
	
	@Test
	public void deveraExcluirUmUsuario() throws Exception {

		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		given(repository.findById(1L)).willReturn(Optional.of(usuario));
		willDoNothing().given(repository).delete(any(Usuario.class));
		
		mvc.perform(delete("/api/usuarios/1"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id': 1, 'nome': 'usuario 1', 'email':'usuario1@usuario.com'}"));

	}
	
	@Test
	public void naoDeveraExcluirUmUsuario() throws Exception {

		given(repository.findById(1L)).willReturn(Optional.empty());
				
		mvc.perform(delete("/api/usuarios/1"))
				.andExpect(status().isNotFound());

	}
	
}
