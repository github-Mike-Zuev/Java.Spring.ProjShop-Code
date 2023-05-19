package spring.attest.zuev.services;

import jakarta.persistence.PrePersist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.attest.zuev.models.Category;
import spring.attest.zuev.models.Statuses;
import spring.attest.zuev.repositories.StatusesRepository;

import java.sql.Array;
import java.util.List;
import java.util.Optional;

@Service
public class StatusesService {
  private final StatusesRepository statusesRepository;

    public StatusesService(StatusesRepository statusesRepository) {
        this.statusesRepository = statusesRepository;
        if (statusesRepository.count() == 0) { /** заполнение полей при создании объекта */
            initStatuses();
        }
    }
    @Modifying
    /** заполнение полей при создании объекта - пока нет формы администратора для редактир-я*/
    public void initStatuses(){
        /** Статус заказа "Принят" - по умолчанию - начальный */
        System.out.println("===> инициализация 'Статусов заказа'");
        String[] initStatuses = {"Принят", "Оформлен", "Ожидает", "Выдан"};
        for (int index = 0; index < initStatuses.length; ++index) {
            Statuses newStatus = new Statuses();
            newStatus.setId(index+1);
            newStatus.setName(initStatuses[index]);
            this.statusesRepository.save(newStatus);
            System.out.println("new Status ===> "+this.statusesRepository.findByName(initStatuses[index]).getName());
        }
    }
    /** получение статуса по id */
    public Statuses getStatusesById (int id){
        Optional<Statuses> status = statusesRepository.findById(id);
        return status.orElse(null);
    }
    /** получение статуса по имени */
    public Statuses getStatusByName(String name){
        return statusesRepository.findByName(name);
    }
    /** передача всех статусов */
    public List<Statuses> getAllStatuses(){
        return statusesRepository.findAll();
    }
    @Transactional // @Modifying
    /** Редактирование статусов заказа (схоже с editCategory) new, del, edit */
    public void editStatuses(String operation, int id, Statuses newStatus) {

        switch (operation) {
            case "del":
                if (!newStatus.getName().equals("Принят")) {
                    /** не разрешается удалять начальный статус заказа "Принят" */

                    /** не удалять если в заказах имеется удаляемый статус **/
                    statusesRepository.delete(newStatus);
//                if (this.haveChildren(category)) return "Нельзя удалить категорию имеющую товары";
//                categoryRepository.delete(category);
//                return "Категория удалена";
                }
                break;
            case "edit":
                statusesRepository.save(newStatus);
//                category.setName(newCategory.getName());
//                categoryRepository.save(category);
                break;
                }
                return;


        }
        /** ниже удалить*/
//    @Transactional
//    public String editCategory (String operation, Category category, Category newCategory) {
//        if (operation.equals("new")) {
//            newCategory.setPersonOwner(currentPerson());
//            categoryRepository.save(newCategory);
//            return "Категория создана";
//        }/* изменение/удаление не админом и не создателем категории */
//        if(!isAdmin() && (!category.getPersonOwner().equals(currentPerson()) )) return "Нет прав для изменения/удаления.";
//        switch (operation) {
//            case "del":
//                /**  разрешается удалять последний элемент// if () return "Нельзя удалять  единственный элемент"; */
//                /** не удалять если в категории есть товары **/
//                if (this.haveChildren(category)) return "Нельзя удалить категорию имеющую товары";
//                categoryRepository.delete(category);
//                return "Категория удалена";
//            case "edit": category.setName(newCategory.getName());
//                categoryRepository.save(category);
//                return "Категория изменена";
//        }
//        return "Не выполнено";
//    }


}
