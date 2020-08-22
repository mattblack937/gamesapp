package hu.gamesgeek.model.user;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "FRIENDS",
            joinColumns={ @JoinColumn(name = "FRIEND_ID") },
            inverseJoinColumns = { @JoinColumn(name = "FRIEND2_ID")})
    private Set<User> friends = new HashSet<>();


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }


    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }


//    CREATE TABLE friend (
//            friend_id BIGINT,
//            friend2_id BIGINT,
//            CONSTRAINT pk_friends PRIMARY KEY (friend_id, friend2_id)
//);




}
