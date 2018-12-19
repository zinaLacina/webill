package com.webill.web.rest;

import com.webill.WebillApp;

import com.webill.domain.Meters;
import com.webill.repository.MetersRepository;
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
 * Test class for the MetersResource REST controller.
 *
 * @see MetersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebillApp.class)
public class MetersResourceIntTest {

    private static final Double DEFAULT_LATITUUDE = 1D;
    private static final Double UPDATED_LATITUUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final byte[] DEFAULT_QR_CODE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_QR_CODE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_QR_CODE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_QR_CODE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ADDRESS_METERS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_METERS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MetersRepository metersRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetersMockMvc;

    private Meters meters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetersResource metersResource = new MetersResource(metersRepository);
        this.restMetersMockMvc = MockMvcBuilders.standaloneSetup(metersResource)
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
    public static Meters createEntity(EntityManager em) {
        Meters meters = new Meters()
            .latituude(DEFAULT_LATITUUDE)
            .longitude(DEFAULT_LONGITUDE)
            .qrCode(DEFAULT_QR_CODE)
            .qrCodeContentType(DEFAULT_QR_CODE_CONTENT_TYPE)
            .addressMeters(DEFAULT_ADDRESS_METERS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return meters;
    }

    @Before
    public void initTest() {
        meters = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeters() throws Exception {
        int databaseSizeBeforeCreate = metersRepository.findAll().size();

        // Create the Meters
        restMetersMockMvc.perform(post("/api/meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meters)))
            .andExpect(status().isCreated());

        // Validate the Meters in the database
        List<Meters> metersList = metersRepository.findAll();
        assertThat(metersList).hasSize(databaseSizeBeforeCreate + 1);
        Meters testMeters = metersList.get(metersList.size() - 1);
        assertThat(testMeters.getLatituude()).isEqualTo(DEFAULT_LATITUUDE);
        assertThat(testMeters.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testMeters.getQrCode()).isEqualTo(DEFAULT_QR_CODE);
        assertThat(testMeters.getQrCodeContentType()).isEqualTo(DEFAULT_QR_CODE_CONTENT_TYPE);
        assertThat(testMeters.getAddressMeters()).isEqualTo(DEFAULT_ADDRESS_METERS);
        assertThat(testMeters.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testMeters.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createMetersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metersRepository.findAll().size();

        // Create the Meters with an existing ID
        meters.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetersMockMvc.perform(post("/api/meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meters)))
            .andExpect(status().isBadRequest());

        // Validate the Meters in the database
        List<Meters> metersList = metersRepository.findAll();
        assertThat(metersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMeters() throws Exception {
        // Initialize the database
        metersRepository.saveAndFlush(meters);

        // Get all the metersList
        restMetersMockMvc.perform(get("/api/meters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meters.getId().intValue())))
            .andExpect(jsonPath("$.[*].latituude").value(hasItem(DEFAULT_LATITUUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].qrCodeContentType").value(hasItem(DEFAULT_QR_CODE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(Base64Utils.encodeToString(DEFAULT_QR_CODE))))
            .andExpect(jsonPath("$.[*].addressMeters").value(hasItem(DEFAULT_ADDRESS_METERS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getMeters() throws Exception {
        // Initialize the database
        metersRepository.saveAndFlush(meters);

        // Get the meters
        restMetersMockMvc.perform(get("/api/meters/{id}", meters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meters.getId().intValue()))
            .andExpect(jsonPath("$.latituude").value(DEFAULT_LATITUUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.qrCodeContentType").value(DEFAULT_QR_CODE_CONTENT_TYPE))
            .andExpect(jsonPath("$.qrCode").value(Base64Utils.encodeToString(DEFAULT_QR_CODE)))
            .andExpect(jsonPath("$.addressMeters").value(DEFAULT_ADDRESS_METERS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeters() throws Exception {
        // Get the meters
        restMetersMockMvc.perform(get("/api/meters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeters() throws Exception {
        // Initialize the database
        metersRepository.saveAndFlush(meters);

        int databaseSizeBeforeUpdate = metersRepository.findAll().size();

        // Update the meters
        Meters updatedMeters = metersRepository.findById(meters.getId()).get();
        // Disconnect from session so that the updates on updatedMeters are not directly saved in db
        em.detach(updatedMeters);
        updatedMeters
            .latituude(UPDATED_LATITUUDE)
            .longitude(UPDATED_LONGITUDE)
            .qrCode(UPDATED_QR_CODE)
            .qrCodeContentType(UPDATED_QR_CODE_CONTENT_TYPE)
            .addressMeters(UPDATED_ADDRESS_METERS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restMetersMockMvc.perform(put("/api/meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeters)))
            .andExpect(status().isOk());

        // Validate the Meters in the database
        List<Meters> metersList = metersRepository.findAll();
        assertThat(metersList).hasSize(databaseSizeBeforeUpdate);
        Meters testMeters = metersList.get(metersList.size() - 1);
        assertThat(testMeters.getLatituude()).isEqualTo(UPDATED_LATITUUDE);
        assertThat(testMeters.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testMeters.getQrCode()).isEqualTo(UPDATED_QR_CODE);
        assertThat(testMeters.getQrCodeContentType()).isEqualTo(UPDATED_QR_CODE_CONTENT_TYPE);
        assertThat(testMeters.getAddressMeters()).isEqualTo(UPDATED_ADDRESS_METERS);
        assertThat(testMeters.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testMeters.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingMeters() throws Exception {
        int databaseSizeBeforeUpdate = metersRepository.findAll().size();

        // Create the Meters

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetersMockMvc.perform(put("/api/meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meters)))
            .andExpect(status().isBadRequest());

        // Validate the Meters in the database
        List<Meters> metersList = metersRepository.findAll();
        assertThat(metersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMeters() throws Exception {
        // Initialize the database
        metersRepository.saveAndFlush(meters);

        int databaseSizeBeforeDelete = metersRepository.findAll().size();

        // Get the meters
        restMetersMockMvc.perform(delete("/api/meters/{id}", meters.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Meters> metersList = metersRepository.findAll();
        assertThat(metersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Meters.class);
        Meters meters1 = new Meters();
        meters1.setId(1L);
        Meters meters2 = new Meters();
        meters2.setId(meters1.getId());
        assertThat(meters1).isEqualTo(meters2);
        meters2.setId(2L);
        assertThat(meters1).isNotEqualTo(meters2);
        meters1.setId(null);
        assertThat(meters1).isNotEqualTo(meters2);
    }
}
