package com.webill.web.rest;

import com.webill.WebillApp;

import com.webill.domain.Bills;
import com.webill.repository.BillsRepository;
import com.webill.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.webill.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BillsResource REST controller.
 *
 * @see BillsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebillApp.class)
public class BillsResourceIntTest {

    private static final Integer DEFAULT_LAST_MONTH_READING = 1;
    private static final Integer UPDATED_LAST_MONTH_READING = 2;

    private static final Integer DEFAULT_CURRENT_MONTH_READING = 1;
    private static final Integer UPDATED_CURRENT_MONTH_READING = 2;

    private static final Instant DEFAULT_DEADLINE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEADLINE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_BILL_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BILL_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BILL_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BILL_FILE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_FILE_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BillsRepository billsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBillsMockMvc;

    private Bills bills;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BillsResource billsResource = new BillsResource(billsRepository);
        this.restBillsMockMvc = MockMvcBuilders.standaloneSetup(billsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bills createEntity(EntityManager em) {
        Bills bills = new Bills()
            .lastMonthReading(DEFAULT_LAST_MONTH_READING)
            .currentMonthReading(DEFAULT_CURRENT_MONTH_READING)
            .deadline(DEFAULT_DEADLINE)
            .billFile(DEFAULT_BILL_FILE)
            .billFileContentType(DEFAULT_BILL_FILE_CONTENT_TYPE)
            .imageFile(DEFAULT_IMAGE_FILE)
            .imageFileContentType(DEFAULT_IMAGE_FILE_CONTENT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .enabled(DEFAULT_ENABLED)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return bills;
    }

    @Before
    public void initTest() {
        bills = createEntity(em);
    }

    @Test
    @Transactional
    public void createBills() throws Exception {
        int databaseSizeBeforeCreate = billsRepository.findAll().size();

        // Create the Bills
        restBillsMockMvc.perform(post("/api/bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bills)))
            .andExpect(status().isCreated());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeCreate + 1);
        Bills testBills = billsList.get(billsList.size() - 1);
        assertThat(testBills.getLastMonthReading()).isEqualTo(DEFAULT_LAST_MONTH_READING);
        assertThat(testBills.getCurrentMonthReading()).isEqualTo(DEFAULT_CURRENT_MONTH_READING);
        assertThat(testBills.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
        assertThat(testBills.getBillFile()).isEqualTo(DEFAULT_BILL_FILE);
        assertThat(testBills.getBillFileContentType()).isEqualTo(DEFAULT_BILL_FILE_CONTENT_TYPE);
        assertThat(testBills.getImageFile()).isEqualTo(DEFAULT_IMAGE_FILE);
        assertThat(testBills.getImageFileContentType()).isEqualTo(DEFAULT_IMAGE_FILE_CONTENT_TYPE);
        assertThat(testBills.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testBills.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testBills.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testBills.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createBillsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = billsRepository.findAll().size();

        // Create the Bills with an existing ID
        bills.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillsMockMvc.perform(post("/api/bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bills)))
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBills() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get all the billsList
        restBillsMockMvc.perform(get("/api/bills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bills.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastMonthReading").value(hasItem(DEFAULT_LAST_MONTH_READING)))
            .andExpect(jsonPath("$.[*].currentMonthReading").value(hasItem(DEFAULT_CURRENT_MONTH_READING)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].billFileContentType").value(hasItem(DEFAULT_BILL_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].billFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_BILL_FILE))))
            .andExpect(jsonPath("$.[*].imageFileContentType").value(hasItem(DEFAULT_IMAGE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_FILE))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getBills() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        // Get the bills
        restBillsMockMvc.perform(get("/api/bills/{id}", bills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bills.getId().intValue()))
            .andExpect(jsonPath("$.lastMonthReading").value(DEFAULT_LAST_MONTH_READING))
            .andExpect(jsonPath("$.currentMonthReading").value(DEFAULT_CURRENT_MONTH_READING))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()))
            .andExpect(jsonPath("$.billFileContentType").value(DEFAULT_BILL_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.billFile").value(Base64Utils.encodeToString(DEFAULT_BILL_FILE)))
            .andExpect(jsonPath("$.imageFileContentType").value(DEFAULT_IMAGE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageFile").value(Base64Utils.encodeToString(DEFAULT_IMAGE_FILE)))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBills() throws Exception {
        // Get the bills
        restBillsMockMvc.perform(get("/api/bills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBills() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        int databaseSizeBeforeUpdate = billsRepository.findAll().size();

        // Update the bills
        Bills updatedBills = billsRepository.findById(bills.getId()).get();
        // Disconnect from session so that the updates on updatedBills are not directly saved in db
        em.detach(updatedBills);
        updatedBills
            .lastMonthReading(UPDATED_LAST_MONTH_READING)
            .currentMonthReading(UPDATED_CURRENT_MONTH_READING)
            .deadline(UPDATED_DEADLINE)
            .billFile(UPDATED_BILL_FILE)
            .billFileContentType(UPDATED_BILL_FILE_CONTENT_TYPE)
            .imageFile(UPDATED_IMAGE_FILE)
            .imageFileContentType(UPDATED_IMAGE_FILE_CONTENT_TYPE)
            .amount(UPDATED_AMOUNT)
            .enabled(UPDATED_ENABLED)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restBillsMockMvc.perform(put("/api/bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBills)))
            .andExpect(status().isOk());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
        Bills testBills = billsList.get(billsList.size() - 1);
        assertThat(testBills.getLastMonthReading()).isEqualTo(UPDATED_LAST_MONTH_READING);
        assertThat(testBills.getCurrentMonthReading()).isEqualTo(UPDATED_CURRENT_MONTH_READING);
        assertThat(testBills.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testBills.getBillFile()).isEqualTo(UPDATED_BILL_FILE);
        assertThat(testBills.getBillFileContentType()).isEqualTo(UPDATED_BILL_FILE_CONTENT_TYPE);
        assertThat(testBills.getImageFile()).isEqualTo(UPDATED_IMAGE_FILE);
        assertThat(testBills.getImageFileContentType()).isEqualTo(UPDATED_IMAGE_FILE_CONTENT_TYPE);
        assertThat(testBills.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testBills.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testBills.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testBills.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingBills() throws Exception {
        int databaseSizeBeforeUpdate = billsRepository.findAll().size();

        // Create the Bills

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillsMockMvc.perform(put("/api/bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bills)))
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBills() throws Exception {
        // Initialize the database
        billsRepository.saveAndFlush(bills);

        int databaseSizeBeforeDelete = billsRepository.findAll().size();

        // Get the bills
        restBillsMockMvc.perform(delete("/api/bills/{id}", bills.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Bills> billsList = billsRepository.findAll();
        assertThat(billsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bills.class);
        Bills bills1 = new Bills();
        bills1.setId(1L);
        Bills bills2 = new Bills();
        bills2.setId(bills1.getId());
        assertThat(bills1).isEqualTo(bills2);
        bills2.setId(2L);
        assertThat(bills1).isNotEqualTo(bills2);
        bills1.setId(null);
        assertThat(bills1).isNotEqualTo(bills2);
    }
}
