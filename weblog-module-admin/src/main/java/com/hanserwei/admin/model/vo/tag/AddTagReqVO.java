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
public class AddTagReqVO {

    /**
     * 标签集合
     */
    @NotEmpty(message = "标签集合 不能为空")
    private List<String> tags;
}
