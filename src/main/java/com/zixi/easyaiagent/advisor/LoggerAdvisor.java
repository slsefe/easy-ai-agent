package com.zixi.easyaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

/**
 * 自定义日志advisor
 * 打印info级别日志、只输出单词用户提示词、AI回复内容
 * 支持：CallAroundAdvisor（非流式处理）、StreamAroundAdvisor（流式处理）
 */
@Slf4j
public class LoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    @NotNull
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 前置处理
        AdvisedRequest modifiedRequest = this.before(advisedRequest);
        // 调用链中的下一个advisor
        AdvisedResponse advisedResponse = chain.nextAroundCall(modifiedRequest);
        // 后置处理
        observeAfter(advisedResponse);
        return advisedResponse;
    }

    private AdvisedRequest before(AdvisedRequest advisedRequest) {
        log.info("Request: {}", advisedRequest.userText());
        return advisedRequest;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("Response: {}", advisedResponse.response().getResult().getOutput().getText());
    }

    @NotNull
    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = before(advisedRequest);

        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);

        return new MessageAggregator().aggregateAdvisedResponse(advisedResponses, this::observeAfter);
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
        return 100;
    }
}
