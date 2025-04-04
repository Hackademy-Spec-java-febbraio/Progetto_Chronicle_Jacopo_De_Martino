package it.aulab.aulabchronicle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    final private NotificationInterceptor notificationInterceptor;

    public WebConfig(NotificationInterceptor notificationInterceptor) {
        this.notificationInterceptor = notificationInterceptor;
    }
    
    @SuppressWarnings("null")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(notificationInterceptor);
    }
    
}
