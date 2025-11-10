package com.zixi.easyaiagent.rag.query;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class QueryRewriter {

    private final QueryTransformer queryTransformer;

    public QueryRewriter(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        queryTransformer = RewriteQueryTransformer.builder().chatClientBuilder(builder).build();
    }

    /**
     * 执行query重写
     *
     * @param prompt 待重写的query
     * @return 重写后的query
     */
    public String doQueryRewrite(String prompt) {
        Query query = new Query(prompt);
        Query transformed = queryTransformer.transform(query);
        return transformed.text();
    }
}
