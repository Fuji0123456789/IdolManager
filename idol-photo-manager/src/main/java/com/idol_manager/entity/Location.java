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
 * 場所
 */
@Entity
@Table(name = "Locations")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 特定のフィールドのみ使用
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // IDが一致すれば同一オブジェクトとみなす
    private Integer locationId;

    @Column(nullable = false)
    @EqualsAndHashCode.Include // 名前も同一性の判断に含める
    private String locationName;
}

// Eventクラスも同様に eventId, eventName で作成してください。 
