package com.webill.web.rest;

import com.webill.WebillApp;

import com.webill.domain.Notifications;
import com.webill.repository.NotificationsRepository;
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
 * Test class for the NotificationsResource REST controller.
 *
 * @see NotificationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebillApp.class)
public class NotificationsResourceIntTest {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CHECKED = false;
    private static final Boolean UPDATED_CHECKED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificationsMockMvc;

    private Notifications notifications;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationsResource notificationsResource = new NotificationsResource(notificationsRepository);
        this.restNotificationsMockMvc = MockMvcBuilders.standaloneSetup(notificationsResource)
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
    public static Notifications createEntity(EntityManager em) {
        Notifications notifications = new Notifications()
            .message(DEFAULT_MESSAGE)
            .checked(DEFAULT_CHECKED)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return notifications;
    }

    @Before
    public void initTest() {
        notifications = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotifications() throws Exception {
        int databaseSizeBeforeCreate = notificationsRepository.findAll().size();

        // Create the Notifications
        restNotificationsMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notifications)))
            .andExpect(status().isCreated());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeCreate + 1);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotifications.isChecked()).isEqualTo(DEFAULT_CHECKED);
        assertThat(testNotifications.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testNotifications.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createNotificationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationsRepository.findAll().size();

        // Create the Notifications with an existing ID
        notifications.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationsMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notifications)))
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList
        restNotificationsMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notifications.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].checked").value(hasItem(DEFAULT_CHECKED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get the notifications
        restNotificationsMockMvc.perform(get("/api/notifications/{id}", notifications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notifications.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.checked").value(DEFAULT_CHECKED.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotifications() throws Exception {
        // Get the notifications
        restNotificationsMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();

        // Update the notifications
        Notifications updatedNotifications = notificationsRepository.findById(notifications.getId()).get();
        // Disconnect from session so that the updates on updatedNotifications are not directly saved in db
        em.detach(updatedNotifications);
        updatedNotifications
            .message(UPDATED_MESSAGE)
            .checked(UPDATED_CHECKED)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restNotificationsMockMvc.perform(put("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotifications)))
            .andExpect(status().isOk());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotifications.isChecked()).isEqualTo(UPDATED_CHECKED);
        assertThat(testNotifications.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testNotifications.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();

        // Create the Notifications

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationsMockMvc.perform(put("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notifications)))
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeDelete = notificationsRepository.findAll().size();

        // Get the notifications
        restNotificationsMockMvc.perform(delete("/api/notifications/{id}", notifications.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notifications.class);
        Notifications notifications1 = new Notifications();
        notifications1.setId(1L);
        Notifications notifications2 = new Notifications();
        notifications2.setId(notifications1.getId());
        assertThat(notifications1).isEqualTo(notifications2);
        notifications2.setId(2L);
        assertThat(notifications1).isNotEqualTo(notifications2);
        notifications1.setId(null);
        assertThat(notifications1).isNotEqualTo(notifications2);
    }
}
