package test.com.altran.carrinhodecompras.builders;

import java.lang.reflect.Field;

import com.altran.carrinhodecompras.entities.Usuario;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UsuarioBuilder {
	
	private final static UsuarioBuilder builder = new UsuarioBuilder();
	
	public static UsuarioBuilder getBuilder() {
		return builder;
	}
	
	public Usuario create(Long id, String nome, String email) throws Exception {
		Usuario usuario = new Usuario(nome, email);
		Field field = Usuario.class.getDeclaredField("id");
		field.setAccessible(true);
		field.set(usuario, id);
		return usuario;
		
	}

}
