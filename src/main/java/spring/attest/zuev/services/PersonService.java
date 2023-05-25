package spring.attest.zuev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import spring.attest.zuev.models.Person;
import spring.attest.zuev.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    /** возврат польз-ля по id  или NULL если не найден */
    public Person getPersonById(int personId){
        Optional<Person> person_opt = personRepository.findById(personId);
        return person_opt.orElse(null);
    }
    /** 1метод - проверка пользователя по логину. 2метод - сохранение польз-ля
     */
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }
/** 1 возврат польз-ля по логину или NULL если не найден */
    public Person findByLogin(Person person){
        Optional<Person> person_opt = personRepository.findByLogin(person.getLogin());
        return person_opt.orElse(null);
    }
/** 2 сохранение зарегистрированного польз-ля */
    /** проверка редактирования логина польз-ля одноимённой записи */
   public void validateLogin(Person person,  Errors errors){

      String loginPers =  person.getLogin();
      if (loginPers == null) { /** Логин не может быть пустым */
          errors.rejectValue("login", "", "Логин не может быть пустым");
      } else if (loginPers.length()<5) { /** Логин должен быть от 5 до 100 символов */
          errors.rejectValue("login", "", "Логин должен быть от 5 до 100 символов");
      } else if(findByLogin(person) != null){ /** проверка доступности логина */
           if(findByLogin(person).getId() != person.getId()) {/** проверка редактирования не одноимённой записи */
               System.out.println("==personService.hasLoginError==>ERROR");
               errors.rejectValue("login", "", "Данный логин уже занят");
           }
       }
   }
    @Transactional
    /** регистрация с ролъю пользователя */
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));/**.encode()возвращает значение хэша аргумента */
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

    public boolean noAdmins(){ /** Нет ни одного администратора" ROLE_ADMIN ROLE_USER*/
        if(!personRepository.existsByRoleIsLike("ROLE_ADMIN")) {
            System.out.println("Нет ни одного администратора");
            return  true;
        }return  false;
    }
    @Transactional
    /** регистрация в роли админа */
    public void registerAdmin (Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_ADMIN");
        personRepository.save(person);
    }

    /** передача всех пользователей */
    public List<Person> getAllPersons(){
       return personRepository.findAll();
    }

//    @Transactional
    @Modifying
            /** изменение данных польз-ля кроме пароля */
    public void changePerson(Person newPerson){
        Person oldPerson = personRepository.findById(newPerson.getId()).orElse(null);
        oldPerson.setLogin(newPerson.getLogin());
        oldPerson.setId(newPerson.getId());
        oldPerson.setRole(newPerson.getRole());
        personRepository.save(oldPerson);
    }

}
