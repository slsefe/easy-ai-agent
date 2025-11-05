package com.zixi.easyaiagent.demo.invoke;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * <a href="https://java2ai.com/docs/1.0.0.2/overview/">SpringAiAlibaba集成</a>
 */
@RestController
@RequestMapping("/demo")
public class SpringAiAiInvoke {
    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    private final ChatClient dashScopeChatClient;

    public SpringAiAiInvoke(ChatClient.Builder chatClientBuilder) {
        this.dashScopeChatClient = chatClientBuilder
                .defaultSystem(DEFAULT_PROMPT)
                // 实现 Logger 的 Advisor
                .defaultAdvisors(new SimpleLoggerAdvisor())
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(DashScopeChatOptions.builder().withTopP(0.7).build())
                .build();
    }

    /**
     * ChatClient 简单调用
     */
    @GetMapping("/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query) {

        return dashScopeChatClient.prompt(query).call().content();
    }
}
