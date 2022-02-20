package provider.service.impl;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import provider.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private static List<User> users =new ArrayList<>();
    private static Long userid=0L;
    @Override
    public User save(User user) {
        userid++;
        user.setId(userid);
        users.add(user);
        return user;
    }

    @Override
    public User get(Long id) {
        List<User> collect = users.stream().filter(user -> user.getId() == id).collect(Collectors.toList());
        return collect.get(0);
    }
}
