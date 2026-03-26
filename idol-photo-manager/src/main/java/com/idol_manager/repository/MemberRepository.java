package com.idol_manager.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.idol_manager.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    // メンバーごとの動画像件数を取得する
    // 返却値は [メンバー名, 件数] のリストを想定
    //@Query("SELECT m.memberName as name, COUNT(med) as count " +
    //       "FROM Member m LEFT JOIN m.media med GROUP BY m.memberId, m.memberName")
    // メンバーごとの動画像件数（Media側からJOIN）
    @Query("SELECT m.memberId as id, m.memberName as name, COUNT(med) as count " +
           "FROM Media med JOIN med.members m GROUP BY m.memberId, m.memberName")
    List<Map<String, Object>> countMediaByMember();

    // 特定のメンバーの「チェキの画像」の枚数を取得する
    //@Query("SELECT COUNT(med) FROM Member m JOIN m.media med " +
    // 特定メンバーのチェキ枚数（Media側からJOIN）
    @Query("SELECT COUNT(med) FROM Media med JOIN med.members m " +
           "WHERE m.memberId = :memberId AND med.type.typeName = 'チェキの画像'")
    long countChekiByMemberId(@Param("memberId") Integer memberId);
}
