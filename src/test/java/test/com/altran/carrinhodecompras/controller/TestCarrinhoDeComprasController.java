package test.com.altran.carrinhodecompras.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.altran.carrinhodecompras.entities.CarrinhoDeCompra;
import com.altran.carrinhodecompras.entities.Usuario;
import com.altran.carrinhodecompras.repositories.CarrinhoRepository;

import test.com.altran.carrinhodecompras.builders.CarrinhoBuilder;
import test.com.altran.carrinhodecompras.builders.UsuarioBuilder;

@SpringBootTest(classes = CarrinhoDeComprasApplication.class, webEnvironment = WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestCarrinhoDeComprasController {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CarrinhoRepository repository;
	
	@Test
	public void deveAbrirCarrinhoDeCompras() throws Exception {
		
		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		CarrinhoDeCompra carrinho = CarrinhoBuilder.getBuilder().create(1L, usuario);
		
		given(repository.save(carrinho)).willReturn(carrinho);		
		
		mvc.perform(post("/api/carrinho/abrir")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"id\": 1, \"nome\": \"usuario 1\", \"email\":\"usuario1@usuario.com\"}"))
		   .andExpect(status().isOk())
		   .andExpect(content()
				   		.json("{'id':1,'usuario':{'id':1,'nome':'usuario 1','email':'usuario1@usuario.com'},'total':0,'quantidadeDeItensNoCarrinho':0}"));
					
	}
	
	@Test
	public void deveRetornarRegistroPelaChave() throws Exception {
		CarrinhoDeCompra carrinho = new CarrinhoDeCompra();

		given(repository.findById(1L)).willReturn(Optional.of(carrinho));

		mvc.perform(get("/api/carrinho/1")).andExpect(status().isOk());
	}

	@Test
	public void naoDeveRetornarRegistroPelaChave() throws Exception {

		given(repository.findById(1L)).willReturn(Optional.empty());

		mvc.perform(get("/api/carrinho/1")).andExpect(status().isNotFound());
	}
	
	@Test
	public void deveAdicionarItemNoCarrinho() throws Exception {
		
		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		CarrinhoDeCompra carrinho = CarrinhoBuilder.getBuilder().create(1L, usuario);
		
		given(repository.findById(1L)).willReturn(Optional.of(carrinho));
		given(repository.save(carrinho)).willReturn(carrinho);
		
		mvc.perform(post("/api/carrinho/1/adicionar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\": 1, \"nome\": \"produto 1\", \"valor\": 1.00}"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'usuario':{'id':1,'nome':'usuario 1','email':'usuario1@usuario.com'},'itens':[{'produto':{'id':1,'nome':'produto 1','valor':1.00},'quantidade':1,'total':1.00}],'total':1.00,'quantidadeDeItensNoCarrinho':1}"));
			
	}
	
	@Test
	public void deveAdicionarOMesmoProdutoMaisDeUmaVezNoCarrinho() throws Exception {
		
		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		CarrinhoDeCompra carrinho = CarrinhoBuilder.getBuilder().create(1L, usuario);
		
		given(repository.findById(1L)).willReturn(Optional.of(carrinho));
		given(repository.save(carrinho)).willReturn(carrinho);
		
		mvc.perform(post("/api/carrinho/1/adicionar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\": 1, \"nome\": \"produto 1\", \"valor\": 1.00}"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'usuario':{'id':1,'nome':'usuario 1','email':'usuario1@usuario.com'},'itens':[{'produto':{'id':1,'nome':'produto 1','valor':1.00},'quantidade':1,'total':1.00}],'total':1.00,'quantidadeDeItensNoCarrinho':1}"));
		
		
		mvc.perform(post("/api/carrinho/1/adicionar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\": 1, \"nome\": \"produto 1\", \"valor\": 1.00}"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'usuario':{'id':1,'nome':'usuario 1','email':'usuario1@usuario.com'},'itens':[{'produto':{'id':1,'nome':'produto 1','valor':1.00},'quantidade':2,'total':2.00}],'total':2.00,'quantidadeDeItensNoCarrinho':1}"));

	}
	
	@Test
	public void deveApresentarCarrinhoVazio() throws Exception {
		
		Usuario usuario = UsuarioBuilder.getBuilder().create(1L, "usuario 1", "usuario1@usuario.com");
		CarrinhoDeCompra carrinho = CarrinhoBuilder.getBuilder().create(1L, usuario);
		
		given(repository.findById(1L)).willReturn(Optional.of(carrinho));
		given(repository.save(carrinho)).willReturn(carrinho);
		
		mvc.perform(post("/api/carrinho/1/adicionar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\": 1, \"nome\": \"produto 1\", \"valor\": 1.00}"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'usuario':{'id':1,'nome':'usuario 1','email':'usuario1@usuario.com'},'itens':[{'produto':{'id':1,'nome':'produto 1','valor':1.00},'quantidade':1,'total':1.00}],'total':1.00,'quantidadeDeItensNoCarrinho':1}"));
		
		
		mvc.perform(post("/api/carrinho/1/adicionar")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\": 1, \"nome\": \"produto 1\", \"valor\": 1.00}"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'usuario':{'id':1,'nome':'usuario 1','email':'usuario1@usuario.com'},'itens':[{'produto':{'id':1,'nome':'produto 1','valor':1.00},'quantidade':2,'total':2.00}],'total':2.00,'quantidadeDeItensNoCarrinho':1}"));
		
		
		mvc.perform(post("/api/carrinho/1/remover")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\": 1, \"nome\": \"produto 1\", \"valor\": 1.00}"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'usuario':{'id':1,'nome':'usuario 1','email':'usuario1@usuario.com'},'itens':[{'produto':{'id':1,'nome':'produto 1','valor':1.00},'quantidade':1,'total':1.00}],'total':1.00,'quantidadeDeItensNoCarrinho':1}"));
		
		mvc.perform(post("/api/carrinho/1/remover")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\": 1, \"nome\": \"produto 1\", \"valor\": 1.00}"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'id':1,'usuario':{'id':1,'nome':'usuario 1','email':'usuario1@usuario.com'},'itens':[],'total':0.00,'quantidadeDeItensNoCarrinho':0}"));
		
	}
	
	
	
}
