package com.hanserwei.admin.model.vo.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindTagsByIdsRspVO {
    /**
     * 标签 ID
     */
    private Long id;
    /**
     * 标签名称
     */
    private String name;
}
