package hu.gamesgeek;

import hu.gamesgeek.util.ConnectionHandler;
import hu.gamesgeek.websocket.dto.UserTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@SpringBootApplication
@Configuration
public class GameSpringApplication {

    @Autowired
    private GameFilter gameFilter;

	private static WSServer wsServer;

    public static void main(String[] args) {
        SpringApplication.run(GameSpringApplication.class, args);
        wsServer = new WSServer(9000);
        wsServer.start();
        new UserTokenRemover().run();
    }


	public static WSServer getWsServer() {
		return wsServer;
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
