package com.zixi.easyaiagent.rag.preretrieval;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.expansion.QueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 预检索：优化用户查询，提高后续检索的质量
 */
@Component
public class QueryRewriter {

    private final ChatModel chatModel;

    public QueryRewriter(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 查询重写
     * 使用LLM对用户的原始查询进行改写，使其更加清晰和详细。
     * 当用户查询含糊不清或者包含无关信息时，非常有效
     *
     * @param query 用户查询
     * @return 重写后的query
     */
    public Query doQueryRewrite(Query query) {
        QueryTransformer queryRewriter = RewriteQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(chatModel))
//                .promptTemplate("") // 自定义prompt
                .build();
        return queryRewriter.transform(query);
    }

    /**
     * 查询翻译
     * 将查询翻译为嵌入模型支持的目标语言。如果查询已经是目标语言，则保持不变。
     * 对于嵌入模型是针对特定语言训练，而用户查询使用不同语言的情况非常有用，便于实现国际化。
     *
     * @param query 用户查询
     * @return 翻译后的查询
     */
    public Query doQueryTranslate(Query query) {
        QueryTransformer queryTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(chatModel))
                .targetLanguage("english")
//                .promptTemplate("") // 自定义prompt
                .build();
        return queryTransformer.transform(query);
    }

    /**
     * 查询压缩
     * 使用LLM将对话历史和后续查询压缩成一个独立的查询，类似于概括总结。
     * 适用于对话历史较长、且后续查询与对话上下文相关的场景
     *
     * @param query 用户查询
     * @return 总结之后的查询
     */
    public Query doQueryCompression(Query query) {
        QueryTransformer queryTransformer = CompressionQueryTransformer.builder()
                .chatClientBuilder(ChatClient.builder(chatModel))
//                .promptTemplate("") // 自定义prompt
                .build();
        return queryTransformer.transform(query);
    }

    /**
     * 多查询扩展
     * 使用LLM将一个查询扩展为多个语义上不同的变体，有助于检索额外的上下文信息，并增加找到相关结果的机会。
     *
     * @param query 用户查询
     * @return 扩展后的查询列表
     */
    public List<Query> doQueryExpansion(Query query) {
        QueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(ChatClient.builder(chatModel))
                .numberOfQueries(3)
                .includeOriginal(true)
                .build();
        return queryExpander.expand(query);
    }
}
