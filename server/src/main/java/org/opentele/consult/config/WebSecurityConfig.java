package org.opentele.consult.config;

import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.service.OrganisationUserService;
import org.opentele.consult.service.SecurityService;
import org.opentele.consult.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSession userSession;

    @Autowired
    private OrganisationUserService organisationUserService;

    private static final String usersQuery = "select * from (select concat(email,mobile) foo, password, true as active from users) x where x.foo = ?";
    private static final String privilegesQuery = "select * from (select concat(email,mobile) foo, 'ROLE_User' from users u) x where x.foo = ?";

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(privilegesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/static/**", "/api/message", "/api/login", "/api/test/open/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/organisation").permitAll();

        handleLogin(expressionInterceptUrlRegistry);
    }

    private void handleLogin(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) throws Exception {
        registry.anyRequest().authenticated().and().csrf().disable()
                .formLogin().loginPage("/api/login").successHandler((request, response, authentication) -> {
            User user = userService.getUser(request.getParameter("email"), request.getParameter("mobile"));
            OrganisationUser organisationUser = organisationUserService.getOrganisationUser(user);
            if (organisationUser != null && !UserType.User.equals(organisationUser.getUserType())) {
                SecurityService.elevateToRole(organisationUser.getUserType());
            }
            userSession.setCurrentUserId(user.getId());
            if (organisationUser != null)
                userSession.setCurrentOrganisationId(organisationUser.getOrganisation().getId());
            response.setStatus(HttpServletResponse.SC_OK);
            logger.info("Login Successful");
        }).failureHandler((request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.error("Login Failed", exception);
        })
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout")).addLogoutHandler((request, response, authentication) -> {
            request.getSession().invalidate();
            logger.info("User logout");
        })
                .and().exceptionHandling().authenticationEntryPoint(unauthenticatedRequestHandler());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UnauthenticatedRequestHandler unauthenticatedRequestHandler() {
        return new UnauthenticatedRequestHandler();
    }

    static class UnauthenticatedRequestHandler implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
            response.setStatus(403);
        }
    }
}
