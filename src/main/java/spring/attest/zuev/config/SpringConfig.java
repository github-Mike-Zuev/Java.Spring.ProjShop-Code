package spring.attest.zuev.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
/** настройка дополнительного сканирования компонентов */
@ComponentScan("spring.attest.zuev.util")
public class SpringConfig {
}
