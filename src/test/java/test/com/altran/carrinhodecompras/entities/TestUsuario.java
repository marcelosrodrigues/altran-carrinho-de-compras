package test.com.altran.carrinhodecompras.entities;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import com.altran.carrinhodecompras.CarrinhoDeComprasApplication;
import com.altran.carrinhodecompras.entities.Usuario;
import com.altran.carrinhodecompras.repositories.UsuarioRepository;

@SpringBootTest(classes = CarrinhoDeComprasApplication.class)
@RunWith(SpringRunner.class)
public class TestUsuario {

	@Autowired
	private UsuarioRepository repository;
	
	@Before
	@After
	public void cleanUp() {
		Usuario usuario = repository.findByEmail("marcelosrodrigues@globo.com");
		if( usuario != null ) repository.delete(usuario);
	}
	
	@Test
	public void criarUsuario() {
		Usuario usuario = new Usuario("Marcelo", "marcelosrodrigues@globo.com");
		usuario = repository.save(usuario);
		
		assertNotNull(usuario.getId());
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void naoPodeSalvarUsuarioRepetido() {
		
		Usuario usuario = new Usuario("Marcelo", "marcelosrodrigues@globo.com");
		usuario = repository.save(usuario);
		
		Usuario usuario2 = new Usuario("Marcelo", "marcelosrodrigues@globo.com");
		usuario2 = repository.save(usuario2);
		
	}

}
