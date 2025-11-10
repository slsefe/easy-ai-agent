package com.zixi.easyaiagent.rag.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyPdfDocumentReader {

    /**
     * PagePdfDocumentReader：使用PdfBox库分割PDF文档
     * @return 文档列表
     */
    List<Document> getDocsFromPdf() {

        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                        .withNumberOfTopTextLinesToDelete(0)
                        .build())
                .withPagesPerDocument(1)
                .build();

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:/sample1.pdf", config);
        return pdfReader.read();
    }

    /**
     * ParagraphPdfDocumentReader: 使用PDF catalog信息分割PDF，每个段落一个文档
     * 注意：不是所有的PDF文档都包括catalog信息
     * @return 文档列表
     */
    public List<Document> getDocsFromPdfWithCatalog() {
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                        .withNumberOfTopTextLinesToDelete(0)
                        .build())
                .withPagesPerDocument(1)
                .build();
        ParagraphPdfDocumentReader reader = new ParagraphPdfDocumentReader("classpath:/sample1.pdf", config);
        return reader.read();
    }

}
