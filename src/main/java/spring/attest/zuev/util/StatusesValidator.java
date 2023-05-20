package spring.attest.zuev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.attest.zuev.models.Order;
import spring.attest.zuev.models.Statuses;
import spring.attest.zuev.services.OrderService;
import spring.attest.zuev.services.StatusesService;

@Component
public class StatusesValidator implements Validator {

    private final StatusesService statusesService;
    private final OrderService orderService;
@Autowired
    public StatusesValidator(StatusesService statusesService, OrderService orderService) {
        this.statusesService = statusesService;
        this.orderService = orderService;
    }

    @Override /** валидатор статусов заказа */
    public boolean supports(Class<?> clazz) {
        return Statuses.class.equals(clazz);
    }

    @Override
    /** ошибка повторенмя названий статусов заказа  */
    public void validate(Object target, Errors errors) {
        Statuses statuses = (Statuses) target; /**принимая тип Object down-кастим до Statuses*/
        Statuses duplicate = statusesService.getStatusByName(statuses.getName());
        if ( duplicate != null && duplicate.getId() != statuses.getId()) {
            errors.rejectValue("name", "", "Такой статус уже имеется");
        }
    }
    public void detailedValidate(Object target, Errors errors, String operation){
        Statuses statuses = (Statuses) target; /**принимая тип Object down-кастим до Statuses*/
        Statuses duplicate = statusesService.getStatusByName(statuses.getName());
        int chosenStatusId = statuses.getId();
//        if ( duplicate != null && duplicate.getId() != statuses.getId()) {
//            errors.rejectValue("name", "", "Такой статус уже имеется");
//        }
        switch (operation) {
            case "del":
                if (statuses.getName().equals("Принят")) {
                    /** не разрешается удалять начальный статус заказа "Принят" */
                    errors.rejectValue("name", "", "не разрешается удалять начальный статус заказа \"Принят\"");
                }
                /** не удалять если в заказах имеется удаляемый статус **/
                for ( Order order : orderService.getAllOrders()){ /** проверка наличия по id */
                    if ( order.getStatus().getId() == chosenStatusId){
                        //System.out.println(order.getStatus().getName()+"==Удаление==> " + statuses.getName()); //тест
                        errors.rejectValue("name","","Удаляемый статус ещё используется в заказах");
                        break;
                    }
                    //System.out.println(order.getStatus().getName()+"==Перебор==> " + statuses.getName()); //тест
                }
                break;
            case "edit":
               /**  проверка дублирования имён при переименовании  */
                if ( duplicate != null && duplicate.getId() != statuses.getId()) {
                    errors.rejectValue("name", "", "Такой статус уже имеется");
                }
                break;
            case "new":
                /**  проверка дублирования имён при добавлении */
                if ( duplicate != null ) {
                    errors.rejectValue("name", "", "Такой статус уже есть!!");
                }
                break;
        }

    }
}
