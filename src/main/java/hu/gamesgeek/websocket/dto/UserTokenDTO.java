package hu.gamesgeek.websocket.dto;

public class UserTokenDTO {

    private String userId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTokenDTO)){
            return false;
        }
        return ((UserTokenDTO) o).getToken().equals(token) && ((UserTokenDTO) o).getUserId().equals(userId);
    }
}
