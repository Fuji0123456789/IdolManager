package com.idol_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 動画像種別
 */
@Entity
@Table(name = "MediaTypes")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 特定のフィールドのみ使用
public class MediaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // IDが一致すれば同一オブジェクトとみなす
    private Integer typeId;

    @Column(nullable = false, length = 50)
    @EqualsAndHashCode.Include // 名前も同一性の判断に含める
    private String typeName;
}
