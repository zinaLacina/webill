package com.webill.web.rest;

import com.webill.WebillApp;

import com.webill.domain.BillSetting;
import com.webill.repository.BillSettingRepository;
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

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.webill.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.webill.domain.enumeration.TypeBill;
/**
 * Test class for the BillSettingResource REST controller.
 *
 * @see BillSettingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebillApp.class)
public class BillSettingResourceIntTest {

    private static final TypeBill DEFAULT_TYPE_BILL = TypeBill.COMPANY;
    private static final TypeBill UPDATED_TYPE_BILL = TypeBill.LOYALCUSTOMER;

    private static final Double DEFAULT_PRICE_PER_KW = 1D;
    private static final Double UPDATED_PRICE_PER_KW = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;

    private static final Double DEFAULT_PROCESSING = 1D;
    private static final Double UPDATED_PROCESSING = 2D;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BillSettingRepository billSettingRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBillSettingMockMvc;

    private BillSetting billSetting;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BillSettingResource billSettingResource = new BillSettingResource(billSettingRepository);
        this.restBillSettingMockMvc = MockMvcBuilders.standaloneSetup(billSettingResource)
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
    public static BillSetting createEntity(EntityManager em) {
        BillSetting billSetting = new BillSetting()
            .typeBill(DEFAULT_TYPE_BILL)
            .pricePerKW(DEFAULT_PRICE_PER_KW)
            .discount(DEFAULT_DISCOUNT)
            .tax(DEFAULT_TAX)
            .processing(DEFAULT_PROCESSING)
            .enabled(DEFAULT_ENABLED)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return billSetting;
    }

    @Before
    public void initTest() {
        billSetting = createEntity(em);
    }

    @Test
    @Transactional
    public void createBillSetting() throws Exception {
        int databaseSizeBeforeCreate = billSettingRepository.findAll().size();

        // Create the BillSetting
        restBillSettingMockMvc.perform(post("/api/bill-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billSetting)))
            .andExpect(status().isCreated());

        // Validate the BillSetting in the database
        List<BillSetting> billSettingList = billSettingRepository.findAll();
        assertThat(billSettingList).hasSize(databaseSizeBeforeCreate + 1);
        BillSetting testBillSetting = billSettingList.get(billSettingList.size() - 1);
        assertThat(testBillSetting.getTypeBill()).isEqualTo(DEFAULT_TYPE_BILL);
        assertThat(testBillSetting.getPricePerKW()).isEqualTo(DEFAULT_PRICE_PER_KW);
        assertThat(testBillSetting.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testBillSetting.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testBillSetting.getProcessing()).isEqualTo(DEFAULT_PROCESSING);
        assertThat(testBillSetting.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testBillSetting.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testBillSetting.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createBillSettingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = billSettingRepository.findAll().size();

        // Create the BillSetting with an existing ID
        billSetting.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillSettingMockMvc.perform(post("/api/bill-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billSetting)))
            .andExpect(status().isBadRequest());

        // Validate the BillSetting in the database
        List<BillSetting> billSettingList = billSettingRepository.findAll();
        assertThat(billSettingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBillSettings() throws Exception {
        // Initialize the database
        billSettingRepository.saveAndFlush(billSetting);

        // Get all the billSettingList
        restBillSettingMockMvc.perform(get("/api/bill-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeBill").value(hasItem(DEFAULT_TYPE_BILL.toString())))
            .andExpect(jsonPath("$.[*].pricePerKW").value(hasItem(DEFAULT_PRICE_PER_KW.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].processing").value(hasItem(DEFAULT_PROCESSING.doubleValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getBillSetting() throws Exception {
        // Initialize the database
        billSettingRepository.saveAndFlush(billSetting);

        // Get the billSetting
        restBillSettingMockMvc.perform(get("/api/bill-settings/{id}", billSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(billSetting.getId().intValue()))
            .andExpect(jsonPath("$.typeBill").value(DEFAULT_TYPE_BILL.toString()))
            .andExpect(jsonPath("$.pricePerKW").value(DEFAULT_PRICE_PER_KW.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.processing").value(DEFAULT_PROCESSING.doubleValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBillSetting() throws Exception {
        // Get the billSetting
        restBillSettingMockMvc.perform(get("/api/bill-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillSetting() throws Exception {
        // Initialize the database
        billSettingRepository.saveAndFlush(billSetting);

        int databaseSizeBeforeUpdate = billSettingRepository.findAll().size();

        // Update the billSetting
        BillSetting updatedBillSetting = billSettingRepository.findById(billSetting.getId()).get();
        // Disconnect from session so that the updates on updatedBillSetting are not directly saved in db
        em.detach(updatedBillSetting);
        updatedBillSetting
            .typeBill(UPDATED_TYPE_BILL)
            .pricePerKW(UPDATED_PRICE_PER_KW)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .processing(UPDATED_PROCESSING)
            .enabled(UPDATED_ENABLED)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restBillSettingMockMvc.perform(put("/api/bill-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBillSetting)))
            .andExpect(status().isOk());

        // Validate the BillSetting in the database
        List<BillSetting> billSettingList = billSettingRepository.findAll();
        assertThat(billSettingList).hasSize(databaseSizeBeforeUpdate);
        BillSetting testBillSetting = billSettingList.get(billSettingList.size() - 1);
        assertThat(testBillSetting.getTypeBill()).isEqualTo(UPDATED_TYPE_BILL);
        assertThat(testBillSetting.getPricePerKW()).isEqualTo(UPDATED_PRICE_PER_KW);
        assertThat(testBillSetting.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testBillSetting.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testBillSetting.getProcessing()).isEqualTo(UPDATED_PROCESSING);
        assertThat(testBillSetting.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testBillSetting.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testBillSetting.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingBillSetting() throws Exception {
        int databaseSizeBeforeUpdate = billSettingRepository.findAll().size();

        // Create the BillSetting

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillSettingMockMvc.perform(put("/api/bill-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billSetting)))
            .andExpect(status().isBadRequest());

        // Validate the BillSetting in the database
        List<BillSetting> billSettingList = billSettingRepository.findAll();
        assertThat(billSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBillSetting() throws Exception {
        // Initialize the database
        billSettingRepository.saveAndFlush(billSetting);

        int databaseSizeBeforeDelete = billSettingRepository.findAll().size();

        // Get the billSetting
        restBillSettingMockMvc.perform(delete("/api/bill-settings/{id}", billSetting.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BillSetting> billSettingList = billSettingRepository.findAll();
        assertThat(billSettingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillSetting.class);
        BillSetting billSetting1 = new BillSetting();
        billSetting1.setId(1L);
        BillSetting billSetting2 = new BillSetting();
        billSetting2.setId(billSetting1.getId());
        assertThat(billSetting1).isEqualTo(billSetting2);
        billSetting2.setId(2L);
        assertThat(billSetting1).isNotEqualTo(billSetting2);
        billSetting1.setId(null);
        assertThat(billSetting1).isNotEqualTo(billSetting2);
    }
}
