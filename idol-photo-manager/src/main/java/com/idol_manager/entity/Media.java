package com.idol_manager.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 動画像本体：メイン
 */
@Entity
@Table(name = "Media")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 特定のフィールドのみ使用
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // IDが一致すれば同一オブジェクトとみなす
    private Integer mediaId;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    @EqualsAndHashCode.Include // ファイルパスも同一性の判断に含める
    private String filePath;

    private LocalDate acquiredDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private MediaType type;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 複数メンバー対応の中間テーブル設定
    @ManyToMany
    @JoinTable(
        name = "MediaMembers",
        joinColumns = @JoinColumn(name = "media_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @ToString.Exclude // 無限ループ防止のため、リレーション先はToStringから除外
    private Set<Member> members;
}
