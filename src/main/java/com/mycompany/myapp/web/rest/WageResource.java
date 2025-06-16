package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Wage;
import com.mycompany.myapp.repository.WageRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Wage}.
 */
@RestController
@RequestMapping("/api/wages")
@Transactional
public class WageResource {

    private static final Logger LOG = LoggerFactory.getLogger(WageResource.class);

    private static final String ENTITY_NAME = "wage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WageRepository wageRepository;

    public WageResource(WageRepository wageRepository) {
        this.wageRepository = wageRepository;
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PostMapping("")
    public ResponseEntity<Wage> createWage(@Valid @RequestBody Wage wage) throws URISyntaxException {
        LOG.debug("REST request to save Wage : {}", wage);
        if (wage.getId() != null) {
            throw new BadRequestAlertException("A new wage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        wage = wageRepository.save(wage);
        return ResponseEntity.created(new URI("/api/wages/" + wage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, wage.getId().toString()))
            .body(wage);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PutMapping("/{id}")
    public ResponseEntity<Wage> updateWage(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Wage wage)
        throws URISyntaxException {
        LOG.debug("REST request to update Wage : {}, {}", id, wage);
        if (wage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        wage = wageRepository.save(wage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wage.getId().toString()))
            .body(wage);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Wage> partialUpdateWage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Wage wage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Wage partially : {}, {}", id, wage);
        if (wage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Wage> result = wageRepository
            .findById(wage.getId())
            .map(existingWage -> {
                if (wage.getCoefficients() != null) {
                    existingWage.setCoefficients(wage.getCoefficients());
                }
                if (wage.getBaseSalary() != null) {
                    existingWage.setBaseSalary(wage.getBaseSalary());
                }
                if (wage.getAllowance() != null) {
                    existingWage.setAllowance(wage.getAllowance());
                }

                return existingWage;
            })
            .map(wageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wage.getId().toString())
        );
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("")
    public ResponseEntity<List<Wage>> getAllWages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Wages");
        Page<Wage> page = wageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/{id}")
    public ResponseEntity<Wage> getWage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Wage : {}", id);
        Optional<Wage> wage = wageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wage);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Wage : {}", id);
        wageRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
