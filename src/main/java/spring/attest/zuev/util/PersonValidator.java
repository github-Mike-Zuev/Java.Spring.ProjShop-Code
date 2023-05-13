package spring.attest.zuev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.attest.zuev.models.Person;
import spring.attest.zuev.services.PersonService;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    // В методе supports указываем для какой модели предназначен этот валидатор
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    // В методе validate - прописываем правила валидации
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person)target; /**принимая тип Object down-кастим до Person*/
        if(personService.findByLogin(person) != null){
            errors.rejectValue("login", "", "Данный логин уже занят");
        }
    }
}
