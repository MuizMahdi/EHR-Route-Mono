package com.project.EhrRoute.Configuration;
import com.project.EhrRoute.Security.AppUserDetailsService;
import com.project.EhrRoute.Security.JwtAuthenticationEntryPoint;
import com.project.EhrRoute.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, jsr250Enabled=true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    // ### AutoWiring ### //
    private AppUserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter authenticationFilter, AppUserDetailsService userDetailsService, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    // ### Beans ### //
    /*
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    // ### Matchers ### //
    // Matchers available for public (permitAll)
    private String[] Public_Matchers = {
            "/",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/auth/**",
            "/chain/**",
            "/node/**",
            "/transaction/**",
            "/users/**",
            "/network/**",
            "/cluster/**"
    };

    // Matchers available for public via HTTP GET
    private String[] GET_Public_Matchers = {
            "/auth/**",
            "/chain/**",
            "/node/**",
            "/transaction/**",
            "/users/**",
            "/network/**",
            "/cluster/**"
    };



    // ### Overridden configure methods ### //
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
        .cors()
        .and()
        .csrf().disable()
        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests().antMatchers(Public_Matchers).permitAll()
        .antMatchers(HttpMethod.GET, GET_Public_Matchers).permitAll()
        .anyRequest().authenticated();

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
