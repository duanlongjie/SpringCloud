package provider.service;

import entity.User;

public interface UserService {
    User save(User user);
    User get(Long id);
}
