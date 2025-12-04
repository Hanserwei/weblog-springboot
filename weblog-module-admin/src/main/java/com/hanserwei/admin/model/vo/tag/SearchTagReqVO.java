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
public class SearchTagReqVO {

    @NotEmpty(message = "标签查询关键词不能为空！")
    private String key;
}
