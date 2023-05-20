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
//    public List<Statuses> getAllStatusesByName(String name){
//        return statusesRepository.List<Statuses> getAllByName(String name);
//    }
    /** передача всех статусов */
    public List<Statuses> getAllStatuses(){
        return statusesRepository.findAll();
    }
    @Transactional // @Modifying
    /** Редактирование статусов заказа (схоже с editCategory) new, del, edit */
    public String editStatuses(String operation, Statuses newStatus) {

        switch (operation) {
            case "del":
                /**  if (newStatus.getName().equals("Принят")) {- перенесено в detailedValidate
                   не разрешается удалять начальный статус заказа "Принят"
                    return "не разрешается удалять начальный статус заказа \"Принят\"";}*/
               statusesRepository.delete(newStatus);
                break;
            case "edit":
//   /** проверка дублирования при переименовании - перенесено в detailedValidate*/
                    statusesRepository.save(newStatus);
                break;
            case "new":
                Statuses status = new Statuses(newStatus.getName(), newStatus.getStatusDescription());
                statusesRepository.save(status);
                break;
                }
                return "no errors";
        }
        /** ниже удалить*/

}
