package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.ArticleCategoryRel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleCategoryRelRepository extends JpaRepository<ArticleCategoryRel, Long> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ArticleCategoryRel acr where acr.articleId = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);

    ArticleCategoryRel findByArticleId(Long articleId);

    List<ArticleCategoryRel> findByCategoryId(Long categoryId);
}
