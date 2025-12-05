package com.hanserwei.common.domain.repository;

import com.hanserwei.common.domain.dataobject.BlogSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogSettingsRepository extends JpaRepository<BlogSettings, Long> {

}
