package com.zixi.easyaiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.zixi.easyaiagent.demo.invoke.Constant.DASHSCOPE_API_KEY;
import static com.zixi.easyaiagent.demo.invoke.Constant.MODEL_QWEN_PLUS;
import static com.zixi.easyaiagent.demo.invoke.Constant.SYSTEM_PROMPT;
import static com.zixi.easyaiagent.demo.invoke.Constant.USER_PROMPT;

/**
 * <a href="https://help.aliyun.com/zh/model-studio/qwen-api-reference#9141263b961cc">HTTP REST API 集成</a>
 * 对于SDK不支持的语言，或者需要灵活控制的场景，可以直接使用HTTP请求调用大模型的API
 * 如果有官方SDK支持，优先使用SDK集成：
 */
public class HttpAiInvoke {
    public static void main(String[] args) {
        // 替换为你的实际 API 密钥
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 设置请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + System.getenv(DASHSCOPE_API_KEY));
        headers.put("Content-Type", "application/json");

        // 设置请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL_QWEN_PLUS);

        JSONObject input = new JSONObject();
        JSONObject[] messages = new JSONObject[2];

        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", SYSTEM_PROMPT);
        messages[0] = systemMessage;

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", USER_PROMPT);
        messages[1] = userMessage;

        input.put("messages", messages);
        requestBody.put("input", input);

        JSONObject parameters = new JSONObject();
        parameters.put("result_format", "message");
        requestBody.put("parameters", parameters);

        // 发送请求
        HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .body(requestBody.toString())
                .execute();

        // 处理响应
        if (response.isOk()) {
            System.out.println("请求成功，响应内容：");
            System.out.println(response.body());
        } else {
            System.out.println("请求失败，状态码：" + response.getStatus());
            System.out.println("响应内容：" + response.body());
        }
    }
}

