package br.com.pawloandre.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Desabilitar CSRF para APIs REST
				.authorizeHttpRequests(auth -> auth //
						
						// Permitir acesso ao Swagger sem autenticação
						
						.requestMatchers( //
								"/swagger-ui.html", //
								"/v3/api-docs/**", //
								"/swagger-ui/**", //
								"/swagger-resources/**", //
								"/webjars/**" //
						).permitAll() //

						// Permitir acesso ao console H2 sem autenticação
						.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()

						// Proteger outros endpoints
						
						.requestMatchers("/users/public").permitAll() // Endpoint público
						.requestMatchers("/users/admin").hasRole("ADMIN") // Apenas ADMIN
						.requestMatchers("/users/**").authenticated() // Exige autenticação
						.anyRequest().denyAll() // Bloqueia todas as outras requisições
						
				).httpBasic(httpBasic -> {
				}); // Usar autenticação básica (usuário/senha)

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.builder().username("user") //
				.password(passwordEncoder().encode("password")) //
				.roles("USER") //
				.build();

		UserDetails admin = User.builder().username("admin") //
				.password(passwordEncoder().encode("admin")) //
				.roles("ADMIN") //
				.build();

		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}