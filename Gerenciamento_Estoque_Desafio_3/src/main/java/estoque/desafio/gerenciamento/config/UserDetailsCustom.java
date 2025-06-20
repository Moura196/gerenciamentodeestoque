package estoque.desafio.gerenciamento.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import estoque.desafio.gerenciamento.entities.Usuario;

public class UserDetailsCustom implements UserDetails {
	
	private final Optional<Usuario> usuario;

	public UserDetailsCustom(Optional<Usuario> usuario) {
		this.usuario = usuario;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.orElse(new Usuario()).getFuncao());
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(authority);
		return authorities;
	}
	
	@Override
	public  String getPassword() {
		return usuario.orElse(new Usuario()).getSenha();
	}
	
	@Override
	public  String getUsername() {
		return usuario.orElse(new Usuario()).getMatricula();
	}
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
