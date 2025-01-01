package login.permission.project.classes.security;
import login.permission.project.classes.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class Config {

  @Autowired
  private JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
//                    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    config.setAllowedHeaders(List.of("*"));  // 允許所有 headers
                    config.setAllowCredentials(true);

                    return config;
                }))
                .authorizeHttpRequests(auth -> auth

                        // 允許不需要驗證的端點
                        .requestMatchers(
                      "/employee/test/login",
                               "/employee/test/add",
                               "/Permission/test/get",
                               "/Position/get",
                               "/status/test/get",
                               "/employee/test/getAllEmployeeInfo",
                               "/role/getAll"
                        ).permitAll()


                        // 部門管理相關權限
                        .requestMatchers(HttpMethod.GET, "/department/**").hasAuthority("dept_mgt_read")
                        .requestMatchers(HttpMethod.POST, "/department/**").hasAuthority("dept_mgt_create")
                        .requestMatchers(HttpMethod.PUT, "/department/**").hasAuthority("dept_mgt_update")
                        .requestMatchers(HttpMethod.DELETE, "/department/**").hasAuthority("dept_mgt_delete")

                        // 單位管理相關權限
                        .requestMatchers(HttpMethod.GET, "/unit/**").hasAuthority("unit_mgt_read")
                        .requestMatchers(HttpMethod.POST, "/unit/**").hasAuthority("unit_mgt_create")
                        .requestMatchers(HttpMethod.PUT, "/unit/**").hasAuthority("unit_mgt_update")
                        .requestMatchers(HttpMethod.DELETE, "/unit/**").hasAuthority("unit_mgt_delete")

                        // 登入記錄相關權限
                        .requestMatchers(HttpMethod.GET, "/loginRecord/**").hasAnyAuthority(
                                "login_rec_read",
                                "login_rec_read_id",
                                "login_rec_read_dept",
                                "login_rec_read_unit"
                        )
                        .requestMatchers(HttpMethod.POST, "/loginRecord/**").hasAuthority("login_rec_create")
                        .requestMatchers(HttpMethod.PUT, "/loginRecord/**").hasAuthority("login_rec_update")
                        .requestMatchers(HttpMethod.DELETE, "/loginRecord/**").hasAuthority("login_rec_delete")
                        .requestMatchers(HttpMethod.POST, "/role/**").hasAuthority("role_mgt_create")

                                // 其他請求需要認證
                                .anyRequest().authenticated()

//                        .anyRequest().denyAll()
                )

                .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//                .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }
}
