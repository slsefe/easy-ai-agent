package com.zixi.easyaiagent.rag.transformer;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文档元数据增强
 */
@Component
public class DocumentMetadataEnricher {

    private final ChatModel chatModel;

    public DocumentMetadataEnricher(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 基于KeywordMetadataEnricher，使用chatModel提取指定数量的关键词，并且加入到文档的元信息
     * prompt template:
     * {context_str}.
     * Give %s unique keywords for this document.
     * Format as comma separated. Keywords:
     * @param documents 文档集合
     * @param keywordCount 关键词数量
     * @return 文档集合，元信息包含了关键词，key：excerpt_keywords
     */
    public List<Document> enrichDocuments(List<Document> documents, Integer keywordCount) {
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(chatModel, keywordCount);
        return enricher.apply(documents);
    }

    /**
     * 基于SummaryMetadataEnricher，使用ChatModel生成文档摘要并添加到文档元数据
     * prompt template:
     * Here is the content of the section:
     * {context_str}
     * Summarize the key topics and entities of the section.
     * Summary:
     * @param documents 文档集合
     * @return 文档集合，元信息包含了文档摘要，key：section_summary
     */
    public List<Document> summaryDocuments(List<Document> documents) {
        SummaryMetadataEnricher enricher = new SummaryMetadataEnricher(chatModel, List.of(SummaryMetadataEnricher.SummaryType.CURRENT));
        return enricher.apply(documents);
    }
}
