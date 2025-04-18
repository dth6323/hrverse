package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RewardPunishment;
import com.mycompany.myapp.repository.RewardPunishmentRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RewardPunishment}.
 */
@RestController
@RequestMapping("/api/reward-punishments")
@Transactional
public class RewardPunishmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(RewardPunishmentResource.class);

    private static final String ENTITY_NAME = "rewardPunishment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RewardPunishmentRepository rewardPunishmentRepository;

    public RewardPunishmentResource(RewardPunishmentRepository rewardPunishmentRepository) {
        this.rewardPunishmentRepository = rewardPunishmentRepository;
    }

    /**
     * {@code POST  /reward-punishments} : Create a new rewardPunishment.
     *
     * @param rewardPunishment the rewardPunishment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rewardPunishment, or with status {@code 400 (Bad Request)} if the rewardPunishment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RewardPunishment> createRewardPunishment(@Valid @RequestBody RewardPunishment rewardPunishment)
        throws URISyntaxException {
        LOG.debug("REST request to save RewardPunishment : {}", rewardPunishment);
        if (rewardPunishment.getId() != null) {
            throw new BadRequestAlertException("A new rewardPunishment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rewardPunishment = rewardPunishmentRepository.save(rewardPunishment);
        return ResponseEntity.created(new URI("/api/reward-punishments/" + rewardPunishment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rewardPunishment.getId().toString()))
            .body(rewardPunishment);
    }

    /**
     * {@code PUT  /reward-punishments/:id} : Updates an existing rewardPunishment.
     *
     * @param id the id of the rewardPunishment to save.
     * @param rewardPunishment the rewardPunishment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rewardPunishment,
     * or with status {@code 400 (Bad Request)} if the rewardPunishment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rewardPunishment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RewardPunishment> updateRewardPunishment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RewardPunishment rewardPunishment
    ) throws URISyntaxException {
        LOG.debug("REST request to update RewardPunishment : {}, {}", id, rewardPunishment);
        if (rewardPunishment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rewardPunishment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rewardPunishmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rewardPunishment = rewardPunishmentRepository.save(rewardPunishment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rewardPunishment.getId().toString()))
            .body(rewardPunishment);
    }

    /**
     * {@code PATCH  /reward-punishments/:id} : Partial updates given fields of an existing rewardPunishment, field will ignore if it is null
     *
     * @param id the id of the rewardPunishment to save.
     * @param rewardPunishment the rewardPunishment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rewardPunishment,
     * or with status {@code 400 (Bad Request)} if the rewardPunishment is not valid,
     * or with status {@code 404 (Not Found)} if the rewardPunishment is not found,
     * or with status {@code 500 (Internal Server Error)} if the rewardPunishment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RewardPunishment> partialUpdateRewardPunishment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RewardPunishment rewardPunishment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RewardPunishment partially : {}, {}", id, rewardPunishment);
        if (rewardPunishment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rewardPunishment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rewardPunishmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RewardPunishment> result = rewardPunishmentRepository
            .findById(rewardPunishment.getId())
            .map(existingRewardPunishment -> {
                if (rewardPunishment.getType() != null) {
                    existingRewardPunishment.setType(rewardPunishment.getType());
                }
                if (rewardPunishment.getAmount() != null) {
                    existingRewardPunishment.setAmount(rewardPunishment.getAmount());
                }
                if (rewardPunishment.getReason() != null) {
                    existingRewardPunishment.setReason(rewardPunishment.getReason());
                }
                if (rewardPunishment.getApplyDate() != null) {
                    existingRewardPunishment.setApplyDate(rewardPunishment.getApplyDate());
                }
                if (rewardPunishment.getNotes() != null) {
                    existingRewardPunishment.setNotes(rewardPunishment.getNotes());
                }

                return existingRewardPunishment;
            })
            .map(rewardPunishmentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rewardPunishment.getId().toString())
        );
    }

    /**
     * {@code GET  /reward-punishments} : get all the rewardPunishments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rewardPunishments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RewardPunishment>> getAllRewardPunishments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of RewardPunishments");
        Page<RewardPunishment> page = rewardPunishmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reward-punishments/:id} : get the "id" rewardPunishment.
     *
     * @param id the id of the rewardPunishment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rewardPunishment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RewardPunishment> getRewardPunishment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RewardPunishment : {}", id);
        Optional<RewardPunishment> rewardPunishment = rewardPunishmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rewardPunishment);
    }

    /**
     * {@code DELETE  /reward-punishments/:id} : delete the "id" rewardPunishment.
     *
     * @param id the id of the rewardPunishment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRewardPunishment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RewardPunishment : {}", id);
        rewardPunishmentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
