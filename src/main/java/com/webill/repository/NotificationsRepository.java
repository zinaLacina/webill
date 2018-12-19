package com.webill.repository;

import com.webill.domain.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Notifications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    @Query("select notifications from Notifications notifications where notifications.senderUser.login = ?#{principal.username}")
    List<Notifications> findBySenderUserIsCurrentUser();

    @Query("select notifications from Notifications notifications where notifications.receiverUser.login = ?#{principal.username}")
    List<Notifications> findByReceiverUserIsCurrentUser();

    Page<Notifications> findAllByReceiverUser_Login(String login, Pageable pageable);


}
