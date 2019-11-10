package hu.gamesgeek;

import hu.gamesgeek.authenticate.CorsFilter;
import hu.gamesgeek.websocket.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@SpringBootApplication
@EnableWebSecurity
@Configuration
public class JpaSpringApplication extends WebSecurityConfigurerAdapter{

	public static ChatServer chatServer = new ChatServer(9000);

	public static void main(String[] args) {
		SpringApplication.run(JpaSpringApplication.class, args);
		chatServer.start();
	}

	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/")
				.authenticated()
				.and()
				.formLogin()
				.loginProcessingUrl("/login");
	}
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
				.addFilterBefore(myCorsFilter, ChannelProcessingFilter.class).cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**")
					.permitAll()
				.and()
				.formLogin()
					.loginPage("http://localhost:3000/login")
					.successHandler((httpServletRequest, httpServletResponse, authentication) -> {})
				.failureHandler((request, response, e) -> {
					response.setStatus(401);
					})
					.loginProcessingUrl("/login")
				.and()
				.logout()
					.addLogoutHandler(new LogoutHandler() {
						@Override
						public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
							try {
								httpServletResponse.sendRedirect(("http://localhost:3000/login"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
	}

	@Autowired
	private CorsFilter myCorsFilter;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource,
								PasswordEncoder passwordEncoder) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
				.usersByUsernameQuery("select username, password, enabled from users where username = ?")
				.authoritiesByUsernameQuery("select username, role from users where username = ?");

	}

}
