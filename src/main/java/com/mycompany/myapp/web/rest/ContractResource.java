package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Contract;
import com.mycompany.myapp.repository.ContractRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Contract}.
 */
@RestController
@RequestMapping("/api/contracts")
@Transactional
public class ContractResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContractResource.class);

    private static final String ENTITY_NAME = "contract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractRepository contractRepository;

    public ContractResource(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @PostMapping("")
    public ResponseEntity<Contract> createContract(@Valid @RequestBody Contract contract) throws URISyntaxException {
        LOG.debug("REST request to save Contract : {}", contract);
        if (contract.getId() != null) {
            throw new BadRequestAlertException("A new contract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contract = contractRepository.save(contract);
        return ResponseEntity.created(new URI("/api/contracts/" + contract.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contract.getId().toString()))
            .body(contract);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Contract contract
    ) throws URISyntaxException {
        LOG.debug("REST request to update Contract : {}, {}", id, contract);
        if (contract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contract = contractRepository.save(contract);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contract.getId().toString()))
            .body(contract);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Contract> partialUpdateContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Contract contract
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Contract partially : {}, {}", id, contract);
        if (contract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Contract> result = contractRepository
            .findById(contract.getId())
            .map(existingContract -> {
                if (contract.getStartDate() != null) {
                    existingContract.setStartDate(contract.getStartDate());
                }
                if (contract.getEndDate() != null) {
                    existingContract.setEndDate(contract.getEndDate());
                }
                if (contract.getStatus() != null) {
                    existingContract.setStatus(contract.getStatus());
                }
                if (contract.getContractCode() != null) {
                    existingContract.setContractCode(contract.getContractCode());
                }

                return existingContract;
            })
            .map(contractRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contract.getId().toString())
        );
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @GetMapping("")
    public ResponseEntity<List<Contract>> getAllContracts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Contracts");
        Page<Contract> page = contractRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Contract : {}", id);
        Optional<Contract> contract = contractRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contract);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Contract : {}", id);
        contractRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
