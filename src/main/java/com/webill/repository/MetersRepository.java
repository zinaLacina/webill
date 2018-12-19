package com.webill.repository;

import com.webill.domain.Meters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Meters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetersRepository extends JpaRepository<Meters, Long> {

}
