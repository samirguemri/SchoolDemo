package edu.samir.schooldemo.config;

import edu.samir.schooldemo.config.converter.thymeleaf.StringToLocalDateConverter;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    //thymeleaf view configurations

    @Override
    public void addFormatters(FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
        registry.addFormatter(dateFormatter());
        registry.addConverter(dateConverter());
    }

    @Bean
    public DateFormatter dateFormatter() {
        return new DateFormatter();
    }

    @Bean
    public StringToLocalDateConverter dateConverter(){
        return new StringToLocalDateConverter();
    }


/*
    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/templates");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }
*/
}
