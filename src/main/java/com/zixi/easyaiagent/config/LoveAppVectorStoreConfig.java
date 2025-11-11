package com.zixi.easyaiagent.config;

import com.zixi.easyaiagent.rag.reader.MyMarkdownDocumentLoader;
import com.zixi.easyaiagent.rag.transformer.DocumentMetadataEnricher;
import com.zixi.easyaiagent.rag.transformer.MyTokenTextSplitter;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

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

    /**
     * VectorStore手动装配
     * 因为同时引入了dashscope和ollama2个embeddingModel，无法使用自动装配，所以手动创建VectorStore
     * @return pgVectorVectorStore
     */
    @Bean
    VectorStore loveAppVectorStore(JdbcTemplate jdbcTemplate,
                                   @Qualifier("dashscopeEmbeddingModel") EmbeddingModel embeddingModel) {
        // VectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build(); // 创建向量化存储（基于内存）
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(1536)                    // Optional: defaults to model dimensions or 1536
                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                     // Optional: defaults to HNSW
                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
                .build();
        // 本地知识库加载文档
        List<Document> documents = myMarkdownDocumentLoader.loadMarkdownDocuments();
        //自主切分文档
        List<Document> splitDocuments = myTokenTextSplitter.splitDocuments(documents);
        // 自动补充关键词元信息
        List<Document> enrichedDocuments = documentMetadataEnricher.enrichDocuments(splitDocuments, 5);
        // 向量化存储
        vectorStore.add(enrichedDocuments);
        return vectorStore;
    }

}
