package com.ecb.revrecapp.service;

import com.ecb.revrecapp.domain.RevCycle;
import com.ecb.revrecapp.repository.RevCycleRepository;
import com.ecb.revrecapp.service.dto.RevCycleDTO;
import com.ecb.revrecapp.service.mapper.RevCycleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing RevCycle.
 */
@Service
@Transactional
public class RevCycleService {

    private final Logger log = LoggerFactory.getLogger(RevCycleService.class);
    
    @Inject
    private RevCycleRepository revCycleRepository;

    @Inject
    private RevCycleMapper revCycleMapper;

    /**
     * Save a revCycle.
     *
     * @param revCycleDTO the entity to save
     * @return the persisted entity
     */
    public RevCycleDTO save(RevCycleDTO revCycleDTO) {
        log.debug("Request to save RevCycle : {}", revCycleDTO);
        RevCycle revCycle = revCycleMapper.revCycleDTOToRevCycle(revCycleDTO);
        revCycle = revCycleRepository.save(revCycle);
        RevCycleDTO result = revCycleMapper.revCycleToRevCycleDTO(revCycle);
        return result;
    }

    /**
     *  Get all the revCycles.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<RevCycleDTO> findAll() {
        log.debug("Request to get all RevCycles");
        List<RevCycleDTO> result = revCycleRepository.findAll().stream()
            .map(revCycleMapper::revCycleToRevCycleDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one revCycle by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RevCycleDTO findOne(Long id) {
        log.debug("Request to get RevCycle : {}", id);
        RevCycle revCycle = revCycleRepository.findOne(id);
        RevCycleDTO revCycleDTO = revCycleMapper.revCycleToRevCycleDTO(revCycle);
        return revCycleDTO;
    }

    /**
     *  Delete the  revCycle by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RevCycle : {}", id);
        revCycleRepository.delete(id);
    }
}
