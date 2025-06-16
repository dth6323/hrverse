package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Attendance;
import com.mycompany.myapp.elasticRepository.AttendanceSearchRepository;
import com.mycompany.myapp.repository.AttendanceRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.AttendanceImportService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/attendances")
@Transactional
public class AttendanceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AttendanceResource.class);

    private static final String ENTITY_NAME = "attendance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttendanceImportService attendanceService;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceSearchRepository attendanceSearchRepository;

    public AttendanceResource(
        AttendanceRepository attendanceRepository,
        AttendanceImportService attendanceService,
        AttendanceSearchRepository attendanceSearchRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceService = attendanceService;
        this.attendanceSearchRepository = attendanceSearchRepository;
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportAttendancesToExcel() throws IOException {
        ByteArrayInputStream in = attendanceService.exportToExcel();
        InputStreamResource resource = new InputStreamResource(in);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendances.xlsx");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @PostMapping("/import")
    public ResponseEntity<String> importAttendance(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) return ResponseEntity.status(500).body(
                "File must end with xlsx"
            );
            int importedCount = attendanceService.importFromExcel(file);
            return ResponseEntity.ok("Successfully imported " + importedCount + " attendance records");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing attendance data: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @PostMapping("")
    public ResponseEntity<Attendance> createAttendance(@Valid @RequestBody Attendance attendance) throws URISyntaxException {
        LOG.debug("REST request to save Attendance : {}", attendance);
        if (attendance.getId() != null) {
            throw new BadRequestAlertException("A new attendance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        attendance = attendanceRepository.save(attendance);
        attendanceSearchRepository.save(attendance);
        return ResponseEntity.created(new URI("/api/attendances/" + attendance.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, attendance.getId().toString()))
            .body(attendance);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Attendance attendance
    ) throws URISyntaxException {
        LOG.debug("REST request to update Attendance : {}, {}", id, attendance);
        if (attendance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attendanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        attendance = attendanceRepository.save(attendance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendance.getId().toString()))
            .body(attendance);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Attendance> partialUpdateAttendance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Attendance attendance
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Attendance partially : {}, {}", id, attendance);
        if (attendance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attendance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attendanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Attendance> result = attendanceRepository
            .findById(attendance.getId())
            .map(existingAttendance -> {
                if (attendance.getDateOfwork() != null) {
                    existingAttendance.setDateOfwork(attendance.getDateOfwork());
                }
                if (attendance.getCheckInTime() != null) {
                    existingAttendance.setCheckInTime(attendance.getCheckInTime());
                }
                if (attendance.getCheckOutTime() != null) {
                    existingAttendance.setCheckOutTime(attendance.getCheckOutTime());
                }
                if (attendance.getWorkHour() != null) {
                    existingAttendance.setWorkHour(attendance.getWorkHour());
                }

                return existingAttendance;
            })
            .map(attendanceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendance.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<Attendance>> getAllAttendances(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Attendances");
        Page<Attendance> page = attendanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendance(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Attendance : {}", id);
        Optional<Attendance> attendance = attendanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attendance);
    }

    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\", \"" + AuthoritiesConstants.MANAGER + "\")")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Attendance : {}", id);
        attendanceRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
