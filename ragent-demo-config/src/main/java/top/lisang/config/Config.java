package top.lisang.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties props = new Properties();
    public static final String API_CHAT_URL;
    public static final String API_EMBEDDING_URL;
    public static final String API_KEY;
    public static final String EMBEDDING_MODEL;
    public static final String CHAT_MODEL;

    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("加载配置失败", e);
        }
        API_KEY = props.getProperty("api.key");
        API_CHAT_URL = props.getProperty("api.base-url") + "/chat/completions";
        API_EMBEDDING_URL = props.getProperty("api.base-url") + "/embeddings";
        EMBEDDING_MODEL = props.getProperty("api.embedding-model");
        CHAT_MODEL = props.getProperty("api.chat-model");
    }

}
