package spring.attest.zuev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.attest.zuev.models.Person;
import spring.attest.zuev.models.Statuses;
import spring.attest.zuev.services.StatusesService;

@Component
public class StatusesValidator implements Validator {

    private final StatusesService statusesService;
@Autowired
    public StatusesValidator(StatusesService statusesService) {
        this.statusesService = statusesService;
    }

    @Override /** валидатор статусов заказа */
    public boolean supports(Class<?> clazz) {
        return Statuses.class.equals(clazz);
    }

    @Override
    /** ошибка повторенмя названий статусов заказа  */
    public void validate(Object target, Errors errors) {
        Statuses statuses = (Statuses) target; /**принимая тип Object down-кастим до Statuses*/
        if (statusesService.getStatusByName(statuses.getName()) != null) {
            errors.rejectValue("name", "", "Такой статус уже имеется");
        }
    }
}
