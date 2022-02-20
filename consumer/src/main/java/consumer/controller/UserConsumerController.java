package consumer.controller;

import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserConsumerController {
    private static final String rest_ful_prefix="http://localhost:8899";
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("save")
    public User save(@RequestBody User user){
        return restTemplate.postForObject(rest_ful_prefix+"/save",user,User.class);
    }
    @GetMapping("get")
    public User save(@RequestParam("id") Long id){
        return restTemplate.getForObject(rest_ful_prefix+"/get/{id}",User.class,id);
    }


}
