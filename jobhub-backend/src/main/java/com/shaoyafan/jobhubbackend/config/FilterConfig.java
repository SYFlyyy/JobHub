package com.shaoyafan.jobhubbackend.config;

import com.shaoyafan.jobhubbackend.filter.SessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 过滤器配置
 *
 * @author SYF
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(new CorsFilter(source));
        registrationBean.setOrder(0); // 设置 CORS 过滤器的执行顺序为 0，确保在其他过滤器之前执行
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SessionFilter> sessionFilter() {
        FilterRegistrationBean<SessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SessionFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1); // 设置 SessionFilter 的执行顺序为 1，在 CORS 过滤器之后执行
        return registrationBean;
    }

}
