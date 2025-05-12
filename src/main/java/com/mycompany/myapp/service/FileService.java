package com.mycompany.myapp.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.mycompany.myapp.domain.MinioConfig;
import com.mycompany.myapp.domain.PdfDocument;
import com.mycompany.myapp.repository.PdfDocumentRepository;
import com.mycompany.myapp.service.dto.response.SearchHighlightResponse;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.hibernate.mapping.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final MinioConfig minioConfig;
    private final MinioClient minioClient;
    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    private PdfDocumentRepository fileDocumentRepository;

    public FileService(MinioConfig minioConfig, ElasticsearchClient elasticsearchClient) {
        this.minioConfig = minioConfig;
        this.minioClient = MinioClient.builder()
            .endpoint(minioConfig.getUrl())
            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
            .build();
        this.elasticsearchClient = elasticsearchClient;
    }

    public Void uploadToMinio(InputStream file, Long fileSize, String contentType, String fileName) throws Exception {
        String bucketName = minioConfig.getBucketName();

        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isBucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        minioClient.putObject(
            PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(file, fileSize, -1).contentType(contentType).build()
        );
        return null;
    }

    public Void uploadToElastic(String content, String fileName) throws Exception {
        if (content != null) {
            PdfDocument fileDocument = new PdfDocument();
            fileDocument.setId(fileName); // Sử dụng tên file làm ID
            fileDocument.setFileName(fileName);
            fileDocument.setContent(content);
            fileDocumentRepository.save(fileDocument); // Lưu vào Elasticsearch
        }
        return null;
    }

    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream ips = file.getInputStream();
        Long fileSize = file.getSize();
        String contentType = file.getContentType();

        uploadToMinio(ips, fileSize, contentType, fileName);
        // Trích xuất nội dung từ file PDF và lưu vào Elasticsearch
        String content = extractContentFromFile(file.getInputStream(), fileName);
        uploadToElastic(content, fileName);
        return fileName;
    }

    // Đọc file từ MinIO
    public String readFileContentFromMinIO(String fileName) throws Exception {
        InputStream inputStream = minioClient.getObject(
            GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileName).build()
        );

        if (fileName.endsWith(".docx") || fileName.endsWith(".doc")) {
            return extractContentFromFile(inputStream, fileName);
        }
        return null;
    }

    public String extractContentFromFile(InputStream inputStream, String fileName) throws IOException {
        return extractContentFromPdf(inputStream);
    }

    public List<SearchHighlightResponse> searchDocuments(String searchTerm) throws IOException {
        try {
            Highlight highlight = new Highlight.Builder()
                .fields("content", h -> h.preTags("<mark>").postTags("</mark>").numberOfFragments(3).fragmentSize(150))
                .build();

            SearchRequest request = new SearchRequest.Builder()
                .index("search-waws")
                .query(q -> q.matchPhrasePrefix(m -> m.field("content").query(searchTerm)))
                .highlight(highlight)
                .source(config -> config.filter(f -> f.includes("content", "fileName")))
                .build();

            SearchResponse<SearchHighlightResponse> response = elasticsearchClient.search(request, SearchHighlightResponse.class);

            List<SearchHighlightResponse> results = new ArrayList<>();

            for (Hit<SearchHighlightResponse> hit : response.hits().hits()) {
                SearchHighlightResponse dto = new SearchHighlightResponse();
                dto.setFileName(hit.id());
                Map<String, List<String>> highlightFields = hit.highlight();
                if (highlightFields != null && highlightFields.containsKey("content")) {
                    // Nếu có highlight, lấy nội dung đã highlight
                    dto.setHighlight(highlightFields.get("content").get(0));
                }
                results.add(dto);
            }
            return results;
        } catch (Exception e) {
            throw e;
        }
    }

    private String extractContentFromPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            return new PDFTextStripper().getText(document); // Trích xuất nội dung
        }
    }

    //    public List<SearchResultDTO> searchFilesWithHighlight(String keyword) {
    //        List<Map<String, Object>> rawResults = fileDocumentRepository.findByContentWithHighlight(keyword);
    //        List<SearchResultDTO> results = new ArrayList<>();
    //
    //        for (Map<String, Object> result : rawResults) {
    //            String fileName = (String) ((Map<String, Object>) result.get("_source")).get("fileName");
    //            List<String> highlights = (List<String>) ((Map<String, Object>) result.get("highlight")).get("content");
    //
    //            SearchResultDTO searchResult = new SearchResultDTO();
    //            searchResult.setFileName(fileName);
    //            searchResult.setHighlightedContent(String.join(" ... ", highlights));
    //            results.add(searchResult);
    //        }
    //
    //        return results;
    //    }

    //public List<Map<String, Object>> test(String keyword) {
    //List<Map<String, Object>> rawResults = fileDocumentRepository.findByContentWithHighlight(keyword);
    //return rawResults;
    //}

    public List<String> searchFilesByContent(String keyword) {
        List<String> matchingFiles = new ArrayList<>();
        try {
            Iterable<PdfDocument> searchResults = fileDocumentRepository.findByContentContaining(keyword);

            for (PdfDocument document : searchResults) {
                matchingFiles.add(document.getFileName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while searching for files", e);
        }
        return matchingFiles;
    }

    public byte[] getFile(String fileName) {
        String bucketName = minioConfig.getBucketName();
        try {
            GetObjectResponse objectResponse = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            return objectResponse.readAllBytes();
        } catch (MinioException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading file from MinIO", e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> listFiles() {
        String bucketName = minioConfig.getBucketName(); // Lấy tên bucket từ cấu hình
        List<String> fileNames = new ArrayList<>();
        try {
            // Liệt kê tất cả các đối tượng trong bucket
            Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());

            // Duyệt qua các đối tượng và lấy tên của chúng
            for (Result<Item> result : objects) {
                Item item = result.get();
                fileNames.add(item.objectName()); // Thêm tên file vào danh sách
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }
}
