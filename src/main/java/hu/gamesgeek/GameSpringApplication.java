package hu.gamesgeek;

import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.websocket.dto.UserTokenDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Configuration
public class GameSpringApplication extends WebSecurityConfigurerAdapter {

	private static WSServer wsServer;

    public static void main(String[] args) {
        SpringApplication.run(GameSpringApplication.class, args);
        wsServer = new WSServer(9000);
        wsServer.start();
        new UserTokenRemover().run();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and().logout().disable();
	}

	private static class UserTokenRemover extends Thread {

		private List<UserTokenDTO> toRemove = new ArrayList<>();

		@Override
		public void run() {
			try {
				Thread.sleep(10000);
				ConnectionHandler.getUserTokens().remove(toRemove);
				toRemove.clear();
				toRemove.addAll(ConnectionHandler.getUserTokens());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
