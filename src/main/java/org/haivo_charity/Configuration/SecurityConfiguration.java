package org.haivo_charity.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM account WHERE username = ? AND isDeleted = 0;")
                .authoritiesByUsernameQuery("SELECT username, roles FROM account WHERE username = ?;")
                .rolePrefix("ROLE_")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //khong can dang nhap van vao duoc
        http.authorizeRequests()
            .antMatchers("/", "/category/*","/all","/search",
                        "/categoryRest/**","/donateRest/**", "/volunteerRest/**",
                        "/VoteImageRest/**", "/voteRest/**", "/create_account/**")
            .permitAll();
        //dang nhap moi vao xem duoc
        http.authorizeRequests()
            .antMatchers("/account/**", "/loginManagement/**", "/proposalRest/**",
                    "/eventRest/**", "/event/**", "/donated/**")
            .authenticated();
        //Chi user moi vao duoc
        http.authorizeRequests()
                .antMatchers("/proposal/**")
                .access("hasRole('USER')");
        //tat ca cac duong dan co quyen admin moi xem duoc
        http.authorizeRequests()
            .antMatchers("/admin/**", "/proposal_admin/**", "/event_manager/**",
                    "/event_register_manager/**")
            .access("hasRole('ADMIN')");

        //Xu ly khi nguoi dung vao trang khong co quyen de vao
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/");
        
        http.authorizeRequests()
            .and().formLogin()
                .loginPage("/create_account/login")
                .loginProcessingUrl("/loginAccount").permitAll()
                .defaultSuccessUrl("/")
                .failureUrl("/create_account/login")
                .usernameParameter("username")
                .passwordParameter("password")
            .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");

        http.csrf().disable();
    }
}