package com.webill.repository;

import com.webill.domain.Bills;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Bills entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillsRepository extends JpaRepository<Bills, Long> {

    @Query("select bills from Bills bills where bills.ownerUser.login = ?#{principal.username}")
    List<Bills> findByOwnerUserIsCurrentUser();

    @Query("select bills from Bills bills where bills.verifierUser.login = ?#{principal.username}")
    List<Bills> findByVerifierUserIsCurrentUser();


    List<Bills> findAllByOwnerUser_Login(String login);

}
