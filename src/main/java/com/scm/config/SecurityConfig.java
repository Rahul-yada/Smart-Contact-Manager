package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomerUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomerUserDetailService userDetailService;
    @Autowired
    private OAuthAuthenicationSuccessHandler handler;
    @Autowired
    private AuthFailureHandler authFailureHandler;
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // user detail service ka object:
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        // password encoder ka object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{


        // configuration  issse ham log link ko public batasakte hai
        httpSecurity.authorizeHttpRequests(authorize -> {
            // isss line se ham individual page protected kar sakte hai aur public kar sakte hai
        // authorize.requestMatchers("/home", "/register", "/service").permitAll();

        authorize.requestMatchers("/user/**").authenticated();
        authorize.anyRequest().permitAll();

        });
        //    this is default login with form 
        // inn sare method se login kee baad wala process handle hoga user khaha jaiga khaha redirect hoga login ke baad 
        httpSecurity.formLogin( formLogin ->{
              formLogin.loginPage("/login"); 
              formLogin.loginProcessingUrl("/authenticate");
              formLogin.defaultSuccessUrl("/user/profile", true);
              formLogin.usernameParameter("email");
              formLogin.passwordParameter("password");
              

             formLogin.failureHandler(authFailureHandler);


        });
       httpSecurity.csrf(AbstractHttpConfigurer::disable);
       httpSecurity.logout(logoutForm -> {
        logoutForm.logoutUrl("/do-logout");
       logoutForm.logoutSuccessUrl("/login?logout=true");
       });

    //   oauth configuration karenge yaha 
    httpSecurity.oauth2Login(oauth ->{
        oauth.loginPage("/login");
        oauth.successHandler(handler); 
    });


        return httpSecurity.build();


    }


    @Bean
    public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
  }


}


