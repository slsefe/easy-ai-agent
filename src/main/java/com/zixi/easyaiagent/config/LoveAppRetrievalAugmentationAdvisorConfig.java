package com.zixi.easyaiagent.config;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义RAG增强Advisor
 */
@Configuration
public class LoveAppRetrievalAugmentationAdvisorConfig {

    @Resource
    private DocumentRetriever loveAppDashScopeDocumentRetriever;

    @Resource
    private DocumentRetriever loveAppPgVectorDocumentRetriever;

    /**
     * 基于阿里云知识库实现RAG
     * @return RetrievalAugmentationAdvisor
     */
    @Bean
    public Advisor loveAppRagCloudAdvisor() {
        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers()
                .documentRetriever(loveAppDashScopeDocumentRetriever)
                // 允许模型在没有找到相关文档的情况下也生成回答
                .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                .build();
    }

    @Bean
    public Advisor loveAppRagCloudAdvisor2() {
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(loveAppPgVectorDocumentRetriever)
                // 允许模型在没有找到相关文档的情况下也生成回答
                .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                .build();
    }
}
