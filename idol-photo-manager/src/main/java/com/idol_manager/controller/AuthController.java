package com.idol_manager.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.idol_manager.dto.UserRegistrationForm;
import com.idol_manager.entity.Users;
import com.idol_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute UserRegistrationForm form) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Validated @ModelAttribute UserRegistrationForm form, 
            BindingResult result, Model model) {
        // 1. 基本的な入力チェック（文字数など）
        if (result.hasErrors()) {
            return "register";
        }

        // 2. ユーザ名の重複チェック
        if (userRepository.existsByUsername(form.getUsername())) {
            result.rejectValue("username", "error.user", "このユーザ名は既に使用されています");
            return "register";
        }

        // 3. 登録処理
        Users user = new Users();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword())); // ハッシュ化
        userRepository.save(user);
        return "redirect:/login?register_success";
    }
}
