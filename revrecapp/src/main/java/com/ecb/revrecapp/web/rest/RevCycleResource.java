package com.ecb.revrecapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ecb.revrecapp.service.RevCycleService;
import com.ecb.revrecapp.web.rest.util.HeaderUtil;
import com.ecb.revrecapp.service.dto.RevCycleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing RevCycle.
 */
@RestController
@RequestMapping("/api")
public class RevCycleResource {

    private final Logger log = LoggerFactory.getLogger(RevCycleResource.class);
        
    @Inject
    private RevCycleService revCycleService;

    /**
     * POST  /rev-cycles : Create a new revCycle.
     *
     * @param revCycleDTO the revCycleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new revCycleDTO, or with status 400 (Bad Request) if the revCycle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rev-cycles")
    @Timed
    public ResponseEntity<RevCycleDTO> createRevCycle(@Valid @RequestBody RevCycleDTO revCycleDTO) throws URISyntaxException {
        log.debug("REST request to save RevCycle : {}", revCycleDTO);
        if (revCycleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("revCycle", "idexists", "A new revCycle cannot already have an ID")).body(null);
        }
        RevCycleDTO result = revCycleService.save(revCycleDTO);
        return ResponseEntity.created(new URI("/api/rev-cycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("revCycle", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rev-cycles : Updates an existing revCycle.
     *
     * @param revCycleDTO the revCycleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated revCycleDTO,
     * or with status 400 (Bad Request) if the revCycleDTO is not valid,
     * or with status 500 (Internal Server Error) if the revCycleDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rev-cycles")
    @Timed
    public ResponseEntity<RevCycleDTO> updateRevCycle(@Valid @RequestBody RevCycleDTO revCycleDTO) throws URISyntaxException {
        log.debug("REST request to update RevCycle : {}", revCycleDTO);
        if (revCycleDTO.getId() == null) {
            return createRevCycle(revCycleDTO);
        }
        RevCycleDTO result = revCycleService.save(revCycleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("revCycle", revCycleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rev-cycles : get all the revCycles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of revCycles in body
     */
    @GetMapping("/rev-cycles")
    @Timed
    public List<RevCycleDTO> getAllRevCycles() {
        log.debug("REST request to get all RevCycles");
        return revCycleService.findAll();
    }

    /**
     * GET  /rev-cycles/:id : get the "id" revCycle.
     *
     * @param id the id of the revCycleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the revCycleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rev-cycles/{id}")
    @Timed
    public ResponseEntity<RevCycleDTO> getRevCycle(@PathVariable Long id) {
        log.debug("REST request to get RevCycle : {}", id);
        RevCycleDTO revCycleDTO = revCycleService.findOne(id);
        return Optional.ofNullable(revCycleDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rev-cycles/:id : delete the "id" revCycle.
     *
     * @param id the id of the revCycleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rev-cycles/{id}")
    @Timed
    public ResponseEntity<Void> deleteRevCycle(@PathVariable Long id) {
        log.debug("REST request to delete RevCycle : {}", id);
        revCycleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("revCycle", id.toString())).build();
    }

}
