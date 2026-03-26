package com.idol_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IdolGroupForm {

    // 新規登録時はnull、更新時はIDが入る想定
    private Long groupId;

    @NotBlank(message = "グループ名を入力してください")
    @Size(max = 100, message = "グループ名は100文字以内で入力してください")
    private String groupName;

}
