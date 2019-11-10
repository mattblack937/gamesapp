package hu.gamesgeek.authenticate;

import hu.gamesgeek.JpaSpringApplication;

import java.util.List;

public class UserTokenRemover extends Thread {

    private List<UserToken> userTokens = JpaSpringApplication.chatServer.getUserTokens();
    private UserToken removableToken;

    public UserTokenRemover(UserToken removableToken) {
        this.removableToken = removableToken;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            userTokens.remove(removableToken);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
