package top.lisang;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import top.lisang.config.Config;

public class EmbeddingClient {

    private static final String API_URL = Config.API_EMBEDDING_URL;
    private static final String API_KEY = Config.API_KEY;
    private static final String EMBEDDING_MODEL = Config.EMBEDDING_MODEL;

    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public EmbeddingClient() {
        this.apiKey = API_KEY;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 将一组文本转成向量
     *
     * @param texts 要向量化的文本列表
     * @return 每段文本对应的向量（double 数组）
     */
    public List<double[]> embed(List<String> texts) throws Exception {
        // 构造请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", EMBEDDING_MODEL);
        requestBody.put("input", texts);
        requestBody.put("encoding_format", "float");

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        // 发送 HTTP 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API 调用失败，状态码：" + response.statusCode()
                    + "，响应：" + response.body());
        }

        // 解析响应，提取向量
        JsonNode root = objectMapper.readTree(response.body());
        JsonNode dataArray = root.get("data");

        List<double[]> embeddings = new ArrayList<>();
        for (JsonNode item : dataArray) {
            JsonNode embeddingNode = item.get("embedding");
            double[] vector = new double[embeddingNode.size()];
            for (int i = 0; i < embeddingNode.size(); i++) {
                vector[i] = embeddingNode.get(i).asDouble();
            }
            embeddings.add(vector);
        }

        return embeddings;
    }

    /**
     * 将单段文本转成向量（便捷方法）
     */
    public double[] embed(String text) throws Exception {
        return embed(List.of(text)).get(0);
    }

    public List<double[]> embedInBatches(List<String> texts, int batchSize) throws Exception {
        List<double[]> allEmbeddings = new ArrayList<>();

        for (int i = 0; i < texts.size(); i += batchSize) {
            int end = Math.min(i + batchSize, texts.size());
            List<String> batch = texts.subList(i, end);

            System.out.printf("向量化进度：%d/%d%n", end, texts.size());
            List<double[]> batchEmbeddings = embed(batch);
            allEmbeddings.addAll(batchEmbeddings);

            // 简单的限流：每批之间等一下，避免触发 API 的速率限制
            if (end < texts.size()) {
                Thread.sleep(200);
            }
        }

        return allEmbeddings;
    }

    public static void main(String[] args) throws Exception {
        EmbeddingClient client = new EmbeddingClient();

        String query = "我是个SB";

        double[] embed = client.embed(query);

        System.out.println(embed.length);

    }
}