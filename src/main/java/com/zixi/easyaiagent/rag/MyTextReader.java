package com.zixi.easyaiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyTextReader {
    private final Resource resource;

    public MyTextReader(@Value("classpath:text-source.txt") Resource resource) {
        this.resource = resource;
    }

    List<Document> loadText() {
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("filename", "text-source.txt");
        return textReader.read();
    }
}
