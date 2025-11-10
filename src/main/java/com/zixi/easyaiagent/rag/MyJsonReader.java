package com.zixi.easyaiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyJsonReader {

    private final Resource resource;

    public MyJsonReader(@Value("classpath:bikes.json") Resource resource) {
        this.resource = resource;
    }

    public List<Document> loadBasicJsonDocuments() {
        JsonReader jsonReader = new JsonReader(this.resource);
        return jsonReader.get();
    }

    public List<Document> loadJsonWithSpecificFields() {
        JsonReader jsonReader = new JsonReader(this.resource, "description", "features");
        return jsonReader.get();
    }

    public List<Document> loadJsonWithPointer() {
        JsonReader jsonReader = new JsonReader(this.resource);
        // 提取items数组内的内容
        return jsonReader.get("/items");
    }
}
