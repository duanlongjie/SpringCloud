package consumer.controller;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserConsumerController {
    private static final Logger LOG = LoggerFactory.getLogger(UserConsumerController.class);
    private static final String rest_ful_prefix="http://localhost:8899";
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("save")
    public User save(@RequestBody User user){
        LOG.info(user.toString());
        return restTemplate.postForObject("http://provider/save",user,User.class);
    }
    @GetMapping("get")
    public User get(@RequestParam("id") Long id){
        LOG.info("id : {}",id);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",id);
        return restTemplate.getForObject("http://provider/get"
                +"?id={id}",User.class,paramMap);
    }


}
