package com.idol_manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.idol_manager.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/**
 * 統計情報の計算と画面遷移を制御します。
 */
@Controller
@RequiredArgsConstructor
public class StatisticsController {

    private final MemberRepository memberRepository;

    /**
     * メンバーごとの動画像件数を表示する画面
     */
    // ドリルダウン: totalCount → メンバー一覧 → チェキ詳細 という流れで、アイドルの活動記録を深掘りできるようになっています。
    @GetMapping("/statistics/members")
    public String showMemberStats(Model model) {
        // Repositoryで作成したカスタムクエリを実行
        List<Map<String, Object>> stats = memberRepository.countMediaByMember();
        
        model.addAttribute("memberStats", stats);
        return "stats_member_list";
    }

    /**
     * 特定メンバーのチェキ枚数を表示する画面
     * @param memberId パスパラメータから取得
     */
    @GetMapping("/statistics/member/{memberId}/cheki")
    public String showChekiStats(@PathVariable Integer memberId, Model model) {
        // メンバー情報を取得
        var member = memberRepository.findById(memberId).orElseThrow();
        
        // チェキの枚数をカウント
        // 動画像種別の判定: countChekiByMemberId メソッドで、種別名が チェキの画像 と一致するかを判定しています。SQLで初期登録した名称と一字一句合わせるのがコツです。
        long chekiCount = memberRepository.countChekiByMemberId(memberId);
        
        model.addAttribute("memberName", member.getMemberName());
        model.addAttribute("chekiCount", chekiCount);
        return "stats_cheki_detail";
    }
}
