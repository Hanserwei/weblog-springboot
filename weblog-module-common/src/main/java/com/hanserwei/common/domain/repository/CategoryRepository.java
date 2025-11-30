package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsCategoryByName(String name);

    Page<Category> findAll(Specification<Category> specification, Pageable pageable);
}
