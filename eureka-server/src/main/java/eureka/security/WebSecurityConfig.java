package eureka.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created time 2022/2/23 12:27
 * Eureka 自带了一个 Web 的管理页面，方便我
 * 们查询注册到上面的实例信息，但是有一个问
 * 题：如果在实际使用中，注册中心地址有公网
 * IP 的话，必然能直接访问到，这样是不安全的。
 * 所以我们需要对 Eureka 进行改造，加上权限认证来保证安全性。
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf
        http.csrf().disable();
        // 支持httpBasic
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }
}
