package com.webill.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webill.config.GetExtension;
import com.webill.config.S3Config;
import com.webill.domain.Setting;
import com.webill.repository.FileStorage;
import com.webill.repository.S3Services;
import com.webill.repository.SettingRepository;
import com.webill.web.rest.errors.BadRequestAlertException;
import com.webill.web.rest.util.HeaderUtil;
import com.webill.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Setting.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SettingResource {

    private final Logger log = LoggerFactory.getLogger(SettingResource.class);

    private static final String ENTITY_NAME = "setting";

    private final SettingRepository settingRepository;

    @Autowired
    S3Services s3Services;



    @Autowired
    private S3Config s3Config;

    public SettingResource(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    /**
     * POST  /settings : Create a new setting.
     *
     * @param file the setting to create
     * @return the ResponseEntity with status 201 (Created) and with body the new setting, or with status 400 (Bad Request) if the setting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/settings")
    @Timed
    public ResponseEntity<Setting> createSetting(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("companyName") String companyName,
                                                 @RequestParam("companyNumber") String companyNumber,
                                                 @RequestParam("companyAddress") String companyAddress,
                                                 @RequestParam("id") String id
                                                 ) throws URISyntaxException {



//        if (id != null  ) {
//            throw new BadRequestAlertException("A new setting cannot already have an ID", ENTITY_NAME, "idexists");
//        }
        Setting setting = new Setting();
        setting.setCompanyAddress(companyAddress);
        setting.setCompanyName(companyName);
        setting.setCompanyNumber(companyNumber);
        setting.setDateCreated(Instant.now());
        setting.setDateModified(Instant.now());
        log.debug("REST request to save Setting : {}", setting);


        Setting result = settingRepository.save(setting);
        //upload file to s3
        if(file!=null) {
            String keyName = file.getOriginalFilename();
            String nomFichier = GetExtension.getValue(keyName, 0) + "_" + result.getId();
            String extension = GetExtension.getValue(keyName, 1);
            keyName = nomFichier + "." + extension;

            s3Services.uploadFile(keyName, file);

            result.setCompanyLogo(keyName);
            result = settingRepository.save(result);

            //fileStorage.store(file);
        }


        return ResponseEntity.created(new URI("/api/settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /settings : Updates an existing setting.
     *
     * @param fileUpdate the setting to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated setting,
     * or with status 400 (Bad Request) if the setting is not valid,
     * or with status 500 (Internal Server Error) if the setting couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/settings")
    @Timed
    public ResponseEntity<Setting> updateSetting(@RequestParam("file") MultipartFile fileUpdate,
                                                 @RequestParam("companyName") String companyNameUpdate,
                                                 @RequestParam("companyNumber") String companyNumberUpdate,
                                                 @RequestParam("companyAddress") String companyAddressUpdate,
                                                 @RequestParam("id") String idUpdate
                                                ) throws URISyntaxException {

        if (idUpdate == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Long idS = new Long(idUpdate);
        Setting setting = settingRepository.findById(idS).get();
        setting.setCompanyAddress(companyAddressUpdate);
        setting.setCompanyName(companyNameUpdate);
        setting.setCompanyNumber(companyNumberUpdate);
        //setting.setDateCreated(Instant.now());
        setting.setDateModified(Instant.now());

        log.debug("REST request to update Setting : {}", setting);

        Setting result = settingRepository.save(setting);
        //upload file to s3
        if(fileUpdate!=null) {
            String keyNameUpdate = fileUpdate.getOriginalFilename();
            String nomFichierUpdate = GetExtension.getValue(keyNameUpdate, 0) + "_" + result.getId();
            String extensionUpdate = GetExtension.getValue(keyNameUpdate, 1);
            keyNameUpdate = nomFichierUpdate + "." + extensionUpdate;

            s3Services.uploadFile(keyNameUpdate, fileUpdate);

            result.setCompanyLogo(keyNameUpdate);
            result = settingRepository.save(result);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, setting.getId().toString()))
            .body(result);
    }

    /**
     * GET  /settings : get all the settings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of settings in body
     */
    @GetMapping("/settings")
    @Timed
    public ResponseEntity<List<Setting>> getAllSettings(Pageable pageable) {
        log.debug("REST request to get a page of Settings");
        Page<Setting> page = settingRepository.findAll(pageable);
        //https://s3-ap-northeast-1.amazonaws.com/webill-s3-bucket/huweip10_5.jpg
        page.forEach(setting -> {
            setting.setCompanyLogo("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+setting.getCompanyLogo());
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/settings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /settings/:id : get the "id" setting.
     *
     * @param id the id of the setting to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the setting, or with status 404 (Not Found)
     */
    @GetMapping("/settings/{id}")
    @Timed
    public ResponseEntity<Setting> getSetting(@PathVariable Long id) {
        log.debug("REST request to get Setting : {}", id);
        Optional<Setting> setting = settingRepository.findById(id);
        setting.get().setCompanyLogo("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+setting.get().getCompanyLogo());
        return ResponseUtil.wrapOrNotFound(setting);
    }

    /**
     * DELETE  /settings/:id : delete the "id" setting.
     *
     * @param id the id of the setting to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/settings/{id}")
    @Timed
    public ResponseEntity<Void> deleteSetting(@PathVariable Long id) {
        log.debug("REST request to delete Setting : {}", id);
        //getting the setting logo name
        Setting setting = settingRepository.findById(id).get();
        if(setting!=null){
            String nomLogo = setting.getCompanyLogo();
                s3Services.deleteFile(nomLogo);
        }

        //s3client.deleteObject("baeldung-bucket","picture/pic.png");
        settingRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
