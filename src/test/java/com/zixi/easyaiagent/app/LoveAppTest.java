package com.zixi.easyaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChatWithMemory() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员子奚";
        System.out.println("User: " + message);
        String answer = loveApp.doChat(message, chatId);
        System.out.println("Agent: " + answer);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想让另一半（编程导航）更爱我";
        System.out.println("User: " + message);
        answer = loveApp.doChat(message, chatId);
        System.out.println("Agent: " + answer);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        System.out.println("User: " + message);
        answer = loveApp.doChat(message, chatId);
        System.out.println("Agent: " + answer);
        Assertions.assertNotNull(answer);
    }
}
