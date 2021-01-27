package com.swj.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swj.vhr.model.Hr;
import com.swj.vhr.model.RespBean;
import com.swj.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: swj
 * @Description:  安全配置类
 * @Date: Create in 11:19 2021/1/13
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private HrService hrService;


    /**hrService就是你自己写的类,
     * 这个类的作用就是去获取用户信息,比如从数据库中获取。
     * 这样的话,AuthenticationManager在认证用户身份信息的时候，
     * 就回从中获取用户身份,和从http中拿的用户身份做对比。*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //前端页面返回信息
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")
                .loginPage("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                            resp.setContentType("application/json:charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            Hr hr = (Hr) authentication.getPrincipal();
                            hr.setPassword(null);
                            RespBean.ok("登录成功！",hr);
                            String s = new ObjectMapper().writeValueAsString(hr);
                            out.write(s);
                            out.flush();
                            out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                            resp.setContentType("application/json:charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            RespBean error = RespBean.error("登录失败！");
                            if (e instanceof LockedException){
                                error.setMsg("账户被锁定,请联系管理员！");
                            }else if (e instanceof CredentialsExpiredException){
                                error.setMsg("密码已失效,请联系管理员！");
                            }else  if( e  instanceof AccountExpiredException){
                                error.setMsg("账号已失效，请联系管理员！");
                            }else  if( e  instanceof DisabledException){
                                error.setMsg("账号被禁用，请联系管理员！");
                            }else if(e instanceof BadCredentialsException){
                                error.setMsg("用户名或者密码错误,请稍后再试！");
                            }
                            out.write(new ObjectMapper().writeValueAsString(error));
                            out.flush();
                            out.close();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功！！")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .csrf().disable();
    }
}
