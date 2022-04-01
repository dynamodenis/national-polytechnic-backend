package com.mabawa.nnpdairy.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

//@Configuration
//@EnableWebMvc
//public class FreeMarkerConfiguration {
//    @Bean
//    public FreeMarkerConfigurer freeMarkerConfigurer()
//    {
//        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
//        freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/views/");
//
//        return freeMarkerConfigurer;
//    }
//
//    @Bean
//    public FreeMarkerViewResolver freeMarkerViewResolver()
//    {
//        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
//        resolver.setCache(true);
//        resolver.setPrefix("");
//        resolver.setSuffix(".ftl");
//
//        return resolver;
//    }
//}
