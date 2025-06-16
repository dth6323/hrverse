package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ContractType;
import com.mycompany.myapp.repository.ContractTypeRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ContractType}.
 */
@RestController
@RequestMapping("/api/contract-types")
@Transactional
public class ContractTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContractTypeResource.class);

    private static final String ENTITY_NAME = "contractType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractTypeRepository contractTypeRepository;

    public ContractTypeResource(ContractTypeRepository contractTypeRepository) {
        this.contractTypeRepository = contractTypeRepository;
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PostMapping("")
    public ResponseEntity<ContractType> createContractType(@Valid @RequestBody ContractType contractType) throws URISyntaxException {
        LOG.debug("REST request to save ContractType : {}", contractType);
        if (contractType.getId() != null) {
            throw new BadRequestAlertException("A new contractType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contractType = contractTypeRepository.save(contractType);
        return ResponseEntity.created(new URI("/api/contract-types/" + contractType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contractType.getId().toString()))
            .body(contractType);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PutMapping("/{id}")
    public ResponseEntity<ContractType> updateContractType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractType contractType
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContractType : {}, {}", id, contractType);
        if (contractType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contractType = contractTypeRepository.save(contractType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractType.getId().toString()))
            .body(contractType);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractType> partialUpdateContractType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractType contractType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContractType partially : {}, {}", id, contractType);
        if (contractType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractType> result = contractTypeRepository
            .findById(contractType.getId())
            .map(existingContractType -> {
                if (contractType.getTypeName() != null) {
                    existingContractType.setTypeName(contractType.getTypeName());
                }
                if (contractType.getDescription() != null) {
                    existingContractType.setDescription(contractType.getDescription());
                }

                return existingContractType;
            })
            .map(contractTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractType.getId().toString())
        );
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("")
    public ResponseEntity<List<ContractType>> getAllContractTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ContractTypes");
        Page<ContractType> page = contractTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/{id}")
    public ResponseEntity<ContractType> getContractType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContractType : {}", id);
        Optional<ContractType> contractType = contractTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contractType);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContractType : {}", id);
        contractTypeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
