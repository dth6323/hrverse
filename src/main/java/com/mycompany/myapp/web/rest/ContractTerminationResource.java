package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ContractTermination;
import com.mycompany.myapp.repository.ContractTerminationRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ContractTermination}.
 */
@RestController
@RequestMapping("/api/contract-terminations")
@Transactional
public class ContractTerminationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContractTerminationResource.class);

    private static final String ENTITY_NAME = "contractTermination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractTerminationRepository contractTerminationRepository;

    public ContractTerminationResource(ContractTerminationRepository contractTerminationRepository) {
        this.contractTerminationRepository = contractTerminationRepository;
    }

    /**
     * {@code POST  /contract-terminations} : Create a new contractTermination.
     *
     * @param contractTermination the contractTermination to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractTermination, or with status {@code 400 (Bad Request)} if the contractTermination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContractTermination> createContractTermination(@Valid @RequestBody ContractTermination contractTermination)
        throws URISyntaxException {
        LOG.debug("REST request to save ContractTermination : {}", contractTermination);
        if (contractTermination.getId() != null) {
            throw new BadRequestAlertException("A new contractTermination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contractTermination = contractTerminationRepository.save(contractTermination);
        return ResponseEntity.created(new URI("/api/contract-terminations/" + contractTermination.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contractTermination.getId().toString()))
            .body(contractTermination);
    }

    /**
     * {@code PUT  /contract-terminations/:id} : Updates an existing contractTermination.
     *
     * @param id the id of the contractTermination to save.
     * @param contractTermination the contractTermination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractTermination,
     * or with status {@code 400 (Bad Request)} if the contractTermination is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractTermination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContractTermination> updateContractTermination(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractTermination contractTermination
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContractTermination : {}, {}", id, contractTermination);
        if (contractTermination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractTermination.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractTerminationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contractTermination = contractTerminationRepository.save(contractTermination);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractTermination.getId().toString()))
            .body(contractTermination);
    }

    /**
     * {@code PATCH  /contract-terminations/:id} : Partial updates given fields of an existing contractTermination, field will ignore if it is null
     *
     * @param id the id of the contractTermination to save.
     * @param contractTermination the contractTermination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractTermination,
     * or with status {@code 400 (Bad Request)} if the contractTermination is not valid,
     * or with status {@code 404 (Not Found)} if the contractTermination is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractTermination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractTermination> partialUpdateContractTermination(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractTermination contractTermination
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContractTermination partially : {}, {}", id, contractTermination);
        if (contractTermination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractTermination.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractTerminationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractTermination> result = contractTerminationRepository
            .findById(contractTermination.getId())
            .map(existingContractTermination -> {
                if (contractTermination.getTerminationDate() != null) {
                    existingContractTermination.setTerminationDate(contractTermination.getTerminationDate());
                }
                if (contractTermination.getReason() != null) {
                    existingContractTermination.setReason(contractTermination.getReason());
                }
                if (contractTermination.getCompensation() != null) {
                    existingContractTermination.setCompensation(contractTermination.getCompensation());
                }

                return existingContractTermination;
            })
            .map(contractTerminationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractTermination.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-terminations} : get all the contractTerminations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractTerminations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContractTermination>> getAllContractTerminations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ContractTerminations");
        Page<ContractTermination> page = contractTerminationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contract-terminations/:id} : get the "id" contractTermination.
     *
     * @param id the id of the contractTermination to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractTermination, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractTermination> getContractTermination(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContractTermination : {}", id);
        Optional<ContractTermination> contractTermination = contractTerminationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contractTermination);
    }

    /**
     * {@code DELETE  /contract-terminations/:id} : delete the "id" contractTermination.
     *
     * @param id the id of the contractTermination to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractTermination(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContractTermination : {}", id);
        contractTerminationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
