package com.zixi.easyaiagent.rag.transformer;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 使用TokenTextSplitter拆分文档
 */
@Component
class MyTokenTextSplitter {

    public static void main(String[] args) {
        Document doc1 = new Document("This is a long piece of text that needs to be split into smaller chunks for processing.",
                Map.of("source", "example.txt"));
        Document doc2 = new Document("Another document with content that will be split based on token count.",
                Map.of("source", "example2.txt"));

        MyTokenTextSplitter myTokenTextSplitter = new MyTokenTextSplitter();
        List<Document> splitDocuments = myTokenTextSplitter.splitDocuments(List.of(doc1, doc2));
        for (Document doc : splitDocuments) {
            System.out.println("doc.getText() = " + doc.getText());
            System.out.println("doc.getMetadata() = " + doc.getMetadata());
            System.out.println("doc.getFormattedContent() = " + doc.getFormattedContent());
        }
    }

    public List<Document> splitDocuments(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.apply(documents);
    }

    public List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true);
        return splitter.apply(documents);
    }
}
