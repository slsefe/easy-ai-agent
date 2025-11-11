package com.zixi.easyaiagent;

import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {PgVectorStoreAutoConfiguration.class})
public class EasyAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyAiAgentApplication.class, args);
    }

}
