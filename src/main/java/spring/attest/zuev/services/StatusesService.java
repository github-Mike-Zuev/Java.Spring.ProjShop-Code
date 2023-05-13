package spring.attest.zuev.services;

import jakarta.persistence.PrePersist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import spring.attest.zuev.models.Statuses;
import spring.attest.zuev.repositories.StatusesRepository;

import java.sql.Array;
import java.util.List;

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
        System.out.println("===> инициализация 'Статусов заказа'");
        String[] initStatuses = {"Принят", "Оформлен", "Ожидает", "Получен"};
        for (int index = 0; index < initStatuses.length; ++index) {
            Statuses newStatus = new Statuses();
            newStatus.setId(index+1);
            newStatus.setName(initStatuses[index]);
            this.statusesRepository.save(newStatus);
            System.out.println("new Status ===> "+this.statusesRepository.findByName(initStatuses[index]).getName());
        }
    }
    /** получение статуса по имени */
    public Statuses getStatusByName(String name){
        return statusesRepository.findByName(name);
    }
    /** передача всех статусов */
    public List<Statuses> getAllStatuses(){
        return statusesRepository.findAll();
    }


}
