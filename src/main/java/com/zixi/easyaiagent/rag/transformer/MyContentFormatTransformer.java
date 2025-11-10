package com.zixi.easyaiagent.rag.transformer;

import org.springframework.ai.document.ContentFormatter;
import org.springframework.ai.document.DefaultContentFormatter;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.ContentFormatTransformer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyContentFormatTransformer {

    private final ContentFormatter contentFormatter;

    public MyContentFormatTransformer() {
        this.contentFormatter = DefaultContentFormatter.builder().build();
    }

    public List<Document> transform(List<Document> documents) {
        ContentFormatTransformer transformer = new ContentFormatTransformer(contentFormatter);
        return transformer.apply(documents);
    }
}
