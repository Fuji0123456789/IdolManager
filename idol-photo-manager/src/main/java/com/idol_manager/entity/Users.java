package com.idol_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer userId;

    @Column(nullable = false, unique = true, length = 50)
    @EqualsAndHashCode.Include
    private String username;

    @Column(nullable = false, length = 255)
    @ToString.Exclude // パスワードがログ等に表示されないように除外
    private String password;

    /**
     * コンストラクタ（新規登録時用）
     */
    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
