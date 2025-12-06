package com.hanserwei.admin.service.impl;

import com.hanserwei.admin.model.vo.article.*;
import com.hanserwei.admin.service.AdminArticleService;
import com.hanserwei.common.domain.dataobject.*;
import com.hanserwei.common.domain.repository.*;
import com.hanserwei.common.enums.ResponseCodeEnum;
import com.hanserwei.common.exception.BizException;
import com.hanserwei.common.utils.PageHelper;
import com.hanserwei.common.utils.PageResponse;
import com.hanserwei.common.utils.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class AdminArticleServiceImpl implements AdminArticleService {

    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private ArticleTagRelRepository articleTagRelRepository;
    @Resource
    private ArticleCategoryRelRepository articleCategoryRelRepository;
    @Resource
    private ArticleContentRepository articleContentRepository;
    @Resource
    private CategoryRepository categoryRepository;
    @Resource
    private TagRepository tagRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<?> publishArticle(PublishArticleReqVO publishArticleReqVO) {
        Article article = new Article();
        BeanUtils.copyProperties(publishArticleReqVO, article);
        Article saved = articleRepository.save(article);
        // 拿到保存后的文章id
        Long articleId = saved.getId();
        ArticleContent articleContent = ArticleContent.builder()
                .articleId(articleId)
                .content(publishArticleReqVO.getContent())
                .build();
        articleContentRepository.save(articleContent);
        // 处理分类
        Long categoryId = publishArticleReqVO.getCategoryId();
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(p -> {
                    ArticleCategoryRel articleCategoryRel = ArticleCategoryRel.builder()
                            .articleId(articleId)
                            .categoryId(categoryId)
                            .build();
                    articleCategoryRelRepository.save(articleCategoryRel);
                }, () -> {
                    log.warn("==>文章分类不存在: {}", categoryId);
                    throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
                });
        // 保存标签
        List<String> tags = publishArticleReqVO.getTags();
        insertTags(articleId, tags);
        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<?> deleteArticle(DeleteArticleReqVO deleteArticleReqVO) {
        Long id = deleteArticleReqVO.getId();
        articleRepository.deleteById(id);
        articleCategoryRelRepository.deleteByArticleId(id);
        articleTagRelRepository.deleteByArticleId(id);
        return Response.success();
    }

    @Override
    public PageResponse<FindArticlePageListRspVO> findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO) {
        return PageHelper.findPageList(
                articleRepository,
                findArticlePageListReqVO,
                findArticlePageListReqVO.getTitle(),
                "title",
                findArticlePageListReqVO.getStartDate(),
                findArticlePageListReqVO.getEndDate(),
                article -> FindArticlePageListRspVO.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .cover(article.getCover())
                        .createTime(LocalDateTime.ofInstant(article.getCreateTime(), ZoneId.systemDefault()))
                        .build()
        );
    }


    @Override
    public Response<FindArticleDetailRspVO> findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long id = findArticleDetailReqVO.getId();
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("==>文章不存在: {}", id);
                    return new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
                });
        // 文章正文详情
        ArticleContent articleContent = articleContentRepository.findByArticleId(id);
        // 所属分类
        ArticleCategoryRel articleCategoryRel = articleCategoryRelRepository.findByArticleId(id);
        // 对应标签集合
        List<ArticleTagRel> articleTagRelList = articleTagRelRepository.findByArticleId(id);
        List<Long> tags = articleTagRelList.stream().map(ArticleTagRel::getTagId).toList();
        // 封装响应结果
        FindArticleDetailRspVO findArticleDetailRspVO = new FindArticleDetailRspVO();
        BeanUtils.copyProperties(article, findArticleDetailRspVO);
        findArticleDetailRspVO.setTagIds(tags);
        findArticleDetailRspVO.setCategoryId(articleCategoryRel.getCategoryId());
        findArticleDetailRspVO.setContent(articleContent.getContent());
        return Response.success(findArticleDetailRspVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<?> updateArticle(UpdateArticleReqVO updateArticleReqVO) {
        Long id = updateArticleReqVO.getId();
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("==>欲更新的文章不存在: {}", id);
                    return new BizException(ResponseCodeEnum.ARTICLE_NOT_EXIST);
                });
        BeanUtils.copyProperties(updateArticleReqVO, article);
        // 更新文章正文
        ArticleContent articleContent = articleContentRepository.findByArticleId(id);
        articleContent.setContent(updateArticleReqVO.getContent());
        articleContentRepository.save(articleContent);
        // 更新分类
        Long categoryId = updateArticleReqVO.getCategoryId();
        // 验证该ID的分类是否存在
        categoryRepository.findById(categoryId).orElseThrow(() -> {
            log.warn("==>欲更新的文章其分类不存在: {}", categoryId);
            return new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        });
        // 先删除关联的分类记录，再插入新的关联记录
        articleCategoryRelRepository.deleteByArticleId(id);
        ArticleCategoryRel articleCategoryRel = ArticleCategoryRel.builder()
                .articleId(id)
                .categoryId(categoryId)
                .build();
        articleCategoryRelRepository.save(articleCategoryRel);
        // 删除该文章的标签关联
        articleTagRelRepository.deleteByArticleId(id);
        insertTags(id, updateArticleReqVO.getTags());
        return Response.success();
    }

    /**
     * 插入标签
     *
     * @param articleId 文章ID
     * @param tags      标签列表（可能是ID字符串或标签名称）
     */
    private void insertTags(Long articleId, List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return;
        }

        for (String tagStr : tags) {
            Long tagId;

            // 判断是否为数字ID
            try {
                tagId = Long.parseLong(tagStr);
                // 验证该ID的标签是否存在
                Optional<Tag> tagOptional = tagRepository.findById(tagId);
                if (tagOptional.isEmpty()) {
                    log.warn("==>标签ID不存在: {}", tagId);
                    throw new BizException(ResponseCodeEnum.TAG_NOT_EXISTED);
                }
            } catch (NumberFormatException e) {
                // 不是数字，说明是新标签名称，需要先保存标签
                Tag newTag = Tag.builder()
                        .name(tagStr)
                        .build();
                Tag savedTag = tagRepository.save(newTag);
                tagId = savedTag.getId();
                log.info("==>新增标签: {}, ID: {}", tagStr, tagId);
            }

            // 保存文章-标签关联关系
            ArticleTagRel articleTagRel = ArticleTagRel.builder()
                    .articleId(articleId)
                    .tagId(tagId)
                    .build();
            articleTagRelRepository.save(articleTagRel);
        }
    }
}
