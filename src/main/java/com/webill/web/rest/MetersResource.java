package com.webill.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.zxing.WriterException;
import com.webill.config.S3Config;
import com.webill.domain.Meters;
import com.webill.helper.HelperRead;
import com.webill.repository.MetersRepository;
import com.webill.repository.S3Services;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Meters.
 */
@RestController
@RequestMapping("/api")
public class MetersResource {

    private final Logger log = LoggerFactory.getLogger(MetersResource.class);

    private static final String ENTITY_NAME = "meters";


    private final MetersRepository metersRepository;

    @Autowired
    private S3Config s3Config;

    @Autowired
    S3Services s3Services;

    //public String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"public"+File.separator+"uploads";
    public String path = "/tmp";



    public MetersResource(MetersRepository metersRepository) {
        this.metersRepository = metersRepository;
    }

    /**
     * POST  /meters : Create a new meters.
     *
     * @param meters the meters to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meters, or with status 400 (Bad Request) if the meters has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meters")
    @Timed
    public ResponseEntity<Meters> createMeters(@RequestBody Meters meters) throws URISyntaxException {
        log.debug("REST request to save Meters : {}", meters);
        if (meters.getId() != null) {
            throw new BadRequestAlertException("A new meters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        meters.setDateCreated(Instant.now());
        meters.setDateModified(Instant.now());
        Meters result = metersRepository.save(meters);
       // System.out.println(meters.toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.from(Instant.now()));

        String fileName = String.format(result.getId()+"_%1$tB-%1$tY.png", cal);

//Path to put the QRcode

        String qrCodeText = result.getId()+";"+meters.getAddressMeters()+";"+meters.getLatituude()+";"+meters.getLongitude();
        //String path = "https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+fileName;


        //write the text to the qrcode
        try {
            HelperRead.generateQRCodeImage(qrCodeText,400,400,path+File.separator+fileName);
            result.setQrCode(fileName);
            result = metersRepository.save(result);

//            upload to s3
            s3Services.uploadFileFromServer(fileName,new File(path+File.separator+fileName));

            // Delete the file
            File file = new File(path+File.separator+fileName);
            file.delete();

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ResponseEntity.created(new URI("/api/meters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meters : Updates an existing meters.
     *
     * @param meters the meters to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meters,
     * or with status 400 (Bad Request) if the meters is not valid,
     * or with status 500 (Internal Server Error) if the meters couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meters")
    @Timed
    public ResponseEntity<Meters> updateMeters(@RequestBody Meters meters) throws URISyntaxException {
        log.debug("REST request to update Meters : {}", meters);
        if (meters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        meters.setDateModified(Instant.now());

        // System.out.println(meters.toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.from(Instant.now()));

        String fileName = String.format(meters.getId()+"_%1$tB-%1$tY.png", cal);

        //Path to put the QRcode
        String qrCodeText = meters.getId()+";"+meters.getAddressMeters()+";"+meters.getLatituude()+";"+meters.getLongitude();

        //write the text to the qrcode
        s3Services.deleteFile(meters.getQrCode());
        try {
            HelperRead.generateQRCodeImage(qrCodeText,400,400,path+File.separator+fileName);
            meters.setQrCode(fileName);

            //   upload to s3
            s3Services.uploadFileFromServer(fileName,new File(path+File.separator+fileName));

            // Delete the file
            File file = new File(path+File.separator+fileName);
            file.delete();

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Meters result = metersRepository.save(meters);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, meters.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meters : get all the meters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of meters in body
     */
    @GetMapping("/meters")
    @Timed
    public ResponseEntity<List<Meters>> getAllMeters(Pageable pageable) {
        log.debug("REST request to get a page of Meters");
        Page<Meters> page = metersRepository.findAll(pageable);
        page.forEach(setting -> {
            setting.setQrCode("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+setting.getQrCode());
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /meters/:id : get the "id" meters.
     *
     * @param id the id of the meters to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meters, or with status 404 (Not Found)
     */
    @GetMapping("/meters/{id}")
    @Timed
    public ResponseEntity<Meters> getMeters(@PathVariable Long id) {
        log.debug("REST request to get Meters : {}", id);
        Optional<Meters> meters = metersRepository.findById(id);
        meters.get().setQrCode("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+meters.get().getQrCode());
        return ResponseUtil.wrapOrNotFound(meters);
    }

    /**
     * DELETE  /meters/:id : delete the "id" meters.
     *
     * @param id the id of the meters to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meters/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeters(@PathVariable Long id) {
        log.debug("REST request to delete Meters : {}", id);

        //getting the setting logo name
        Meters meters = metersRepository.findById(id).get();
        if(meters!=null){
            String nomLogo = meters.getQrCode();
            s3Services.deleteFile(nomLogo);
        }
        metersRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
