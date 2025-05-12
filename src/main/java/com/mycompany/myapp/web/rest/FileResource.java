package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.FileService;
import com.mycompany.myapp.service.dto.response.SearchHighlightResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileResource {

    private final FileService fileService;

    public FileResource(FileService fileService) {
        this.fileService = fileService;
    }

    // API tải file lên MinIO
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Tải file lên MinIO và lấy URL trả về
            String fileUrl = fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }

    // API tải file xuống từ MinIO
    @GetMapping("/filesall")
    public List<String> listFiles() {
        return fileService.listFiles();
    }

    @GetMapping("/readfiles")
    public ResponseEntity<?> getFile(@RequestParam("filename") String fileName) {
        try {
            byte[] fileBytes = fileService.getFile(fileName);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam("keyword") String keyword) throws IOException {
        return ResponseEntity.ok(fileService.searchDocuments(keyword));
    }

    @GetMapping("/search-by-content")
    public ResponseEntity<List<String>> searchFilesByContent(@RequestParam("keyword") String keyword) {
        try {
            List<String> matchingFiles = fileService.searchFilesByContent(keyword);
            return ResponseEntity.ok(matchingFiles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @GetMapping("/searchWithHighlight")
    public ResponseEntity<List<SearchHighlightResponse>> searchFileWithHighlight(@RequestParam("keyword") String keyword) {
        try {
            List<SearchHighlightResponse> results = fileService.searchDocuments(keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
