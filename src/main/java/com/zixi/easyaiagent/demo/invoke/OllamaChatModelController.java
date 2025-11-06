package com.zixi.easyaiagent.demo.invoke;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.zixi.easyaiagent.demo.invoke.Constant.USER_PROMPT;

@RestController
@RequestMapping("/ollama")
@RequiredArgsConstructor
public class OllamaChatModelController {

    @Resource(name = "ollamaChatModel")
    private final ChatModel ollamaChatModel;

    @GetMapping("/chat")
    public String simpleChat() {
        return ollamaChatModel.call(USER_PROMPT);
    }
}
