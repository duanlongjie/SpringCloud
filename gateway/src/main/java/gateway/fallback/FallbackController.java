package gateway.fallback;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

import java.util.Map;

/**
 * 熔断回退,当消费者，或生产者服务的接口不可用是，会走这里
 */
@RestController
public class FallbackController {
    private final Logger log = LoggerFactory.getLogger(FallbackController.class);
    @RequestMapping("/fallback")
    public String fallback(ServerWebExchange exchange, Throwable throwable) {
        log.info("throwable is null : " + String.valueOf(throwable == null));
        if (throwable != null) {
            log.info("throwable message : " + throwable.getMessage());
        }
        Map<String, Object> attributes =
                exchange.getAttributes();
        for (String key : attributes.keySet()) {
            log.info("key : " + key);
            log.info("value : " + attributes.get(key));
            Exception exception = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_PREDICATE_ROUTE_ATTR);
            ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
            log.error("接口调用失败，URL={}", delegate.getRequest().getURI(), exception);
            log.info("exception is null : " + String.valueOf(exception == null));
            if (exception instanceof HystrixTimeoutException) {
                log.info("接口调用超时");
            } else if (exception != null && exception.getMessage() != null) {
                log.info(exception.getMessage());
            } else {
                log.info("接口调用失败");
            }
        }
        return "服务暂时不可用";
    }
}
