package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SalaryDistribute;
import com.mycompany.myapp.repository.SalaryDistributeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SalaryDistribute}.
 */
@RestController
@RequestMapping("/api/salary-distributes")
@Transactional
public class SalaryDistributeResource {

    private static final Logger LOG = LoggerFactory.getLogger(SalaryDistributeResource.class);

    private static final String ENTITY_NAME = "salaryDistribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaryDistributeRepository salaryDistributeRepository;

    public SalaryDistributeResource(SalaryDistributeRepository salaryDistributeRepository) {
        this.salaryDistributeRepository = salaryDistributeRepository;
    }

    /**
     * {@code POST  /salary-distributes} : Create a new salaryDistribute.
     *
     * @param salaryDistribute the salaryDistribute to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salaryDistribute, or with status {@code 400 (Bad Request)} if the salaryDistribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SalaryDistribute> createSalaryDistribute(@Valid @RequestBody SalaryDistribute salaryDistribute)
        throws URISyntaxException {
        LOG.debug("REST request to save SalaryDistribute : {}", salaryDistribute);
        if (salaryDistribute.getId() != null) {
            throw new BadRequestAlertException("A new salaryDistribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        salaryDistribute = salaryDistributeRepository.save(salaryDistribute);
        return ResponseEntity.created(new URI("/api/salary-distributes/" + salaryDistribute.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, salaryDistribute.getId().toString()))
            .body(salaryDistribute);
    }

    /**
     * {@code PUT  /salary-distributes/:id} : Updates an existing salaryDistribute.
     *
     * @param id the id of the salaryDistribute to save.
     * @param salaryDistribute the salaryDistribute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryDistribute,
     * or with status {@code 400 (Bad Request)} if the salaryDistribute is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salaryDistribute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalaryDistribute> updateSalaryDistribute(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SalaryDistribute salaryDistribute
    ) throws URISyntaxException {
        LOG.debug("REST request to update SalaryDistribute : {}, {}", id, salaryDistribute);
        if (salaryDistribute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaryDistribute.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salaryDistributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        salaryDistribute = salaryDistributeRepository.save(salaryDistribute);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salaryDistribute.getId().toString()))
            .body(salaryDistribute);
    }

    /**
     * {@code PATCH  /salary-distributes/:id} : Partial updates given fields of an existing salaryDistribute, field will ignore if it is null
     *
     * @param id the id of the salaryDistribute to save.
     * @param salaryDistribute the salaryDistribute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salaryDistribute,
     * or with status {@code 400 (Bad Request)} if the salaryDistribute is not valid,
     * or with status {@code 404 (Not Found)} if the salaryDistribute is not found,
     * or with status {@code 500 (Internal Server Error)} if the salaryDistribute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalaryDistribute> partialUpdateSalaryDistribute(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SalaryDistribute salaryDistribute
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SalaryDistribute partially : {}, {}", id, salaryDistribute);
        if (salaryDistribute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salaryDistribute.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salaryDistributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalaryDistribute> result = salaryDistributeRepository
            .findById(salaryDistribute.getId())
            .map(existingSalaryDistribute -> {
                if (salaryDistribute.getStartDate() != null) {
                    existingSalaryDistribute.setStartDate(salaryDistribute.getStartDate());
                }
                if (salaryDistribute.getEndDate() != null) {
                    existingSalaryDistribute.setEndDate(salaryDistribute.getEndDate());
                }
                if (salaryDistribute.getWorkDay() != null) {
                    existingSalaryDistribute.setWorkDay(salaryDistribute.getWorkDay());
                }
                if (salaryDistribute.getTypeOfSalary() != null) {
                    existingSalaryDistribute.setTypeOfSalary(salaryDistribute.getTypeOfSalary());
                }

                return existingSalaryDistribute;
            })
            .map(salaryDistributeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salaryDistribute.getId().toString())
        );
    }

    /**
     * {@code GET  /salary-distributes} : get all the salaryDistributes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaryDistributes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SalaryDistribute>> getAllSalaryDistributes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SalaryDistributes");
        Page<SalaryDistribute> page = salaryDistributeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /salary-distributes/:id} : get the "id" salaryDistribute.
     *
     * @param id the id of the salaryDistribute to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salaryDistribute, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalaryDistribute> getSalaryDistribute(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SalaryDistribute : {}", id);
        Optional<SalaryDistribute> salaryDistribute = salaryDistributeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(salaryDistribute);
    }

    /**
     * {@code DELETE  /salary-distributes/:id} : delete the "id" salaryDistribute.
     *
     * @param id the id of the salaryDistribute to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalaryDistribute(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SalaryDistribute : {}", id);
        salaryDistributeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
