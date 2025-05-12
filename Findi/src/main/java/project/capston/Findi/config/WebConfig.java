package project.capston.Findi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //모든 api 경로에 대해
                .allowedOrigins("http://localhost:5173") // 이 origin에서 오는 요청만 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*");  // 모든 요청 헤더 허용
    }
}
