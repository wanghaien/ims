package com.en.ims.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/lib/**","**/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable()
            .and().csrf().disable()
            .authorizeRequests()//对授权请求进行配置
            .anyRequest().authenticated()
            .and().formLogin()//表单登录 UsernamePasswordAuthenticationFilter
            .loginPage("/login").failureUrl("/loginError").permitAll().successHandler(loginSuccessHandler())//登录成功以后要用我们自定义的登录成功处理器
            .and().logout().permitAll().invalidateHttpSession(true)
            .deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler())
            .and().sessionManagement().maximumSessions(1).expiredUrl("/login");
    }
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler() { //登入处理
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                User userDetails = (User) authentication.getPrincipal();
                logger.info("用户:" + userDetails.getUsername() + "登录成功!");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() { //登出处理
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.sendRedirect("/login");
            }
        };
    }
}
