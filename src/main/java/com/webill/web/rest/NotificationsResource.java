package com.webill.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webill.domain.Notifications;
import com.webill.repository.NotificationsRepository;
import com.webill.security.SecurityUtils;
import com.webill.web.rest.errors.BadRequestAlertException;
import com.webill.web.rest.util.HeaderUtil;
import com.webill.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notifications.
 */
@RestController
@RequestMapping("/api")
public class NotificationsResource {

    private final Logger log = LoggerFactory.getLogger(NotificationsResource.class);

    private static final String ENTITY_NAME = "notifications";

    private final NotificationsRepository notificationsRepository;

    public NotificationsResource(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    /**
     * POST  /notifications : Create a new notifications.
     *
     * @param notifications the notifications to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notifications, or with status 400 (Bad Request) if the notifications has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notifications")
    @Timed
    public ResponseEntity<Notifications> createNotifications(@RequestBody Notifications notifications) throws URISyntaxException {
        log.debug("REST request to save Notifications : {}", notifications);
        if (notifications.getId() != null) {
            throw new BadRequestAlertException("A new notifications cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notifications result = notificationsRepository.save(notifications);
        return ResponseEntity.created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notifications : Updates an existing notifications.
     *
     * @param notifications the notifications to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notifications,
     * or with status 400 (Bad Request) if the notifications is not valid,
     * or with status 500 (Internal Server Error) if the notifications couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notifications")
    @Timed
    public ResponseEntity<Notifications> updateNotifications(@RequestBody Notifications notifications) throws URISyntaxException {
        log.debug("REST request to update Notifications : {}", notifications);
        if (notifications.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Notifications result = notificationsRepository.save(notifications);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notifications.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notifications : get all the notifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notifications in body
     */
    @GetMapping("/notifications")
    @Timed
    public ResponseEntity<List<Notifications>> getAllNotifications(Pageable pageable) {
        log.debug("REST request to get a page of Notifications");
        String username = SecurityUtils.getCurrentUserLogin().orElse(null);
        if(username==null){
            throw new BadRequestAlertException("Please renew your token by login", ENTITY_NAME, "username");
        }
        Page<Notifications> page = notificationsRepository.findAll(pageable);
        page.stream().filter(p->p.getReceiverUser().getLogin()==username);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notifications/:id : get the "id" notifications.
     *
     * @param id the id of the notifications to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notifications, or with status 404 (Not Found)
     */
    @GetMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Notifications> getNotifications(@PathVariable Long id) {
        log.debug("REST request to get Notifications : {}", id);
        Optional<Notifications> notifications = notificationsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(notifications);
    }
    /**
     * GET  /notifications/:id : get the "id" notifications.
     *
     * @param id the id of the notifications to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notifications, or with status 404 (Not Found)
     */
    @GetMapping("/notifications/checked/{id}")
    @Timed
    public ResponseEntity<Notifications> checkedNotifications(@PathVariable Long id) {
        //log.debug("REST request to get Notifications : {}", id);
        Optional<Notifications> notifications = notificationsRepository.findById(id);
        Notifications notes = notifications.get();
        notes.setChecked(true);
        notificationsRepository.save(notes);
        return ResponseUtil.wrapOrNotFound(notifications);
    }

    /**
     * DELETE  /notifications/:id : delete the "id" notifications.
     *
     * @param id the id of the notifications to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotifications(@PathVariable Long id) {
        log.debug("REST request to delete Notifications : {}", id);

        notificationsRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
