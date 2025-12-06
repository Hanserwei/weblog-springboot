package com.hanserwei.admin.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryByIdRspVO {
    /**
     * 分类 ID
     */
    private Long id;
    /**
     * 分类名称
     */
    private String name;
}
