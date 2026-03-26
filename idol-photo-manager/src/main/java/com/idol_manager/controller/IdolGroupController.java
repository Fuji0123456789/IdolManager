package com.idol_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idol_manager.dto.IdolGroupForm;
import com.idol_manager.entity.IdolGroup;
import com.idol_manager.repository.IdolGroupRepository;

@Controller
@RequestMapping("/groups") // ここで /groups を受ける
public class IdolGroupController {

	@Autowired
	private IdolGroupRepository idolGroupRepository;

	@GetMapping("/register") // ここで /register を受ける
    public String showRegisterForm() {
        return "groups/register"; // templates/groups/register.html を探す
    }

    // 保存処理用のメソッドが必要
    @PostMapping("/register") // ← @GetMapping ではなく @PostMapping
    public String registerGroup(IdolGroupForm form) { // ここで引数として受け取る
        // デバッグ用のログを入れてみてください
        System.out.println("登録するグループ名: " + form.getGroupName());

        // 保存処理...
        IdolGroup group = new IdolGroup();
        group.setGroupName(form.getGroupName());
        
        idolGroupRepository.save(group);
        return "redirect:/groups/list";
    }

    @GetMapping("/list") // URL: /groups/list
    public String listGroups(Model model) {
        // データベースから全グループを取得
        List<IdolGroup> groups = idolGroupRepository.findAll();
        // 画面（Thymeleaf）に渡す
        model.addAttribute("groups", groups);
        // groups/list.html を表示
        return "groups/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id") Integer id) {
        // IDを指定して削除を実行
        idolGroupRepository.deleteById(id);
        
        // 削除後は一覧画面にリダイレクト
        return "redirect:/groups/list";
    }
}
