package com.drm.server.domain.user;

import com.drm.server.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String email;
    private String password;
    private String company;
    private boolean isDeleted;
    public static User toEntity(String email,String password){
        return User.builder().email(email).password(password).isDeleted(false).build();
    }

}
