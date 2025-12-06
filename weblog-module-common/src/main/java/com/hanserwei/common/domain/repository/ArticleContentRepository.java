package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.ArticleContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleContentRepository extends JpaRepository<ArticleContent, Long> {
    ArticleContent findByArticleId(Long articleId);
}
