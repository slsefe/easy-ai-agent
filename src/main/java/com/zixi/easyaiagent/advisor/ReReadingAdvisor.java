package com.zixi.easyaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * Re-Reading Advisor
 * 用于提高LLM的推理能力
 * prompt增强为：
 * {Input_Query}
 * Read the question again: {Input_Query}
 * 支持：CallAroundAdvisor（非流式处理）、StreamAroundAdvisor（流式处理）
 */
@Slf4j
public class ReReadingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private static final String DEFAULT_RE2_ADVISE_TEMPLATE = """
            {re2_input_query}
            Read the question again: {re2_input_query}
            """;

    @NotNull
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 前置处理
        AdvisedRequest modifiedRequest = this.before(advisedRequest);
        // 调用链中的下一个advisor
        return chain.nextAroundCall(modifiedRequest);
    }

    private AdvisedRequest before(AdvisedRequest advisedRequest) {
        String userText = advisedRequest.userText();
        Map<String, Object> userParams = new HashMap<>(advisedRequest.userParams());
        userParams.put("re2_input_query", userText);
        return AdvisedRequest.from(advisedRequest)
                .userText(DEFAULT_RE2_ADVISE_TEMPLATE)
                .userParams(userParams)
                .build();
    }

    @NotNull
    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = before(advisedRequest);
        return chain.nextAroundStream(advisedRequest);
    }

    @NotNull
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 设置当前Advisor在调用链中的执行顺序，值越小优先级越高，越先执行。
     * @return
     */
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
