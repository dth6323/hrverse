package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PdfDocument;
import java.util.List;
import java.util.Map;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PdfDocumentRepository extends ElasticsearchRepository<PdfDocument, String> {
    @Query("{ \"wildcard\": { \"content\": \"*?0*\" } }")
    List<PdfDocument> findByContentContaining(String keyword);

    @Query(
        """
        {\n" +
        "  \"query\": {\n" +
        "    \"match\": {\n" +
        "      \"content\": \"?0\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"highlight\": {\n" +
        "    \"fields\": {\n" +
        "      \"content\": {}\n" +
        "    }\n" +
        "  }\n" +
        "}
        """
    )
    List<Map<String, Object>> findByContentWithHighlight(String keyword);
}
