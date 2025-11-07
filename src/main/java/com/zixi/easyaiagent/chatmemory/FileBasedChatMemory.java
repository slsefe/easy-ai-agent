package com.zixi.easyaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jetbrains.annotations.NotNull;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileBasedChatMemory implements ChatMemory {

    private final String BASE_DIR;

    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public FileBasedChatMemory(String baseDir) {
        BASE_DIR = baseDir;
        File file = new File(BASE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        // 读取文件已存在的消息
        List<Message> existedMessages = getMessages(conversationId);
        // 将新的消息追加写在原来的消息后面
        existedMessages.addAll(messages);
        // 保存消息
        saveMessages(conversationId, existedMessages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> all = getMessages(conversationId);
        return all != null ? all.stream().skip(Math.max(0, all.size() - lastN)).toList() : List.of();
    }

    @Override
    public void clear(String conversationId) {
        File file = getFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    private void saveMessages(String conversationId, List<Message> existedMessages) {
        File file = getFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, existedMessages);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Message> getMessages(String conversationId) {
        File file = getFile(conversationId);
        // 如果文件已存在，读取已存在的消息
        List<Message> existedMessages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                existedMessages = kryo.readObject(input, ArrayList.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return existedMessages;
    }

    @NotNull
    private File getFile(String conversationId) {
        // 创建文件
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}
