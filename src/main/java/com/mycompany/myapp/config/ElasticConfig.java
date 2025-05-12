package com.mycompany.myapp.config;

import java.time.Duration;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.mycompany.myapp.repository")
public class ElasticConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        try {
            SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(null, (chain, authType) -> true).build();
            return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .usingSsl(sslContext)
                .withClientConfigurer((HttpAsyncClientBuilder clientBuilder) -> {
                    clientBuilder.setSSLContext(sslContext);
                    clientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
                    return clientBuilder;
                })
                .withBasicAuth("elastic", "123456")
                .withConnectTimeout(Duration.ofSeconds(30))
                .withSocketTimeout(Duration.ofSeconds(30))
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cấu hình client Elasticsearch", e);
        }
    }
}
