package com.minhhung.config;

import com.minhhung.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    UserDetailsService customUserService(){
        return new CustomUserService();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserService());
        authProvider.setPasswordEncoder(new CustomPasswordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(customUserService());
        auth.authenticationProvider(authProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout().permitAll();
        */

        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/", "/home", "/verify", "/about","/demo","/download","/404","/403").permitAll()
                    //.antMatchers("/", "/home", "/about","/webjars/bootstrap/3.3.7/js/**","/webjars/bootstrap/3.3.7/css/**").permitAll()
                    .antMatchers("/student/**").hasAnyRole("STUDENT","ADMIN")
                    .antMatchers("/checker/**").hasAnyRole("CHECKER","ADMIN")
                    .antMatchers("/issuer/**").hasAnyRole("ISSUER","ADMIN")
                    .antMatchers("/hr/**").hasAnyRole("HR", "ADMIN")
                    .antMatchers("/admin/**").hasAnyRole("ADMIN")
                    //.antMatchers("/user/**").hasAnyRole("USER")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/perform_login")
                    .defaultSuccessUrl("/", true)
                    //.successHandler(myAuthenticationSuccessHandler())
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication().withUser("student").password("student").roles("student");
        auth.inMemoryAuthentication().withUser("checker").password("checker").roles("checker");
        auth.inMemoryAuthentication().withUser("issuer").password("issuer").roles("issuer");*/
       /* auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");*/
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**",  "/css/**", "/jslib/**","/js/**", "/img/**", "/images/**","/webjars/bootstrap/3.3.7/js/**","/webjars/bootstrap/3.3.7/css/**");
    }
}
