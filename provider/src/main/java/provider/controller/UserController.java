package provider.controller;

import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import provider.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("save")
    public User save(@RequestBody User user){
       return userService.save(user);
    }
    @GetMapping("get")
    public User save(@RequestParam("id") Long id){
        return userService.get(id);
    }
}
