package com.zixi.easyaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

import static com.zixi.easyaiagent.demo.invoke.Constant.DASHSCOPE_API_KEY;
import static com.zixi.easyaiagent.demo.invoke.Constant.MODEL_QWEN_PLUS;

public class LangChainAiInvoke {

    public static void main(String[] args) {
        ChatLanguageModel model = QwenChatModel.builder()
                .apiKey(System.getenv(DASHSCOPE_API_KEY))
                .modelName(MODEL_QWEN_PLUS)
                .build();
        String answer = model.chat("请介绍一下你");
        System.out.println(answer);
    }
}

