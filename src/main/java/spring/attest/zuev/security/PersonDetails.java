package spring.attest.zuev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.attest.zuev.models.Person;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {
    /* информация об авторизующемся пользователе. Реализуем все методы UserDetails и внедряем объект модели person : */
    private final Person person;
    @Autowired
    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {return Collections.singletonList(new SimpleGrantedAuthority(person.getRole())); /** Возвращает роль польз-ля -  лист из одного элемента */}

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getLogin();
    }

    // Аккаунт действует
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Акаунт не заблокирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Пароль действует
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Аккаунт активен
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Получить объект пользователя
    public Person getPerson(){
        return this.person;
    }

}
