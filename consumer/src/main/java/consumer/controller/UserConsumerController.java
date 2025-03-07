package consumer.controller;

import consumer.service.UserControllerFeign;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者的接口，暴露给消费者（让消费者调用）。而不对外暴露
 * 消费者的接口，可以暴露给客户端。如果用gateway的话，消费者的接口就暴露给gateway，就不对客户端暴露
 */
@RestController
public class UserConsumerController {
    private static final Logger LOG = LoggerFactory.getLogger(UserConsumerController.class);
    private static final String rest_ful_prefix = "http://localhost:8899";
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserControllerFeign userControllerFeign;

    @GetMapping("feign/get")
    public User feignGet(@RequestParam("id") Long id) {
        LOG.info("id : {}", id);
        return userControllerFeign.get(id);
    }
    @PostMapping("feign/save")
    public User feignSave(@RequestBody User user) {
        LOG.info(user.toString());
        return userControllerFeign.save(user);
    }

    @PostMapping("save")
    public User save(@RequestBody User user) {
        LOG.info(user.toString());
        return restTemplate.postForObject("http://provider-application/save", user, User.class);
    }

    @GetMapping("get")
    public User get(@RequestParam("id") Long id) {
        LOG.info("id : {}", id);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        return restTemplate.getForObject("http://provider-application/get"
                + "?id={id}", User.class, paramMap);
    }

    @Autowired
    private LoadBalancerClient loadBalancer;
    @GetMapping("/choose")
    public Object chooseUrl() {
        ServiceInstance instance = loadBalancer.choose("provider-application");
        return instance;
    }

}
