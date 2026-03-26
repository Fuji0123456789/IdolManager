package com.idol_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idol_manager.entity.Media;

/**
 * 
 */
@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
    // ファイルパスで登録済みか確認する
    boolean existsByFilePath(String filePath);
    
    // パスからエンティティを取得（重複登録防止用）
    Optional<Media> findByFilePath(String filePath);
    
    // 全体の登録件数（動画像一覧画面で表示）
    long count();
}
