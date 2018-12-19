package com.webill.repository;

import com.webill.domain.AssignMeters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the AssignMeters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignMetersRepository extends JpaRepository<AssignMeters, Long> {

    @Query("select assign_meters from AssignMeters assign_meters where assign_meters.metersUser.login = ?#{principal.username}")
    Page<AssignMeters> findByMetersUserIsCurrentUser(Pageable pageable);
    Page<AssignMeters> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<AssignMeters> findOneByMetersUserLogin(String login);

}
