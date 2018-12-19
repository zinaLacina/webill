package com.webill.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webill.domain.BillSetting;
import com.webill.helper.HelperRead;
import com.webill.repository.BillSettingRepository;
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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BillSetting.
 */
@RestController
@RequestMapping("/api")
public class BillSettingResource {

    private final Logger log = LoggerFactory.getLogger(BillSettingResource.class);

    private static final String ENTITY_NAME = "billSetting";

    private final BillSettingRepository billSettingRepository;

    public BillSettingResource(BillSettingRepository billSettingRepository) {
        this.billSettingRepository = billSettingRepository;
    }

    /**
     * POST  /bill-settings : Create a new billSetting.
     *
     * @param billSetting the billSetting to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billSetting, or with status 400 (Bad Request) if the billSetting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bill-settings")
    @Timed
    public ResponseEntity<BillSetting> createBillSetting(@RequestBody BillSetting billSetting) throws URISyntaxException {
        log.debug("REST request to save BillSetting : {}", billSetting);
        if (billSetting.getId() != null) {
            throw new BadRequestAlertException("A new billSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        List<BillSetting> checked = billSettingRepository.findAllByTypeBillEquals(billSetting.getTypeBill());
        if(!HelperRead.isNullOrEmpty(checked)){
            throw new BadRequestAlertException("The type of bill already exist", ENTITY_NAME, "idexists");
        }
        billSetting.setDateCreated(Instant.now());
        billSetting.setDateModified(Instant.now());
        billSetting.setTax(billSetting.getTax()/100);
        billSetting.setDiscount(billSetting.getDiscount()/100);
        //billSetting.get
        BillSetting result = billSettingRepository.save(billSetting);
        return ResponseEntity.created(new URI("/api/bill-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bill-settings : Updates an existing billSetting.
     *
     * @param billSetting the billSetting to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billSetting,
     * or with status 400 (Bad Request) if the billSetting is not valid,
     * or with status 500 (Internal Server Error) if the billSetting couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bill-settings")
    @Timed
    public ResponseEntity<BillSetting> updateBillSetting(@RequestBody BillSetting billSetting) throws URISyntaxException {
        log.debug("REST request to update BillSetting : {}", billSetting);
        if (billSetting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        billSetting.setDateModified(Instant.now());
        BillSetting result = billSettingRepository.save(billSetting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, billSetting.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bill-settings : get all the billSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of billSettings in body
     */
    @GetMapping("/bill-settings")
    @Timed
    public ResponseEntity<List<BillSetting>> getAllBillSettings(Pageable pageable) {
        log.debug("REST request to get a page of BillSettings");
        Page<BillSetting> page = billSettingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bill-settings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bill-settings/:id : get the "id" billSetting.
     *
     * @param id the id of the billSetting to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billSetting, or with status 404 (Not Found)
     */
    @GetMapping("/bill-settings/{id}")
    @Timed
    public ResponseEntity<BillSetting> getBillSetting(@PathVariable Long id) {
        log.debug("REST request to get BillSetting : {}", id);
        Optional<BillSetting> billSetting = billSettingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(billSetting);
    }

    /**
     * DELETE  /bill-settings/:id : delete the "id" billSetting.
     *
     * @param id the id of the billSetting to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bill-settings/{id}")
    @Timed
    public ResponseEntity<Void> deleteBillSetting(@PathVariable Long id) {
        log.debug("REST request to delete BillSetting : {}", id);

        billSettingRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /bill-settings : get all the billSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of billSettings in body
     */
    @GetMapping("/active-bill-settings")
    @Timed
    public ResponseEntity<List<BillSetting>> getAllActiveBillSettings(Pageable pageable) {
        log.debug("REST request to get a page of BillSettings");
        Page<BillSetting> page = billSettingRepository.findAllByEnabledTrue(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/active-bill-settings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
