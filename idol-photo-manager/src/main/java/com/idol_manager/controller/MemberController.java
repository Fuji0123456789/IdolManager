package com.idol_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.idol_manager.entity.Member;
import com.idol_manager.repository.IdolGroupRepository;
import com.idol_manager.repository.MemberRepository;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final IdolGroupRepository idolGroupRepository;

    // コンストラクタ注入（Autowiredより推奨される方式です）
    public MemberController(MemberRepository memberRepository, IdolGroupRepository idolGroupRepository) {
        this.memberRepository = memberRepository;
        this.idolGroupRepository = idolGroupRepository;
    }

    // 1. メンバー一覧表示
    /**
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String listMembers(Model model) {
        model.addAttribute("members", memberRepository.findAll());
        return "members/list";
    }

    // 2. メンバー登録画面の表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // 所属グループを選択させるために、グループ一覧も渡す
        model.addAttribute("groups", idolGroupRepository.findAll()); // セレクトボックス用
        return "members/register";
    }

    // 3. メンバー登録処理
    /**
     * @param memberName
     * @param groupId
     * @return
     */
    @PostMapping("/register")
    public String registerMember(@RequestParam String memberName, @RequestParam(required = false) Integer groupId) {
        Member member = new Member();
        member.setMemberName(memberName);
        
        // グループが選択されている場合のみセット
        if (groupId != null) {
            member.setGroup(idolGroupRepository.findById(groupId).orElse(null));
        }
        
        memberRepository.save(member);
        return "redirect:/members/list";
    }

    // 4. メンバー削除処理
    @PostMapping("/delete/{id}")
    public String deleteMember(@PathVariable("id") Integer id) {
        memberRepository.deleteById(id);
        return "redirect:/members/list";
    }
    
}
