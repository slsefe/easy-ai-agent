package com.zixi.easyaiagent.rag.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 读取本地知识库的markdown文档，转换为Document对象列表
 */
@Slf4j
@Component
public class LoveAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    private static final String FILE_PATH = "classpath:document/*.md";

    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdownDocuments() {
        List<Document> documents = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources(FILE_PATH);
            for (Resource resource : resources) {
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        // 是否按照水平分割线拆分为多个文档
                        .withHorizontalRuleCreateDocument(true)
                        // 文档是否包含代码块
                        .withIncludeCodeBlock(false)
                        // 文档是否包含引用块
                        .withIncludeBlockquote(false)
                        // 指定文档额外元信息：文件名
                        .withAdditionalMetadata("filename", Objects.requireNonNull(resource.getFilename()))
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                documents.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("markdown file load error", e);
        }
        return documents;
    }
}
