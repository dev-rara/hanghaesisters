package com.team6.hanghaesisters.entity;

import com.team6.hanghaesisters.util.PasswordEncConverter;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Convert(converter = PasswordEncConverter.class)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = UserRole.USER;
    }
}
