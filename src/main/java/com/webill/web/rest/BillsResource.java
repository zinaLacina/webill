package com.webill.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.zxing.NotFoundException;
import com.webill.config.GetExtension;
import com.webill.config.S3Config;
import com.webill.domain.*;
import com.webill.helper.HelperRead;
import com.webill.helper.InvoiceGenerator;
import com.webill.repository.*;
import com.webill.security.SecurityUtils;
import com.webill.web.rest.errors.BadRequestAlertException;
import com.webill.web.rest.util.HeaderUtil;
import com.webill.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Bills.
 */
@RestController
@RequestMapping("/api")
public class BillsResource {

    private final Logger log = LoggerFactory.getLogger(BillsResource.class);

    private static final String ENTITY_NAME = "bills";

    private final BillsRepository billsRepository;

    @Autowired
    public AssignMetersRepository assignMetersRepository;

    @Autowired
    FileStorage fileStorage;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BillSettingRepository billSettingRepository;

    @Autowired
    private S3Services s3Services;

    @Autowired
    private S3Config s3Config;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private NotificationsRepository notificationsRepository;




    public BillsResource(BillsRepository billsRepository) {
        this.billsRepository = billsRepository;
    }

    /**
     * POST  /bills : Create a new bills.
     *
     * @param bills the bills to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bills, or with status 400 (Bad Request) if the bills has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bills")
    @Timed
    public ResponseEntity<Bills> createBills(@RequestBody Bills bills) throws URISyntaxException {
        log.debug("REST request to save Bills : {}", bills);
        if (bills.getId() != null) {
            throw new BadRequestAlertException("A new bills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bills result = billsRepository.save(bills);
        return ResponseEntity.created(new URI("/api/bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bills : Updates an existing bills.
     *
     * @param bills the bills to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bills,
     * or with status 400 (Bad Request) if the bills is not valid,
     * or with status 500 (Internal Server Error) if the bills couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bills")
    @Timed
    public ResponseEntity<Bills> updateBills(@RequestBody Bills bills) throws URISyntaxException {
        log.debug("REST request to update Bills : {}", bills);
        if (bills.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Bills result = billsRepository.save(bills);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bills.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bills : get all the bills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bills in body
     */
    @GetMapping("/bills")
    @Timed
    public ResponseEntity<List<Bills>> getAllBills(Pageable pageable) {
        log.debug("REST request to get a page of Bills");
        Page<Bills> page = billsRepository.findAll(pageable);
        page.forEach(p->{
            if(p.getBillFile()!=null){
                p.setBillFile("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+p.getBillFile());
            }
            if(p.getImageFile()!=null){
                p.setImageFile("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+p.getImageFile());
            }
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bills/:id : get the "id" bills.
     *
     * @param id the id of the bills to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bills, or with status 404 (Not Found)
     */
    @GetMapping("/bills/{id}")
    @Timed
    public ResponseEntity<Bills> getBills(@PathVariable Long id) {
        log.debug("REST request to get Bills : {}", id);
        Optional<Bills> bills = billsRepository.findById(id);
        if(bills.get().getBillFile()!=null){
            bills.get().setBillFile("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+bills.get().getBillFile());
        }

        if(bills.get().getImageFile()!=null){
            bills.get().setImageFile("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+bills.get().getImageFile());
        }
        return ResponseUtil.wrapOrNotFound(bills);
    }
    /**
     * GET  /bills/:id : get the "id" bills.
     *
     * @param idBill the id of the bills to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bills, or with status 404 (Not Found)
     */
    @PostMapping("/bills/activate")
    @Timed
    public ResponseEntity<Bills> activateBills(@RequestParam("billId") String idBill,
                                               @RequestParam("accepted") String accepted ) {
        log.debug("REST request to get Bills : {}", idBill);
        Optional<Bills> bills = billsRepository.findById(Long.parseLong(idBill));
        Bills billsToActivate = bills.get();
        //Get the user who is doing the action
        System.out.println(billsToActivate.toString());
        String username = SecurityUtils.getCurrentUserLogin().orElse(null);
        Optional<User> user = userRepository.findOneByLogin(username);
        if(user.isPresent()){
            billsToActivate.setVerifierUser(user.get());
        }else{
            billsToActivate.setVerifierUser(null);
        }
        //Notifice him
        Notifications notifications = new Notifications();
        notifications.setChecked(false);
        notifications.setSenderUser(user.get());
        notifications.setReceiverUser(billsToActivate.getOwnerUser());
        notifications.setUpdateAt(Instant.now());
        notifications.setCreatedAt(Instant.now());
        System.out.print("Accepted value is ="+accepted+"; billId= "+idBill+"                          \n ");

        if(accepted.equals("1")){
            //put not rejected generate the pdf file and enabled the bill
            billsToActivate.setNotRejected(true);
            billsToActivate.setEnabled(true);
            //generate the bill
            //get seting
            String uniqID = UUID.randomUUID().toString();
            Setting setting= settingRepository.findAll().get(0);
            InvoiceGenerator invoiceGenerator = new InvoiceGenerator(billsToActivate,setting);
            String filename = billsToActivate.getVerifierMetricBill().getMetersUser().getLogin()+uniqID+"";
            //generate the pdf file
            invoiceGenerator.createPDF(filename);

            //put in s3 bucket
            File tmpInvoice =  new File("/tmp/"+filename+".pdf");
            s3Services.uploadFileFromServer(filename+".pdf", tmpInvoice);
            //update bill
            billsToActivate.setBillFile(filename+".pdf");
            //Delete the temp bill file
            tmpInvoice.delete();

            notifications.setMessage("Your submission is valid. Please checkout your bill and pay to any store.");



        }else{
            //rejected
            notifications.setMessage("Your submission is rejected because of many wrong information. Please contact our agence for more information.");
            billsToActivate.setBillFile(null);
            billsToActivate.setEnabled(false);
            billsToActivate.setNotRejected(false);

        }
        billsRepository.save(billsToActivate);
        notificationsRepository.save(notifications);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, billsToActivate.getId().toString()))
            .body(billsToActivate);
    }

    /**
     * DELETE  /bills/:id : delete the "id" bills.
     *
     * @param id the id of the bills to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bills/{id}")
    @Timed
    public ResponseEntity<Void> deleteBills(@PathVariable Long id) {
        log.debug("REST request to delete Bills : {}", id);

        billsRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /bills : Create a new bills.
     * @return the ResponseEntity with status 201 (Created) and with body the new bills, or with status 400 (Bad Request) if the bills has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/billsends")
    @Timed
    public ResponseEntity<Bills> uploadCapture(@RequestParam("file") MultipartFile file) throws URISyntaxException, IOException,
        NotFoundException, ImageReadException {

        if(!HelperRead.checkIfImage(file)){
            throw new BadRequestAlertException("Please upload an image file", ENTITY_NAME, "meterdisable");
        }

        //Get the active meter of the current login. if no meter send
        String username = SecurityUtils.getCurrentUserLogin().orElse(null);
        if(username==null){
            throw new BadRequestAlertException("Please renew your token by login", ENTITY_NAME, "username");
        }
        Optional<AssignMeters> assignMeters = assignMetersRepository.findOneByMetersUserLogin(username);
        if(!assignMeters.isPresent()){
            throw new BadRequestAlertException("You don't have a meter to do this action", ENTITY_NAME, "metermisses");
        }
        if(!assignMeters.get().isEnabled()){
            throw new BadRequestAlertException("Your meter has been disabled, please contact clean energy", ENTITY_NAME, "meterdisable");
        }

//Extension
        String extension = GetExtension.getValue(file.getOriginalFilename(), 1);

        //Upload to the tmp folder
        fileStorage.store(file);
        String path = "/tmp/"+file.getOriginalFilename();

        String readLine = HelperRead.checkQrCode(path);
        //Split readline with semicolon separator
        String readResult[] = HelperRead.getSplitString(readLine,";");
        if(readResult.length<=0){
            throw new BadRequestAlertException("Impossible to read your image", ENTITY_NAME, "unreadableimage");
        }
        Meters meters = assignMeters.get().getHistoryMeterUser();
        System.out.println("IDMETERS ="+meters.getId()+" and dr id="+readResult[0]);
//        System.out.println("address ="+meters.getAddressMeters()+" and dr add="+readResult[1]);
//        System.out.println("longitude ="+meters.getLongitude()+" and dr long="+readResult[3]);
//        System.out.println("latitde ="+meters.getLatituude()+" and dr lat="+readResult[2]);
        //the assign meters to the user

        if(meters.getId()!= Integer.parseInt(readResult[0])){
            throw new BadRequestAlertException("The informations on your QRCode is not good", ENTITY_NAME, "qrcodeinvalide");
        }
        File filePath = new File(path);
        if ((Sanselan.getMetadata(filePath) != null)
            || (Sanselan.getMetadata(filePath) instanceof IImageMetadata)) {
            final IImageMetadata metadata = (IImageMetadata) Sanselan.getMetadata(filePath);
            //Check if we can convert it to JpegImageMetadata
            if (metadata instanceof JpegImageMetadata) {
                final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                //Inside the MetaData, the GPS inforrmation is saved as EXIF data. Check if it exists.
                if (jpegMetadata.getExif() != null) {
                    final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
                    if (null != exifMetadata.getGPS()) {
                        final TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                        if (null != gpsInfo) {
                            //Finally, we get to the GPS data.
                            //final String gpsDescription = gpsInfo.toString();
                            final double longitude = gpsInfo.getLongitudeAsDegreesEast();
                            final double latitude = gpsInfo.getLatitudeAsDegreesNorth();
                            System.out.println("Longitude: " + longitude);
                            System.out.println("Latitude: " + latitude);

                            System.out.print("File located in: "+path+"       ");
                            String message ="Longitude: "+longitude+";Latitude:"+latitude+";ocr Value:";
                            String valeur = "";

                            try {
                                valeur = HelperRead.crackImage(path);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if(valeur!=null){
                                System.out.print("OCR VALUE IS = "+valeur);
                                message = message+valeur;
                            }else{
                                throw new BadRequestAlertException("No number in your file", ENTITY_NAME, "qrcodeinvalide");
                            }

                            int readingValue = Integer.parseInt(valeur);

//                            Save a new bills records
                            Bills bills = new Bills();
                            bills.setPaid(false);
                            bills.setEnabled(false);
                            bills.setNotRejected(true);
                            String user = SecurityUtils.getCurrentUserLogin().orElse(null);
                            Optional<User> userName = userRepository.findOneByLogin(user);
                            if(userName.isPresent()){
                                bills.setOwnerUser(userName.get());
                            }else{
                                bills.setOwnerUser(null);
                            }
                            String uniqueID = UUID.randomUUID().toString();
                            bills.setBillCode(uniqueID);

                            bills.setUploadedContent(message);
                            bills.setVerifierUser(null);
                            bills.setDeadline(HelperRead.deadlineMonth(1).toInstant());
                            BillSetting billSetting = billSettingRepository.findAll().get(0);
                            bills.setBillSettingApp(billSetting);
                            bills.setDateModified(Instant.now());
                            bills.setDateCreated(Instant.now());

                            //get my bill assignment
                            bills.setVerifierMetricBill(assignMeters.get());

                            //Get the reading of the current user
                            List<Bills> lastReadList = billsRepository.findByOwnerUserIsCurrentUser();
                            Bills lastRead = lastReadList.get(lastReadList.size() - 1);

                            bills.setLastMonthReading(lastRead.getCurrentMonthReading());
                            bills.setCurrentMonthReading(readingValue);
                            double amount = 0.0;
                            int readingN = readingValue-lastRead.getCurrentMonthReading();
                            amount =readingN*billSetting.getPricePerKW();
                            bills.setAmount(amount);

                            //billsRepository.save(bills);

                            String nameUpload = user+"UploadFile"+uniqueID+"."+extension;
                            //Upload to S3
                            File toBeDeleted = new File(path);
                            s3Services.uploadFileFromServer(nameUpload,toBeDeleted);
                            toBeDeleted.delete();
                            bills.setImageFile(nameUpload);
                            //System.out.println(bills.toString());

                            billsRepository.save(bills);




                            return ResponseEntity.created(new URI("/api/bills/" + bills.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, bills.getId().toString()))
                                .body(bills);

                            //Read with ocr

                        }
                    }else{
                        throw new BadRequestAlertException("No GPS data!", ENTITY_NAME, "qrcodeinvalide");
                    }
                }else{
                    throw new BadRequestAlertException("No EXIF data!", ENTITY_NAME, "qrcodeinvalide");
                }

            }else{
                throw new BadRequestAlertException("The format of the image file you uploaded "
                    + "does not contain metadata!", ENTITY_NAME, "qrcodeinvalide");
            }

        }else {
            throw new BadRequestAlertException("No meta Data in your image", ENTITY_NAME, "qrcodeinvalide");
        }

        //Check the amount

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUploadAlert(ENTITY_NAME, "ok")).build();
    }


    /**
     * GET  /my-bills : get all the bills.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bills in body
     */
    @GetMapping("/my-bills")
    @Timed
    public ResponseEntity<List<Bills>> getAllMyBills() {
        String username = SecurityUtils.getCurrentUserLogin().orElse(null);
        log.debug("REST request to get Preferences : {}", username);
        List<Bills> bills = billsRepository.findAllByOwnerUser_Login(username);
        bills.forEach(bill ->{
            bill.setBillFile("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+bill.getBillFile());
            bill.setImageFile("https://s3-"+s3Config.getRegion()+".amazonaws.com/"+s3Config.getBucketNameWebill()+"/"+bill.getImageFile());
        } );
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(bills, headers, HttpStatus.OK);
    }


}
