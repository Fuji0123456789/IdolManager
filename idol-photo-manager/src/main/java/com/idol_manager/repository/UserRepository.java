package com.idol_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idol_manager.entity.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
    /**
     * ユーザー名でユーザーを検索する
     * Spring Securityの認証処理で使用します
     */
    Optional<Users> findByUsername(String username);

    /**
     * ユーザー名が既に存在するか確認する
     * 新規登録時の重複チェックに使用します
     */
    boolean existsByUsername(String username);
}
