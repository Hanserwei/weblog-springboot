package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.ArticleTagRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTagRelRepository extends JpaRepository<ArticleTagRel, Long> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ArticleTagRel atr where atr.articleId = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);

    List<ArticleTagRel> findByArticleId(Long articleId);

    List<ArticleTagRel> findByTagId(Long tagId);
}
