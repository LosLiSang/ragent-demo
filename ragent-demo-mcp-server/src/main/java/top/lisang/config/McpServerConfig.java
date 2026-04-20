package top.lisang.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import top.lisang.tools.EnterpriseTools;

@Configuration

public class McpServerConfig {

    @Bean
    public ToolCallbackProvider enterpriseToolProvider(EnterpriseTools enterpriseTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(enterpriseTools)
                .build();
    }
}
