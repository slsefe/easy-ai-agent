package com.zixi.easyaiagent.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentRetrievalConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;

    @Resource
    private VectorStore pgVectorVectorStore;

    /**
     * 基于阿里云dashscope云知识库的文档检索
     *
     * @return DocumentRetriever
     */
    @Bean
    public DocumentRetriever loveAppDashScopeDocumentRetriever() {
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        final String KNOWLEDGE_INDEX = "恋爱大师"; // 知识库名称
        return new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder().withIndexName(KNOWLEDGE_INDEX).build());
    }

    /**
     * 基于PgVector的VectorStore文档检索
     *
     * @return VectorStoreDocumentRetriever
     */
    @Bean
    public DocumentRetriever loveAppPgVectorDocumentRetriever() {
        return VectorStoreDocumentRetriever.builder()
                .similarityThreshold(0.50)
                .vectorStore(pgVectorVectorStore)
                .topK(5)
                .build();
    }
}
