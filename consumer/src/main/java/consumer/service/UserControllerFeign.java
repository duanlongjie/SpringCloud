package consumer.service;

import consumer.config.FeignConfiguration;
import consumer.fallbackFactory.UserControllerFeignFallbackFactory;
import entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 消费者通过feign调用生产者接口
 * 消费者调用生产者接口的演化过程
 * （1）原始RestTemplate调用
 * （2）注册Eureka之后，通过生产者http://application.name/url调用
 * （3）使用feign，直接通过接口调用生产者接口
 */
// provider-application 注册在eureka的生产者应用名 即spring.application.name
//fallback = UserControllerFeignFallback.class  Hystrix fallback 使用方式
//fallbackFactory = UserControllerFeignFallbackFactory.class Hystrix fallbackFactory 使用方式
@FeignClient(value = "provider-application",configuration = FeignConfiguration.class,
        fallbackFactory = UserControllerFeignFallbackFactory.class)
public interface UserControllerFeign {
    @GetMapping("get")
    User get(@RequestParam("id") Long id);

    @PostMapping("save")
    User save(@RequestBody User user);
}
