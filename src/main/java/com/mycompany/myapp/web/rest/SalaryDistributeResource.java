package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Payroll;
import com.mycompany.myapp.domain.SalaryDistribute;
import com.mycompany.myapp.repository.SalaryDistributeRepository;
import com.mycompany.myapp.service.FileService;
import com.mycompany.myapp.service.SalaryDistributeService;
import com.mycompany.myapp.service.dto.request.CaculateSalaryRequest;
import com.mycompany.myapp.service.dto.request.SalaryDistributeDetailrequest;
import com.mycompany.myapp.service.dto.response.PdfExportResponse;
import com.mycompany.myapp.service.dto.response.SalaryDistributeDetailResponse;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private final SalaryDistributeService salaryDistributeService;
    private final FileService fileService;

    public SalaryDistributeResource(
        SalaryDistributeRepository salaryDistributeRepository,
        SalaryDistributeService salaryDistributeService,
        FileService fileService
    ) {
        this.salaryDistributeRepository = salaryDistributeRepository;
        this.salaryDistributeService = salaryDistributeService;
        this.fileService = fileService;
    }

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

    @GetMapping("/test")
    public String test() {
        return "Test endpoint working";
    }

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

    @PostMapping("/caculate")
    public ResponseEntity<List<Payroll>> caculateSalaryDistribute(@RequestBody CaculateSalaryRequest request) {
        return ResponseEntity.ok(salaryDistributeService.salaryCaculate(request));
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportDetails(@RequestParam("id") String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] pdfData = salaryDistributeService.exportSalaryDistribute(id);
        ByteArrayResource resource = new ByteArrayResource(pdfData);
        String fileName = String.format("salary-report-%s-%s.pdf", id, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        HttpHeaders headers = new HttpHeaders();
        String contentDisposition = "attachment; filename=" + fileName;
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        InputStream ips = new ByteArrayInputStream(pdfData);
        fileService.uploadToMinio(ips, Long.valueOf(pdfData.length), "application/pdf", fileName);

        InputStream contentStream = new ByteArrayInputStream(pdfData); // Tạo InputStream mới cho trích xuất
        String content = fileService.extractContentFromFile(contentStream, fileName);
        fileService.uploadToElastic(content, fileName);
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(resource.contentLength())
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
    }

    @GetMapping("/details")
    public ResponseEntity<List<SalaryDistributeDetailResponse>> findDetail(@RequestParam("id") String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(salaryDistributeService.showSalaryDistribute(id));
    }
}
