package com.idol_manager.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * アイドルグループ
 */
@Entity
@Table(name = "IdolGroups")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 特定のフィールドのみ使用
public class IdolGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // IDが一致すれば同一オブジェクトとみなす
    private Integer groupId;

    @Column(nullable = false, length = 100)
    @EqualsAndHashCode.Include // 名前も同一性の判断に含める
    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<Member> members;
}
