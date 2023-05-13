package spring.attest.zuev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import spring.attest.zuev.services.PersonDetailsService;

@Configuration
public class SecurityConfig {
    private  final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    /** Настройка аутентификации */
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        authenticationManagerBuilder.userDetailsService(personDetailsService)
        // .passwordEncoder(getPasswordEncoder())/**шифрование паролей  (закоментировать при тестировании без шифрования) */
                ;}

    @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http  /** конфигурируем работу Spring Security (роли - без "ROLE_")*/
 /**.csrf().disable() //-временно - откл. защиты от межсайтовой подделки запросов*/
.authorizeHttpRequests() /** защита аутентификацией всех страниц */
.requestMatchers("/admin").hasRole("ADMIN") /** страницы url с /admin -доступны пользователю с ролью ROLE_ADMIN префикс 'ROLE_' -отбрасывается*/
.requestMatchers("/authentication", "/registration", "/error", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/product", "/product/info/{id}", "/product/search", "/backFromInfo").permitAll()/** в requestMatchers(,,,).permitAll() перечисляем страницы доступные с помощью permitAll не аутентифицированным пользователям */
/**.anyRequest().authenticated() //(заменено ролями) .anyRequest()-для остальных страниц - вызывать метод authenticated(), который открывает форму аутентификации */
 .anyRequest().hasAnyRole("USER", "ADMIN") /** доступ остальных страниц для ROLE_ADMIN и ROLE_USER*/
 .and() /** указываем что дальше настраиватеся аутентификация и соединяем ее с настройкой доступа */
.formLogin().loginPage("/authentication")/**переопределение базовой формы аут-ции и в loginPage передаём url запроса, он будет отправлятся при заходе на защищенные страницы */
.loginProcessingUrl("/process_login") /** указываем на какой адрес будут отправляться данные с формы. Нам уже не нужно будет создавать метод в контроллере и обрабатывать данные с формы. Мы задали url, который используется по умолчанию для обработки формы аутентификации по средствам Spring Security. Spring Security будет ждать объект с формы аутентификации и затем сверять логин и пароль с данными в БД */
.defaultSuccessUrl("/start page", true) /** ("/person account", true) /index /** Указываем на какой url необходимо направить пользователя после успешной аутентификации. Вторым аргументом указывается true чтобы перенаправление шло в любом случае после успешной аутентификации */
.failureUrl("/authentication?error") /** Указываем куда необходимо перенаправить пользователя при неудаче аутентификации.?error- в запрос будет передан объект error,(в качестве get-параетра) который будет проверятся на форме и при наличии данного объекта в запросе выводится сообщение "Неправильный логин или пароль"*/
.and()
.logout().logoutUrl("/logout").logoutSuccessUrl("/authentication");/** настройка выхода из аккаунта: logoutUrl -Url по обабртке выхода, logoutSuccessUrl - страница переадресации */
        return http.build(); }

    /** @Bean   public PasswordEncoder getPasswordEncode(){return NoOpPasswordEncoder.getInstance();} /** -без шифрования паролей - для тестирования */
    @Bean
    public PasswordEncoder getPasswordEncode(){return new BCryptPasswordEncoder();}

}
