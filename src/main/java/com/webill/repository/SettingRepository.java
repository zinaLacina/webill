package com.webill.repository;

import com.webill.domain.Setting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Setting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

}
