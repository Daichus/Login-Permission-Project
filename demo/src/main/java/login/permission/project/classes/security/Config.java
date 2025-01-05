package login.permission.project.classes.security;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class Config {

  @Autowired

    private final JwtService jwtService;
    private final EmployeeRepository employeeRepository;


    public Config(JwtService jwtService, EmployeeRepository employeeRepository) {
        this.jwtService = jwtService;
        this.employeeRepository = employeeRepository;
    }
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

//                        // 允許不需要驗證的端點
//                        // 登入和註冊不需要權限
                        .requestMatchers(
                                "/employee/test/login",
                                "/employee/test/add"   // 添加註冊端點
                        ).permitAll()

                        //測試 打開 Permission, Position 權限
                                .requestMatchers(
                                        "/Permission/test/get",
                                        "/Position/get",
                                "/status/test/get",
                                        "/employee/test/get",
                                        "/employee/test/verify",
                                        "/role/getAll"
                                ).permitAll()

                        // 部門管理相關權限
                        .requestMatchers(HttpMethod.GET, "/department/**").hasAuthority("dept_mgt_read")
                        .requestMatchers(HttpMethod.POST, "/department/**").hasAuthority("dept_mgt_create")
                        .requestMatchers(HttpMethod.PUT, "/department/**").hasAuthority("dept_mgt_update")
                        .requestMatchers(HttpMethod.DELETE, "/department/**").hasAuthority("dept_mgt_delete")

                        // 單位管理相關權限
                        .requestMatchers(HttpMethod.GET, "/unit/test/get").hasAuthority("unit_mgt_read")
                        .requestMatchers(HttpMethod.POST, "/unit/test/add").hasAuthority("unit_mgt_create")
                        .requestMatchers(HttpMethod.PUT, "/unit/test/edit").hasAuthority("unit_mgt_update")
                        .requestMatchers(HttpMethod.DELETE, "/unit/test/**").hasAuthority("unit_mgt_delete")


                        .requestMatchers(HttpMethod.GET, "/api/loginRecord/**").hasAnyAuthority(

                                "rec_login_read",
                                "rec_login_read_id",
                                "rec_login_read_dept",
                                "rec_login_read_unit"
                        )

                        .requestMatchers(HttpMethod.POST,"/role/create").hasAuthority("role_mgt_create")
                                .requestMatchers(HttpMethod.PUT,"/role/edit").hasAuthority("role_mgt_update")
                                .requestMatchers(HttpMethod.DELETE,"/role/delete").hasAuthority("role_mgt_delete")
                        .requestMatchers(HttpMethod.POST, "/api/loginRecord/**").hasAuthority("rec_login_create")
                        .requestMatchers(HttpMethod.PUT, "/api/loginRecord/**").hasAuthority("rec_login_update")
                        .requestMatchers(HttpMethod.DELETE, "/api/loginRecord/**").hasAuthority("rec_login_delete")
                                .requestMatchers(HttpMethod.GET,"/api/loginRecord/getLoginRecord/**").hasAuthority("rec_login_read_self")
                                // 其他請求需要認證
                                .anyRequest().authenticated()

//                        .anyRequest().denyAll()
                )
                .addFilterBefore(new JwtAuthFilter(jwtService, employeeRepository), UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//                .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }
}
