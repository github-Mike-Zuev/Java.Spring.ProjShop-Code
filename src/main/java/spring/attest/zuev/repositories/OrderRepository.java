package spring.attest.zuev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.attest.zuev.models.Order;
import spring.attest.zuev.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    /** заказ пользователя */
    List<Order> findByPerson(Person person);
    /** поиск по окончанию номера-названия String number */
    List<Order> findAllByNumberEndingWith(String endNumbers);

    /** передача количества заказов(уникальных номеров) клиента*/
@Query(value ="select count(distinct number) from orders  where orders.person_id = ?1" ,nativeQuery = true)
int totalNumbersOrder(int personId);

}
