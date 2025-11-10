package com.zixi.easyaiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentWriter;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyDocumentWriter {

    /**
     * @param documents
     * @param fileName
     */
    public void writeToFile(List<Document> documents, String fileName) {
        DocumentWriter writer = new FileDocumentWriter(
                fileName, // 文件名称
                true, // 是否包含文档标记
                MetadataMode.ALL, // 写入哪些元数据
                false // 覆盖写or追加写
        );
        writer.accept(documents);
    }
}
