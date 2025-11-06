package com.zixi.easyaiagent.demo.invoke;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.zixi.easyaiagent.demo.invoke.Constant.USER_PROMPT;


/**
 * SpringAIAlibaba
 * <a href="https://java2ai.com/docs/1.0.0.2/overview/">SpringAiAlibaba集成</a>
 */
@RestController
@RequestMapping("/springai")
public class SpringAiAiInvoke {

    private final ChatModel chatModel;

    public SpringAiAiInvoke(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
        System.out.println("chatModel.getDefaultOptions() = " + chatModel.getDefaultOptions());
    }

    /**
     * ChatClient 简单调用
     */
    @GetMapping("/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = USER_PROMPT) String query) {
        return chatModel.call(query);
    }
}
