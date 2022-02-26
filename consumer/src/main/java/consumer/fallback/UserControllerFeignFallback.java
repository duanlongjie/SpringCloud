package consumer.fallback;

import consumer.service.UserControllerFeign;
import entity.User;
import org.springframework.stereotype.Component;

/**
 * hystrix fallback方式
 */
@Component
public class UserControllerFeignFallback implements UserControllerFeign {
    @Override
    public User get(Long id) {
        return new User(null,"fallbackName","fallbackGender","fallbackPhone",1);
    }
}
