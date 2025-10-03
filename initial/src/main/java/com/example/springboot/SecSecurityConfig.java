package com.example.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.ldap.core.ContextSource;
import org.springframework.security.ldap.server.UnboundIdContainer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.http.HttpMethod;


//this is A spring boot configuration file
@Configuration
@EnableWebSecurity
public class SecSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login","/css/**", "/dba","/").permitAll() //requestMatchers allows /css as well otherwise css is not rendered
                        .requestMatchers(HttpMethod.POST, "/dba", "/").permitAll()
                        .anyRequest().fullyAuthenticated()


                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .csrf().disable()
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
   /*
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    ContextSource contextSource(UnboundIdContainer container) {
        return new DefaultSpringSecurityContextSource("ldaps://ldap.ext.national.com.au/OU=Applications,OU=NAB,OU=Groups,OU=Production");
    }

    @Bean
    AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserDnPatterns("uid={0},CN=AURDEV-Application-Qtxa-DevOps");
        return factory.createAuthenticationManager();
    }

    @Bean
    AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserSearchFilter("(uid={0}, uid={0},CN=AURDEV-Application-Qtxa-DevOps)");
        factory.setUserSearchBase("ou=people");
        return factory.createAuthenticationManager();
    }
    */
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception { //use ldap for logon authentication
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=devs,dc=ccnab,dc=com")
                .groupSearchBase("ou=devs,dc=ccnab,dc=com")
                .contextSource()
                .url("ldap://host.docker.internal")
                .and()
                .passwordCompare()
                //.passwordEncoder(new BCryptPasswordEncoder())  # point to improve, how to match ldappasswd encryption with spring boot
                .passwordAttribute("userPassword");
    }


}