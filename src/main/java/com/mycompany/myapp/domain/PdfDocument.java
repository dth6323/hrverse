package com.mycompany.myapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "search-waws")
public class PdfDocument {

    @Id
    private String id; // ID duy nhất

    @Field(type = FieldType.Text)
    private String fileName; // Tên file PDF

    @Field(type = FieldType.Text)
    private String content; // Nội dung trích xuất từ PDF

    // Getters và setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
