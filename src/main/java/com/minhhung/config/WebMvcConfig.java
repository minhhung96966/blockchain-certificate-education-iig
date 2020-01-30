package com.minhhung.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		/*
		 * registry.addViewController("/home").setViewName("home");
		 * registry.addViewController("/").setViewName("home");
		 * registry.addViewController("/hello").setViewName("hello");
		 * registry.addViewController("/login").setViewName("login");
		 */
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/*
		 * registry.addResourceHandler("/error/**").addResourceLocations(
		 * "classpath:/static/");
		 * registry.addResourceHandler("/static/**").addResourceLocations(
		 * "classpath:/static/");
		 * registry.addResourceHandler("/css/**").addResourceLocations(
		 * "classpath:/static/css/");
		 * registry.addResourceHandler("/images/**").addResourceLocations(
		 * "classpath:/static/images/");
		 * registry.addResourceHandler("/js/**").addResourceLocations(
		 * "classpath:/static/js/");
		 */

	}
}
