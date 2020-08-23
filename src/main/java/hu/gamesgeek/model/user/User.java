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


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "FRIEND_REQUEST",
            joinColumns={ @JoinColumn(name = "REQUESTER_ID") },
            inverseJoinColumns = { @JoinColumn(name = "REQUESTED_ID")})
    private Set<User> friendRequests = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "friendRequests")
    private Set<User> requestedBy = new HashSet<>();

//    DROP TABLE IF EXISTS `friend_request`;
//    CREATE TABLE IF NOT EXISTS `friend_request` (
//            `REQUESTER_ID` bigint(20) NOT NULL,
//  `REQUESTED_ID` bigint(20) NOT NULL,
//    KEY `REQUESTER_ID` (`REQUESTER_ID`),
//    KEY `REQUESTED_ID` (`REQUESTED_ID`)
//            ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;
//
//--
//        -- Constraints for dumped tables
//--
//
//        --
//        -- Constraints for table `friend_request`
//            --
//    ALTER TABLE `friend_request`
//    ADD CONSTRAINT `FK_REQUESTED_ID` FOREIGN KEY (`REQUESTED_ID`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
//    ADD CONSTRAINT `FK_REQUESTER_ID` FOREIGN KEY (`REQUESTER_ID`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
//    COMMIT;

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

    public Set<User> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Set<User> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public Set<User> getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Set<User> requestedBy) {
        this.requestedBy = requestedBy;
    }
}
