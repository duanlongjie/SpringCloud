package consumer.service;

import consumer.config.FeignConfiguration;
import consumer.fallbackFactory.UserControllerFeignFallbackFactory;
import entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
// provider-application 注册在eureka的生产者应用名 即spring.application.name
//fallback = UserControllerFeignFallback.class  Hystrix fallback 使用方式
//fallbackFactory = UserControllerFeignFallbackFactory.class Hystrix fallbackFactory 使用方式
@FeignClient(value = "provider-application",configuration = FeignConfiguration.class,
        fallbackFactory = UserControllerFeignFallbackFactory.class)
public interface UserControllerFeign {
    @GetMapping("get")
    User get(@RequestParam("id") Long id);
}
