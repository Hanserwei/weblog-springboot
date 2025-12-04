package com.hanserwei.admin.service.impl;

import com.hanserwei.admin.model.vo.tag.*;
import com.hanserwei.admin.service.AdminTagService;
import com.hanserwei.common.domain.dataobject.Tag;
import com.hanserwei.common.domain.repository.TagRepository;
import com.hanserwei.common.enums.ResponseCodeEnum;
import com.hanserwei.common.model.vo.SelectRspVO;
import com.hanserwei.common.utils.PageHelper;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AdminTagServiceImpl implements AdminTagService {

    @Resource
    private TagRepository tagRepository;


    /**
     * 添加标签
     *
     * @param addTagReqVO 添加标签请求对象
     * @return 响应结果
     */
    @Override
    public Response<?> addTag(AddTagReqVO addTagReqVO) {
        // 获取标签列表
        List<String> tagList = addTagReqVO.getTags();

        // 对标签进行清洗：去除空格、过滤空字符串、去重
        List<String> names = tagList.stream()
                .map(String::trim)  // 去除首尾空格
                .filter(s -> !s.isEmpty())  // 过滤空字符串
                .distinct()  // 去重
                .toList();

        // 查询数据库中已存在的标签名称
        List<String> exists = tagRepository.findByNameIn(names).stream()
                .map(Tag::getName)
                .toList();

        // 筛选出需要新建的标签，并构建标签对象
        List<Tag> toCreate = names.stream()
                .filter(n -> !exists.contains(n))  // 过滤掉已存在的标签
                .map(n -> Tag.builder().name(n).build())  // 构建标签对象
                .toList();

        // 批量保存新标签到数据库
        if (!toCreate.isEmpty()) {
            tagRepository.saveAllAndFlush(toCreate);
        }

        return Response.success();
    }

    @Override
    public PageResponse<FindTagPageListRspVO> findTagList(FindTagPageListReqVO findTagPageListReqVO) {
        return PageHelper.findPageList(
                tagRepository,
                findTagPageListReqVO,
                findTagPageListReqVO.getName(),
                findTagPageListReqVO.getStartDate(),
                findTagPageListReqVO.getEndDate(),
                tag -> FindTagPageListRspVO.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .createTime(LocalDateTime.ofInstant(tag.getCreateTime(), ZoneId.systemDefault()))
                        .build()
        );
    }

    @Override
    public Response<?> deleteTag(DeleteTagReqVO deleteTagReqVO) {

        return tagRepository.findById(deleteTagReqVO.getId())
                .map(tag -> {
                    tag.setIsDeleted(true);
                    tagRepository.save(tag);
                    return Response.success();
                })
                .orElse(Response.fail(ResponseCodeEnum.TAG_NOT_EXIST));
    }

    @Override
    public Response<List<SelectRspVO>> searchTag(SearchTagReqVO searchTagReqVO) {
        // 使用模糊查询获取标签列表
        List<Tag> tags = tagRepository.findByNameContaining(searchTagReqVO.getKey());
        
        // 将标签转换为下拉列表格式
        List<SelectRspVO> vos = tags.stream()
                .map(tag -> SelectRspVO.builder()
                        .label(tag.getName())
                        .value(tag.getId())
                        .build())
                .toList();
        
        return Response.success(vos);
    }
}
