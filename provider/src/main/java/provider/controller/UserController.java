package provider.controller;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import provider.service.UserService;

@RestController
public class UserController{
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("save")
    public User save(@RequestBody User user){
        LOG.info(user.toString());
       return userService.save(user);
    }
    @GetMapping("get")
    public User get(@RequestParam("id") Long id){
        LOG.info("id : {}",id);
        return userService.get(id);
    }
}
