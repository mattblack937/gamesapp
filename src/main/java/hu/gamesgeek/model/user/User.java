package hu.gamesgeek.model.user;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

//    @PreRemove
//    private void removeActorFromMovies() {
//        for (User friend : friendTo) {
//            friend.getFriends().remove(this);
//        }
//        setFriendTo(new HashSet<>());
//    }

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "FRIEND",
            joinColumns={ @JoinColumn(name = "FRIEND_ID") },
            inverseJoinColumns = { @JoinColumn(name = "FRIEND_TO_ID")})
    private Set<User> friends = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "friends")
    private Set<User> friendTo = new HashSet<>();


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


//    CREATE TABLE `friend` (
//            `FRIEND_ID` bigint(20) NOT NULL,
//  `FRIEND_TO_ID` bigint(20) NOT NULL,
//    PRIMARY KEY (`FRIEND_ID`,`FRIEND_TO_ID`)
//)


    public Set<User> getFriendTo() {
        return friendTo;
    }

    public void setFriendTo(Set<User> friendTo) {
        this.friendTo = friendTo;
    }
}
