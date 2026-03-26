package com.idol_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationForm {

    @NotBlank(message = "ユーザ名を入力してください")
    @Size(min = 4, max = 20, message = "ユーザ名は4文字以上20文字以内で入力してください")
    private String username;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください")
    private String password;
}
