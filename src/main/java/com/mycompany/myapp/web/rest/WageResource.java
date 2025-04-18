package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Wage;
import com.mycompany.myapp.repository.WageRepository;
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

    /**
     * {@code POST  /wages} : Create a new wage.
     *
     * @param wage the wage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wage, or with status {@code 400 (Bad Request)} if the wage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

    /**
     * {@code PUT  /wages/:id} : Updates an existing wage.
     *
     * @param id the id of the wage to save.
     * @param wage the wage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wage,
     * or with status {@code 400 (Bad Request)} if the wage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

    /**
     * {@code PATCH  /wages/:id} : Partial updates given fields of an existing wage, field will ignore if it is null
     *
     * @param id the id of the wage to save.
     * @param wage the wage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wage,
     * or with status {@code 400 (Bad Request)} if the wage is not valid,
     * or with status {@code 404 (Not Found)} if the wage is not found,
     * or with status {@code 500 (Internal Server Error)} if the wage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

    /**
     * {@code GET  /wages} : get all the wages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Wage>> getAllWages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Wages");
        Page<Wage> page = wageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wages/:id} : get the "id" wage.
     *
     * @param id the id of the wage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Wage> getWage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Wage : {}", id);
        Optional<Wage> wage = wageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wage);
    }

    /**
     * {@code DELETE  /wages/:id} : delete the "id" wage.
     *
     * @param id the id of the wage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Wage : {}", id);
        wageRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
