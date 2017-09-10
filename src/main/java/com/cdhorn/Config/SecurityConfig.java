package com.cdhorn.Config;

import com.cdhorn.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    private String userQuery = "select username, password, active from user_data u where u.username = ?";
    private String roleQuery = "select u.username, r.name from user_data u inner join role r on u.role_id = r.id where u.username = ?";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(roleQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers("/map/**").hasRole("USER")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(loginSuccessHandler())
                    .failureHandler(loginFailureHandler())
//                    .loginPage("/login/admin")
//                    .successHandler(adminLoginSuccessHandler())
//                    .failureHandler(adminLoginFailureHandler())
                    .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/login");
    }

//    private AuthenticationSuccessHandler adminLoginSuccessHandler() {
//        return (request, response, authentication) -> response.sendRedirect("/admin");
//    }
//
//    private AuthenticationFailureHandler adminLoginFailureHandler() {
//        return (request, response, exception) -> {
//            request.getSession().setAttribute("error", "Unable to login with provided credentials");
//            response.sendRedirect("/admin/login");
//        };
//    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> response.sendRedirect("/user");
    }

    private AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            request.getSession().setAttribute("error", "Unable to login with provided credentials");
            response.sendRedirect("/login");
        };
    }

}
