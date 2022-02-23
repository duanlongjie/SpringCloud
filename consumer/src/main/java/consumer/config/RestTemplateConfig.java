//package consumer.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class RestTemplateConfig {
//    @Bean
//    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
//        return new RestTemplate(factory);
//    }
//
//    @Bean
//    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        // 调用 视频检索和图像检索的时候，设置的太小，报错
//        factory.setReadTimeout(500000);//单位为ms 500秒
//        factory.setConnectTimeout(500000);//单位为ms 500秒
//        return factory;
//    }
//}
