package com.example.pricing_service.infraestructure.configuration;

/*import com.example.pricing_service.infraestructure.security.JwtRequestFilter;*/
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/v1/auth/**").permitAll()  // Endpoint abierto
                .anyRequest().authenticated()  // Todos los demás requieren autenticación
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // No sesiones

        // Añadir el filtro JWT antes de la autenticación
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}*/
