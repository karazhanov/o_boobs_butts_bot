import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.telegram.telegrambots.Constants.SOCKET_TIMEOUT;

/**
 * @author karazhanov on 24.10.17.
 */
public class ImageChoiser {

    private final CloseableHttpClient httpclient;
    private final RequestConfig requestConfig;
    private final ObjectMapper objectMapper;

    public ImageChoiser() {
        httpclient = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setConnectionTimeToLive(70, TimeUnit.SECONDS)
                .setMaxConnTotal(100)
                .build();
        requestConfig = RequestConfig.copy(RequestConfig.custom().build())
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(SOCKET_TIMEOUT).build();
        objectMapper = new ObjectMapper();
    }

    public String getRandomImage(ImageType imageType) throws IOException, TelegramApiValidationException {
        return imageType.getMedia() + getRandomImageUrl(imageType);
    }

    private String getRandomImageUrl(ImageType imageType) throws TelegramApiValidationException, IOException {
        HttpGet httpGet = configuredHttpGet(imageType.getApi());
        httpGet.addHeader("charset", StandardCharsets.UTF_8.name());
        String response = sendHttpGetRequest(httpGet);
        JsonNode jsonArray = objectMapper.readTree(response);
        return jsonArray.findValue("preview").asText();
    }

    private HttpGet configuredHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    private String sendHttpGetRequest(HttpGet httpGet) throws IOException {
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            return EntityUtils.toString(buf, StandardCharsets.UTF_8);
        }
    }
}
