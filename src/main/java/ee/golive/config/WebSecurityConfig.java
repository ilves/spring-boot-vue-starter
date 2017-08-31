package ee.golive.config;

import ee.golive.security.JWTAuthenticationFilter;
import ee.golive.security.JWTLoginFilter;
import ee.golive.security.MyAccessDeniedHandler;
import ee.golive.security.RestAuthenticationEntryPoint;
import ee.golive.services.UserServiceImpl;
import ee.golive.util.MyRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserDetailsService userService;

  @Autowired
  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new MyAccessDeniedHandler();
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new MyAccessDeniedHandler();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
      .antMatchers("/", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html").permitAll()
      .antMatchers( "/webjars/**").permitAll()
      .antMatchers(HttpMethod.POST, "/api/account/login").permitAll()
      .anyRequest().authenticated().and()
      .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(accessDeniedHandler()).and()
      .addFilterBefore(new MyRequestFilter(), UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(new JWTAuthenticationFilter((UserServiceImpl) userService),
        UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userService);
    authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
    auth.authenticationProvider(authenticationProvider);
  }
}
