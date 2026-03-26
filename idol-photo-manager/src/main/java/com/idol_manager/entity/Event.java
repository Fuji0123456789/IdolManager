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
 * イベント
 */
@Entity
@Table(name = "Events")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 特定のフィールドのみ使用
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // IDが一致すれば同一オブジェクトとみなす
    private Integer eventId;

    @Column(nullable = false)
    @EqualsAndHashCode.Include // 名前も同一性の判断に含める
    private String eventName;
}
