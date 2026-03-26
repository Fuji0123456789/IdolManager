package com.idol_manager.dto;

import java.util.List;

import lombok.Data;

@Data
public class MediaUpdateForm {
    private Integer mediaId;
    private String acquiredDate; // yyyy-MM-dd
    private Integer locationId;
    private Integer eventId;
    private Integer typeId;
    private List<Integer> memberIds; // 複数選択対応
}
