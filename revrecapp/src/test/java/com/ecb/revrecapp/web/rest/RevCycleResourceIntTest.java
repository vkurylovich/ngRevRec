package com.ecb.revrecapp.web.rest;

import com.ecb.revrecapp.RevrecappApp;

import com.ecb.revrecapp.domain.RevCycle;
import com.ecb.revrecapp.repository.RevCycleRepository;
import com.ecb.revrecapp.service.RevCycleService;
import com.ecb.revrecapp.service.dto.RevCycleDTO;
import com.ecb.revrecapp.service.mapper.RevCycleMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RevCycleResource REST controller.
 *
 * @see RevCycleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RevrecappApp.class)
public class RevCycleResourceIntTest {

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    @Inject
    private RevCycleRepository revCycleRepository;

    @Inject
    private RevCycleMapper revCycleMapper;

    @Inject
    private RevCycleService revCycleService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRevCycleMockMvc;

    private RevCycle revCycle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RevCycleResource revCycleResource = new RevCycleResource();
        ReflectionTestUtils.setField(revCycleResource, "revCycleService", revCycleService);
        this.restRevCycleMockMvc = MockMvcBuilders.standaloneSetup(revCycleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RevCycle createEntity(EntityManager em) {
        RevCycle revCycle = new RevCycle()
                .month(DEFAULT_MONTH);
        return revCycle;
    }

    @Before
    public void initTest() {
        revCycle = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevCycle() throws Exception {
        int databaseSizeBeforeCreate = revCycleRepository.findAll().size();

        // Create the RevCycle
        RevCycleDTO revCycleDTO = revCycleMapper.revCycleToRevCycleDTO(revCycle);

        restRevCycleMockMvc.perform(post("/api/rev-cycles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revCycleDTO)))
            .andExpect(status().isCreated());

        // Validate the RevCycle in the database
        List<RevCycle> revCycles = revCycleRepository.findAll();
        assertThat(revCycles).hasSize(databaseSizeBeforeCreate + 1);
        RevCycle testRevCycle = revCycles.get(revCycles.size() - 1);
        assertThat(testRevCycle.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = revCycleRepository.findAll().size();
        // set the field null
        revCycle.setMonth(null);

        // Create the RevCycle, which fails.
        RevCycleDTO revCycleDTO = revCycleMapper.revCycleToRevCycleDTO(revCycle);

        restRevCycleMockMvc.perform(post("/api/rev-cycles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revCycleDTO)))
            .andExpect(status().isBadRequest());

        List<RevCycle> revCycles = revCycleRepository.findAll();
        assertThat(revCycles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRevCycles() throws Exception {
        // Initialize the database
        revCycleRepository.saveAndFlush(revCycle);

        // Get all the revCycles
        restRevCycleMockMvc.perform(get("/api/rev-cycles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revCycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    @Test
    @Transactional
    public void getRevCycle() throws Exception {
        // Initialize the database
        revCycleRepository.saveAndFlush(revCycle);

        // Get the revCycle
        restRevCycleMockMvc.perform(get("/api/rev-cycles/{id}", revCycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(revCycle.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    public void getNonExistingRevCycle() throws Exception {
        // Get the revCycle
        restRevCycleMockMvc.perform(get("/api/rev-cycles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevCycle() throws Exception {
        // Initialize the database
        revCycleRepository.saveAndFlush(revCycle);
        int databaseSizeBeforeUpdate = revCycleRepository.findAll().size();

        // Update the revCycle
        RevCycle updatedRevCycle = revCycleRepository.findOne(revCycle.getId());
        updatedRevCycle
                .month(UPDATED_MONTH);
        RevCycleDTO revCycleDTO = revCycleMapper.revCycleToRevCycleDTO(updatedRevCycle);

        restRevCycleMockMvc.perform(put("/api/rev-cycles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revCycleDTO)))
            .andExpect(status().isOk());

        // Validate the RevCycle in the database
        List<RevCycle> revCycles = revCycleRepository.findAll();
        assertThat(revCycles).hasSize(databaseSizeBeforeUpdate);
        RevCycle testRevCycle = revCycles.get(revCycles.size() - 1);
        assertThat(testRevCycle.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void deleteRevCycle() throws Exception {
        // Initialize the database
        revCycleRepository.saveAndFlush(revCycle);
        int databaseSizeBeforeDelete = revCycleRepository.findAll().size();

        // Get the revCycle
        restRevCycleMockMvc.perform(delete("/api/rev-cycles/{id}", revCycle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RevCycle> revCycles = revCycleRepository.findAll();
        assertThat(revCycles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
