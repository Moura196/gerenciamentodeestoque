package estoque.desafio.gerenciamento.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import estoque.desafio.gerenciamento.entities.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends  UsernamePasswordAuthenticationFilter {
	
	public static final String SECRET_JWT = "6c3c9f41-9fc3-461c-a60b-a8adfd03f698";//"c5e1fb88-dabb-4e0f-a4ba-12eadb58355c"; //<<---- NÃO ESQUECER DE COLOCAR ISSO AQUIIII
	private final AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication (HttpServletRequest request,
												 HttpServletResponse response)
			throws AuthenticationException {
		try {
			Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					usuario.getMatricula(), usuario.getSenha(), new ArrayList<GrantedAuthority>()
					));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse  response,
											FilterChain chain,
											Authentication authResult)
			throws IOException, ServletException {
		
		UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authResult.getPrincipal();
		
		String role = userDetailsCustom.getAuthorities().stream().findFirst().get().toString();
		
		String token = JWT.create()
				.withSubject(userDetailsCustom.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+600000))
				.withClaim("permissao", role)
				.sign(Algorithm.HMAC256(SECRET_JWT));
		
		response.getWriter().write(token);
		response.getWriter().flush();
	}

}