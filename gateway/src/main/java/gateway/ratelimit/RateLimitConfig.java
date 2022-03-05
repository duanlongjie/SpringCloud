package gateway.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;


/**
 * 限流，对应在yml进行配置
 */
@Configuration
public class RateLimitConfig {
    private static final Logger LOG = LoggerFactory.getLogger(RateLimitConfig.class);

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange ->
                Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }

    @Bean
    KeyResolver apiKeyResolver() {
        return exchange ->
                Mono.just(exchange.getRequest().getPath().value());
    }

}
