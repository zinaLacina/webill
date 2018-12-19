package com.webill.web.rest;

import com.webill.WebillApp;

import com.webill.domain.AssignMeters;
import com.webill.repository.AssignMetersRepository;
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

/**
 * Test class for the AssignMetersResource REST controller.
 *
 * @see AssignMetersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebillApp.class)
public class AssignMetersResourceIntTest {

    private static final Integer DEFAULT_START_MONTH = 1;
    private static final Integer UPDATED_START_MONTH = 2;

    private static final Integer DEFAULT_END_MONTH = 1;
    private static final Integer UPDATED_END_MONTH = 2;

    private static final Integer DEFAULT_START_DAY = 1;
    private static final Integer UPDATED_START_DAY = 2;

    private static final Integer DEFAULT_END_DAY = 1;
    private static final Integer UPDATED_END_DAY = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String DEFAULT_REASON_END = "AAAAAAAAAA";
    private static final String UPDATED_REASON_END = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AssignMetersRepository assignMetersRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAssignMetersMockMvc;

    private AssignMeters assignMeters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AssignMetersResource assignMetersResource = new AssignMetersResource(assignMetersRepository);
        this.restAssignMetersMockMvc = MockMvcBuilders.standaloneSetup(assignMetersResource)
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
    public static AssignMeters createEntity(EntityManager em) {
        AssignMeters assignMeters = new AssignMeters()
            .startMonth(DEFAULT_START_MONTH)
            .endMonth(DEFAULT_END_MONTH)
            .startDay(DEFAULT_START_DAY)
            .endDay(DEFAULT_END_DAY)
            .year(DEFAULT_YEAR)
            .reasonEnd(DEFAULT_REASON_END)
            .enabled(DEFAULT_ENABLED)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return assignMeters;
    }

    @Before
    public void initTest() {
        assignMeters = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssignMeters() throws Exception {
        int databaseSizeBeforeCreate = assignMetersRepository.findAll().size();

        // Create the AssignMeters
        restAssignMetersMockMvc.perform(post("/api/assign-meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignMeters)))
            .andExpect(status().isCreated());

        // Validate the AssignMeters in the database
        List<AssignMeters> assignMetersList = assignMetersRepository.findAll();
        assertThat(assignMetersList).hasSize(databaseSizeBeforeCreate + 1);
        AssignMeters testAssignMeters = assignMetersList.get(assignMetersList.size() - 1);
        assertThat(testAssignMeters.getStartMonth()).isEqualTo(DEFAULT_START_MONTH);
        assertThat(testAssignMeters.getEndMonth()).isEqualTo(DEFAULT_END_MONTH);
        assertThat(testAssignMeters.getStartDay()).isEqualTo(DEFAULT_START_DAY);
        assertThat(testAssignMeters.getEndDay()).isEqualTo(DEFAULT_END_DAY);
        assertThat(testAssignMeters.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testAssignMeters.getReasonEnd()).isEqualTo(DEFAULT_REASON_END);
        assertThat(testAssignMeters.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testAssignMeters.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAssignMeters.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createAssignMetersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assignMetersRepository.findAll().size();

        // Create the AssignMeters with an existing ID
        assignMeters.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssignMetersMockMvc.perform(post("/api/assign-meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignMeters)))
            .andExpect(status().isBadRequest());

        // Validate the AssignMeters in the database
        List<AssignMeters> assignMetersList = assignMetersRepository.findAll();
        assertThat(assignMetersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAssignMeters() throws Exception {
        // Initialize the database
        assignMetersRepository.saveAndFlush(assignMeters);

        // Get all the assignMetersList
        restAssignMetersMockMvc.perform(get("/api/assign-meters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignMeters.getId().intValue())))
            .andExpect(jsonPath("$.[*].startMonth").value(hasItem(DEFAULT_START_MONTH)))
            .andExpect(jsonPath("$.[*].endMonth").value(hasItem(DEFAULT_END_MONTH)))
            .andExpect(jsonPath("$.[*].startDay").value(hasItem(DEFAULT_START_DAY)))
            .andExpect(jsonPath("$.[*].endDay").value(hasItem(DEFAULT_END_DAY)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].reasonEnd").value(hasItem(DEFAULT_REASON_END.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getAssignMeters() throws Exception {
        // Initialize the database
        assignMetersRepository.saveAndFlush(assignMeters);

        // Get the assignMeters
        restAssignMetersMockMvc.perform(get("/api/assign-meters/{id}", assignMeters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assignMeters.getId().intValue()))
            .andExpect(jsonPath("$.startMonth").value(DEFAULT_START_MONTH))
            .andExpect(jsonPath("$.endMonth").value(DEFAULT_END_MONTH))
            .andExpect(jsonPath("$.startDay").value(DEFAULT_START_DAY))
            .andExpect(jsonPath("$.endDay").value(DEFAULT_END_DAY))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.reasonEnd").value(DEFAULT_REASON_END.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAssignMeters() throws Exception {
        // Get the assignMeters
        restAssignMetersMockMvc.perform(get("/api/assign-meters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssignMeters() throws Exception {
        // Initialize the database
        assignMetersRepository.saveAndFlush(assignMeters);

        int databaseSizeBeforeUpdate = assignMetersRepository.findAll().size();

        // Update the assignMeters
        AssignMeters updatedAssignMeters = assignMetersRepository.findById(assignMeters.getId()).get();
        // Disconnect from session so that the updates on updatedAssignMeters are not directly saved in db
        em.detach(updatedAssignMeters);
        updatedAssignMeters
            .startMonth(UPDATED_START_MONTH)
            .endMonth(UPDATED_END_MONTH)
            .startDay(UPDATED_START_DAY)
            .endDay(UPDATED_END_DAY)
            .year(UPDATED_YEAR)
            .reasonEnd(UPDATED_REASON_END)
            .enabled(UPDATED_ENABLED)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restAssignMetersMockMvc.perform(put("/api/assign-meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAssignMeters)))
            .andExpect(status().isOk());

        // Validate the AssignMeters in the database
        List<AssignMeters> assignMetersList = assignMetersRepository.findAll();
        assertThat(assignMetersList).hasSize(databaseSizeBeforeUpdate);
        AssignMeters testAssignMeters = assignMetersList.get(assignMetersList.size() - 1);
        assertThat(testAssignMeters.getStartMonth()).isEqualTo(UPDATED_START_MONTH);
        assertThat(testAssignMeters.getEndMonth()).isEqualTo(UPDATED_END_MONTH);
        assertThat(testAssignMeters.getStartDay()).isEqualTo(UPDATED_START_DAY);
        assertThat(testAssignMeters.getEndDay()).isEqualTo(UPDATED_END_DAY);
        assertThat(testAssignMeters.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testAssignMeters.getReasonEnd()).isEqualTo(UPDATED_REASON_END);
        assertThat(testAssignMeters.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testAssignMeters.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAssignMeters.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingAssignMeters() throws Exception {
        int databaseSizeBeforeUpdate = assignMetersRepository.findAll().size();

        // Create the AssignMeters

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignMetersMockMvc.perform(put("/api/assign-meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assignMeters)))
            .andExpect(status().isBadRequest());

        // Validate the AssignMeters in the database
        List<AssignMeters> assignMetersList = assignMetersRepository.findAll();
        assertThat(assignMetersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAssignMeters() throws Exception {
        // Initialize the database
        assignMetersRepository.saveAndFlush(assignMeters);

        int databaseSizeBeforeDelete = assignMetersRepository.findAll().size();

        // Get the assignMeters
        restAssignMetersMockMvc.perform(delete("/api/assign-meters/{id}", assignMeters.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AssignMeters> assignMetersList = assignMetersRepository.findAll();
        assertThat(assignMetersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignMeters.class);
        AssignMeters assignMeters1 = new AssignMeters();
        assignMeters1.setId(1L);
        AssignMeters assignMeters2 = new AssignMeters();
        assignMeters2.setId(assignMeters1.getId());
        assertThat(assignMeters1).isEqualTo(assignMeters2);
        assignMeters2.setId(2L);
        assertThat(assignMeters1).isNotEqualTo(assignMeters2);
        assignMeters1.setId(null);
        assertThat(assignMeters1).isNotEqualTo(assignMeters2);
    }
}
