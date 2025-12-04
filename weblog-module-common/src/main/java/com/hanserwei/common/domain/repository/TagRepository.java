package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    Collection<Tag> findByNameIn(List<String> names);

    Page<Tag> findAll(Specification<Tag> specification, Pageable pageable);

    /**
     * 根据标签名称模糊查询
     * @param name 标签名称关键词
     * @return 标签列表
     */
    List<Tag> findByNameContaining(String name);

}
