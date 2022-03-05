package consumer.fallbackFactory;

import consumer.service.UserControllerFeign;
import entity.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserControllerFeignFallbackFactory implements FallbackFactory<UserControllerFeign> {
    private final org.slf4j.Logger LOG = LoggerFactory.getLogger(getClass());
    @Override
    public UserControllerFeign create(Throwable throwable) {
        LOG.error("fallbackFactory方式");
        LOG.error(throwable.getMessage(), throwable);
        return new UserControllerFeign(){
            @Override
            public User get(Long id) {
                return new User(null,"fallbackName","fallbackGender","fallbackPhone",1);
            }

            @Override
            public User save(User user) {
                return new User(null,"fallbackName","fallbackGender","fallbackPhone",1);
            }
        };
    }
}
