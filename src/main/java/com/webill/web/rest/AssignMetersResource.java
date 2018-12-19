package com.webill.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webill.config.S3Config;
import com.webill.domain.*;
import com.webill.helper.InvoiceGenerator;
import com.webill.repository.*;
import com.webill.security.AuthoritiesConstants;
import com.webill.security.SecurityUtils;
import com.webill.web.rest.errors.BadRequestAlertException;
import com.webill.web.rest.util.HeaderUtil;
import com.webill.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing AssignMeters.
 */
@RestController
@RequestMapping("/api")
public class AssignMetersResource {

    private final Logger log = LoggerFactory.getLogger(AssignMetersResource.class);

    private static final String ENTITY_NAME = "assignMeters";

    private final AssignMetersRepository assignMetersRepository;

    @Autowired
    public BillsRepository billsRepository;

    @Autowired
    public BillSettingRepository billSettingRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public NotificationsRepository notificationsRepository;

    @Autowired
    public SettingRepository settingRepository;

    @Autowired
    private S3Services s3Services;

    @Autowired
    private S3Config s3Config;

    public AssignMetersResource(AssignMetersRepository assignMetersRepository) {
        this.assignMetersRepository = assignMetersRepository;
    }

    /**
     * POST  /assign-meters : Create a new assignMeters.
     *
     * @param assignMeters the assignMeters to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assignMeters, or with status 400 (Bad Request) if the assignMeters has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assign-meters")
    @Timed
    public ResponseEntity<AssignMeters> createAssignMeters(@RequestBody AssignMeters assignMeters) throws URISyntaxException {
        log.debug("REST request to save AssignMeters : {}", assignMeters);
        if (assignMeters.getId() != null) {
            throw new BadRequestAlertException("A new assignMeters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assignMeters.setCreatedAt(Instant.now());
        assignMeters.setUpdateAt(Instant.now());
        AssignMeters result = assignMetersRepository.save(assignMeters);

        // Initialize the consomption and every other things
        Bills bills = new Bills();

        // Get the first line of bill setting
        BillSetting billSetting = billSettingRepository.findAll().get(0);
        bills.setBillSettingApp(billSetting);
        bills.setLastMonthReading(0);
        bills.setOwnerUser(assignMeters.getMetersUser());
        //
        bills.setAmount(billSetting.getProcessing());
        bills.setVerifierMetricBill(assignMeters);
        bills.setDateCreated(Instant.now());
        bills.setDateModified(Instant.now());
        bills.setDeadline(Instant.now());
        bills.setCurrentMonthReading(0);
        String uniqueID = UUID.randomUUID().toString();
        bills.setBillCode(uniqueID);
        bills.setNotRejected(true);
        bills.setImageFile(null);


        //Get the user who is doing the action
        String username = SecurityUtils.getCurrentUserLogin().orElse(null);
        Optional<User> user = userRepository.findOneByLogin(username);
        if(user.isPresent()){
            bills.setVerifierUser(user.get());
        }else{
            bills.setVerifierUser(null);
        }
        bills.setEnabled(true);
        if(billSetting.getProcessing()>0){
            bills.setPaid(false);
        }else{
            bills.setPaid(true);
        }
        String uniqID = UUID.randomUUID().toString();

        //billsRepository.save(bills);
        //create a bill
        //get seting
        Setting setting= settingRepository.findAll().get(0);
        InvoiceGenerator invoiceGenerator = new InvoiceGenerator(bills,setting);
        String filename = assignMeters.getMetersUser().getLogin()+uniqID+"";
        //generate the pdf file
        invoiceGenerator.createPDF(filename);

        //put in s3 bucket
        File tmpInvoice =  new File("/tmp/"+filename+".pdf");
        s3Services.uploadFileFromServer(filename+".pdf", tmpInvoice);
        //update bill
        bills.setBillFile(filename+".pdf");
        //Delete the temp bill file
        tmpInvoice.delete();


        billsRepository.save(bills);

        //create notifications
        Notifications notifications = new Notifications();
        notifications.checked(false);
        notifications.createdAt(Instant.now());
        notifications.setUpdateAt(Instant.now());
        notifications.setMessage("The assign a meter to you, the bill of the preprocessing is available in your bill section");
        notifications.setReceiverUser(assignMeters.getMetersUser());
        if(user.isPresent()){
            notifications.setSenderUser(user.get());
        }else{
            notifications.setSenderUser(null);
        }
        notificationsRepository.save(notifications);




        return ResponseEntity.created(new URI("/api/assign-meters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assign-meters : Updates an existing assignMeters.
     *
     * @param assignMeters the assignMeters to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assignMeters,
     * or with status 400 (Bad Request) if the assignMeters is not valid,
     * or with status 500 (Internal Server Error) if the assignMeters couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/assign-meters")
    @Timed
    public ResponseEntity<AssignMeters> updateAssignMeters(@RequestBody AssignMeters assignMeters) throws URISyntaxException {
        log.debug("REST request to update AssignMeters : {}", assignMeters);
        if (assignMeters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AssignMeters result = assignMetersRepository.save(assignMeters);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assignMeters.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assign-meters : get all the assignMeters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of assignMeters in body
     */
    @GetMapping("/assign-meters")
    @Timed
    public ResponseEntity<List<AssignMeters>> getAllAssignMeters(Pageable pageable) {
        log.debug("REST request to get a page of AssignMeters");
        Page<AssignMeters> page;

        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)){
            page = assignMetersRepository.findAllByOrderByCreatedAtDesc(pageable);
        }else {
            page = assignMetersRepository.findByMetersUserIsCurrentUser(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assign-meters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /assign-meters/:id : get the "id" assignMeters.
     *
     * @param id the id of the assignMeters to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assignMeters, or with status 404 (Not Found)
     */
    @GetMapping("/assign-meters/{id}")
    @Timed
    public ResponseEntity<AssignMeters> getAssignMeters(@PathVariable Long id) {
        log.debug("REST request to get AssignMeters : {}", id);
        Optional<AssignMeters> assignMeters = assignMetersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(assignMeters);
    }

    /**
     * DELETE  /assign-meters/:id : delete the "id" assignMeters.
     *
     * @param id the id of the assignMeters to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assign-meters/{id}")
    @Timed
    public ResponseEntity<Void> deleteAssignMeters(@PathVariable Long id) {
        log.debug("REST request to delete AssignMeters : {}", id);

        assignMetersRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /my-meters -> get the current user's preferences.
     */
    @GetMapping("/my-meters")
    @Timed
    public ResponseEntity<AssignMeters> getUserMeters() {
        String username = SecurityUtils.getCurrentUserLogin().orElse(null);
        log.debug("REST request to get Preferences : {}", username);

        Optional<AssignMeters> preferences = assignMetersRepository.findOneByMetersUserLogin(username);

        if (preferences.isPresent()) {
            return new ResponseEntity<>(preferences.get(), HttpStatus.OK);
        } else {
            AssignMeters defaultAssignMeters = new AssignMeters();
            //defaultPreferences.setWeeklyGoal(10); // default
            return new ResponseEntity<>(defaultAssignMeters, HttpStatus.OK);
        }
    }

}
