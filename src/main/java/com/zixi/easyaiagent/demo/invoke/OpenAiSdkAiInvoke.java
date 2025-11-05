package com.zixi.easyaiagent.demo.invoke;

// 该代码 OpenAI SDK 版本为 2.6.0
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import static com.zixi.easyaiagent.demo.invoke.Constant.ALIYUN_DASHSCOPE_BASE_URL;
import static com.zixi.easyaiagent.demo.invoke.Constant.DASHSCOPE_API_KEY;
import static com.zixi.easyaiagent.demo.invoke.Constant.MODEL_QWEN_PLUS;


/**
 * <a href="https://help.aliyun.com/zh/model-studio/qwen-api-reference#40f6d2c9d23u2">Open AI SDK接入</a>
 */
public class OpenAiSdkAiInvoke {



    public static void main(String[] args) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(System.getenv(DASHSCOPE_API_KEY))
                .baseUrl(ALIYUN_DASHSCOPE_BASE_URL)
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage("你是谁")
                .model(MODEL_QWEN_PLUS)
                .build();

        try {
            ChatCompletion chatCompletion = client.chat().completions().create(params);
            System.out.println(chatCompletion);
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
