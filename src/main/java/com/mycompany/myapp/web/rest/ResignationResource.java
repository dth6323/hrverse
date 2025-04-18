package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Resignation;
import com.mycompany.myapp.repository.ResignationRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Resignation}.
 */
@RestController
@RequestMapping("/api/resignations")
@Transactional
public class ResignationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ResignationResource.class);

    private static final String ENTITY_NAME = "resignation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResignationRepository resignationRepository;

    public ResignationResource(ResignationRepository resignationRepository) {
        this.resignationRepository = resignationRepository;
    }

    /**
     * {@code POST  /resignations} : Create a new resignation.
     *
     * @param resignation the resignation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resignation, or with status {@code 400 (Bad Request)} if the resignation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Resignation> createResignation(@Valid @RequestBody Resignation resignation) throws URISyntaxException {
        LOG.debug("REST request to save Resignation : {}", resignation);
        if (resignation.getId() != null) {
            throw new BadRequestAlertException("A new resignation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        resignation = resignationRepository.save(resignation);
        return ResponseEntity.created(new URI("/api/resignations/" + resignation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, resignation.getId().toString()))
            .body(resignation);
    }

    /**
     * {@code PUT  /resignations/:id} : Updates an existing resignation.
     *
     * @param id the id of the resignation to save.
     * @param resignation the resignation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resignation,
     * or with status {@code 400 (Bad Request)} if the resignation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resignation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Resignation> updateResignation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Resignation resignation
    ) throws URISyntaxException {
        LOG.debug("REST request to update Resignation : {}, {}", id, resignation);
        if (resignation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resignation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resignationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        resignation = resignationRepository.save(resignation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resignation.getId().toString()))
            .body(resignation);
    }

    /**
     * {@code PATCH  /resignations/:id} : Partial updates given fields of an existing resignation, field will ignore if it is null
     *
     * @param id the id of the resignation to save.
     * @param resignation the resignation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resignation,
     * or with status {@code 400 (Bad Request)} if the resignation is not valid,
     * or with status {@code 404 (Not Found)} if the resignation is not found,
     * or with status {@code 500 (Internal Server Error)} if the resignation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Resignation> partialUpdateResignation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Resignation resignation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Resignation partially : {}, {}", id, resignation);
        if (resignation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resignation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resignationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Resignation> result = resignationRepository
            .findById(resignation.getId())
            .map(existingResignation -> {
                if (resignation.getSubmissionDate() != null) {
                    existingResignation.setSubmissionDate(resignation.getSubmissionDate());
                }
                if (resignation.getEffectiveDate() != null) {
                    existingResignation.setEffectiveDate(resignation.getEffectiveDate());
                }
                if (resignation.getReason() != null) {
                    existingResignation.setReason(resignation.getReason());
                }
                if (resignation.getStatus() != null) {
                    existingResignation.setStatus(resignation.getStatus());
                }
                if (resignation.getNotes() != null) {
                    existingResignation.setNotes(resignation.getNotes());
                }

                return existingResignation;
            })
            .map(resignationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resignation.getId().toString())
        );
    }

    /**
     * {@code GET  /resignations} : get all the resignations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resignations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Resignation>> getAllResignations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Resignations");
        Page<Resignation> page = resignationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resignations/:id} : get the "id" resignation.
     *
     * @param id the id of the resignation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resignation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resignation> getResignation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Resignation : {}", id);
        Optional<Resignation> resignation = resignationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resignation);
    }

    /**
     * {@code DELETE  /resignations/:id} : delete the "id" resignation.
     *
     * @param id the id of the resignation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResignation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Resignation : {}", id);
        resignationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
