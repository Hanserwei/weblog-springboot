package com.hanserwei.admin.model.vo.tag;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindTagsByIdsReqVO {
    /**
     * 标签 ID 集合
     */
    @NotEmpty(message = "标签 ID 集合不能为空")
    private List<Long> tagIds;
}
