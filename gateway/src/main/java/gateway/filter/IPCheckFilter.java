package gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局过滤器作用于所有的路由，不需要单独配置，我们可以用它来实现很多统一化处理的业务需求，比如权限认证、IP 访问限制等
 * 限制访问ip，可以做到控制哪些ip可以访问接口，哪些ip不可以访问接口。（ip白名单功能）
 */
@Component
public class IPCheckFilter implements GlobalFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(IPCheckFilter.class);
    /**
     * 通过 @Order 来指定执行的顺序，数字越小，优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        ServerHttpRequest request = exchange.getRequest();
        String clientIp = getClientIp(request);
        LOG.info("clientIp : {}",clientIp);
        // 此处写得非常绝对, 只作演示用, 实际中需要采取配置的方式
        if (clientIp.equals("127.0.0.1")) {
            ServerHttpResponse response = exchange.getResponse();
            Map<String,Object> data = new HashMap<>();
            data.put("code",401);
            data.put("message","限制该ip的请求非法请求");
            ObjectMapper mapper = new ObjectMapper();
            byte[] datas = new byte[0];
            try {
                datas = mapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            DataBuffer buffer = response.bufferFactory().wrap(datas);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }

    private static final String LOCALHOST = "127.0.0.1";
    private static final String SEPARATOR = ",";
    private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";
    private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    // 这里从请求头中获取用户的实际IP,根据Nginx转发的请求头获取
    private String getClientIp(ServerHttpRequest serverHttpRequest) {
        String ipAddress;
        try {
            // 1.根据常见的代理服务器转发的请求ip存放协议，从请求头获取原始请求ip。值类似于203.98.182.163, 203.98.182.163
            ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_X_FORWARDED_FOR);
            if ("".equals(ipAddress) ||"unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_PROXY_CLIENT_IP);
            }
            if ("".equals(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = serverHttpRequest.getHeaders().getFirst(HEADER_WL_PROXY_CLIENT_IP);
            }

            // 2.如果没有转发的ip，则取当前通信的请求端的ip
            if ("".equals(ipAddress) ||"unknown".equalsIgnoreCase(ipAddress)) {
                InetSocketAddress inetSocketAddress = serverHttpRequest.getRemoteAddress();
                if(inetSocketAddress != null) {
                    ipAddress = inetSocketAddress.getAddress().getHostAddress();
                }
                // 如果是127.0.0.1，则取本地真实ip
                if (LOCALHOST.equals(ipAddress)) {
                    InetSocketAddress remoteAddress = serverHttpRequest.getRemoteAddress();
                    if(remoteAddress.getAddress() != null) {
                        ipAddress = remoteAddress.getAddress().getHostAddress();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***"
            if (ipAddress != null) {
                ipAddress = ipAddress.split(SEPARATOR)[0].trim();
            }
        } catch (Exception e) {
            LOG.error("解析请求IP失败", e);
            ipAddress = "";
        }
        return ipAddress == null ? "" : ipAddress;
    }

}
