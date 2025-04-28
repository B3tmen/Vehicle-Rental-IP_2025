package org.unibl.etf.carrentalbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebMvc
@PropertySource("classpath:custom.properties")
public class WebConfig implements WebMvcConfigurer {
    @Value("${employees.frontend.url}")
    private String employeesFrontendUrl;
    @Value("${clients.frontend.url}")
    private String clientsFrontendUrl;
    @Value("${resources.static.file.path}")
    private String resourcesPath;

    // We have to enable Cross-Origin Resource Sharing (CORS) so the frontend and backend from different domains can communicate / so that front can send API requests to the back
    // (because the backend only accepts calls from itself by default)
    // Alternative: Make a proxy on the front, so this below isn't needed
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = getCorsConfiguration();

        source.registerCorsConfiguration("/**", config);    // Applied to all requests

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(-102);  // we put this bean at the lowest position, to be used before any Spring Security filter
        return bean;
    }

    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin(employeesFrontendUrl);               // React frontend
        config.addAllowedOrigin(clientsFrontendUrl);                 // JSP client app
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
        config.setAllowedMethods(Arrays.asList(
                "GET", "PUT", "POST", "DELETE"
        ));
        config.setMaxAge(3600L);    // 30min
        return config;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(resourcesPath + "images/");
    }
}
