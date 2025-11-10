package com.zixi.easyaiagent.rag.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 使用Apache Tika从多种文档格式中提取文档，比如PDF、DOC/DOCX、PPT/PPTX、HTML
 * <a href="https://tika.apache.org/3.1.0/formats.html">Apache Tika</a>
 */
@Component
public class MyTikaDocumentReader {

    private final Resource resource;

    public MyTikaDocumentReader(@Value("classpath:/word-sample.docx") Resource resource) {
        this.resource = resource;
    }

    public List<Document> loadText() {
        return new TikaDocumentReader(resource).read();
    }
}
