package com.zixi.easyaiagent.config;

import com.zixi.easyaiagent.rag.reader.MyMarkdownDocumentLoader;
import com.zixi.easyaiagent.rag.transformer.DocumentMetadataEnricher;
import com.zixi.easyaiagent.rag.transformer.MyTokenTextSplitter;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 初始化基于内存的向量数据库
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private MyMarkdownDocumentLoader myMarkdownDocumentLoader;

    @Resource
    private DocumentMetadataEnricher documentMetadataEnricher;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Bean
    VectorStore loveAppVectorStore(@Qualifier("dashscopeEmbeddingModel") EmbeddingModel embeddingModel) {
        // 创建向量化存储（基于内存）
        VectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        // 本地知识库加载文档
        List<Document> documents = myMarkdownDocumentLoader.loadMarkdownDocuments();
        //自主切分文档
        List<Document> splitDocuments = myTokenTextSplitter.splitDocuments(documents);
        // 自动补充关键词元信息
        List<Document> enrichedDocuments = documentMetadataEnricher.enrichDocuments(splitDocuments, 5);
        // 向量化存储
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }
}
