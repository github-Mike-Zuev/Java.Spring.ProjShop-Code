package spring.attest.zuev.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.attest.zuev.models.*;
import spring.attest.zuev.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    /** получение всех заказов  */
   public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    /** получение заказа по id  */
    public Optional<Order> getOrderById (int id){
        return orderRepository.findById(id);
    }
    /** получение заказов по пользователю */
   public List<Order> getPersonOrders(Person person){
      return orderRepository.findByPerson(person);
    }

    @Transactional
    /**  редактирование заказов */
    public void editStatuses (String operation, int orderId, Statuses newStatus) {
        /**Выбор операции: changeStatus -менять статус,  cancel-отмена, del-не реализовано,, */

        switch (operation) {
            case "changeStatus": /**  менять статус (изменение статуса отдельно по каждой записи*/
                orderRepository.findById(orderId).orElse(null).setStatuses(newStatus);
               // System.out.println("====>"+ orderRepository.findById(orderId).orElse(null).getStatuses().getName() ); //тест
            case "del":
            case "cancel":
            default:

        }
    }
/** поиск в заказах по последним символам заказа (добавить сортировку и фильтры)  */
    public List<Order> ordersSearch(String search){
      /** заказы  по окончанию номера-названия String number */
       return orderRepository.findAllByNumberEndingWith(search);


//        /** Если нет поисковой строки **/
//        if (search.isEmpty()){
//            /** поиск по всем категориям (public void initFirstCategory()=> name='Все категории'*/
//            if (inCategory.getName().equals("Все категории")){
//                if (sorting.equals("ascending_price")) {/** сортировка по возрастанию цен **/
//                    return productRepository.findByPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(min, max);
//                }else{/** сортировка по убыванию цен **/
//                    return productRepository.getAllProdDescPrice(min, max);
//                }
//            }else {/**поиск по отдельным категориям **/
//                if (sorting.equals("ascending_price")) {/** сортировка по возрастанию цен **/
//                    return productRepository.findByCategoryAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(inCategory, min, max);
//                }else{/** сортировка по убыванию цен **/
//                    return productRepository.getAllProdInCategoryDescPrice(inCategoryId, min, max);
//                }
//            }/******* ниже - тоже самое + поиск по наименованию ********/
//        }else /** Если нужен поиск по строке-наименованию **/
//        {
//            /** поиск по всем категориям (public void initFirstCategory()=> name='Все категории'*/
//            if (inCategory.getName().equals("Все категории")){
//                if (sorting.equals("ascending_price")) {/** сортировка по возрастанию цен **/
//                    return productRepository.findByTitleOrderByPriceAsc(search, min, max);
//                }else{/** сортировка по убыванию цен **/
//                    return productRepository.findByTitleOrderByPriceDest(search, min, max);
//                }
//            }else {/**поиск по отдельным категориям **/
//                if (sorting.equals("ascending_price")) {/** сортировка по возрастанию цен **/
//                    return productRepository.findByTitleAndCategoryOrderByPriceAsc(search, min, max, inCategoryId);
//                } else {/** сортировка по убыванию цен **/
//                    return productRepository.findByTitleAndCategoryOrderByPriceDesc(search, min, max, inCategoryId);
//                }    }   }

    }





}
