package spring.attest.zuev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.attest.zuev.models.Category;
import spring.attest.zuev.models.Order;
import spring.attest.zuev.models.Product;
import spring.attest.zuev.models.Statuses;
import spring.attest.zuev.services.OrderService;
import spring.attest.zuev.services.ProductService;

import java.util.List;


@Component
public class ProductValidator implements Validator {
    private final ProductService productService;
    private final OrderService orderService;
    @Autowired

    public ProductValidator(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override /** валидатор продукции */
    public boolean supports(Class<?> clazz) {
        return Product.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }



    //   public List<Product> getListProductByTitleAndCategory
//    @Override
//    /** ошибка повторенмя названий статусов заказа  */
//    public void validate(Object target, Errors errors) {
//        Product product = (Product) target; /**принимая тип Object down-кастим до Product*/
//        Product duplicate = productService. getStatusByName(statuses.getName());
//        if ( duplicate != null && duplicate.getId() != statuses.getId()) {
//            errors.rejectValue("title", "", "Такой статус уже имеется");
//        }
//    }
   /** повторения имён в одноимённой категории - не разрешены (повторения- только в разных категориях- по 1) */
   /** operation: "new"- addProduct  "edit"-updateProduct|| */
    public void detailedValidate(Object target, Errors errors, String operation ){
        Product product = (Product) target; /**тип Object down-кастим до Product*/
        Category currentCategory = product.getCategory();
        List <Product> duplicatesArr = productService.getListProductByTitleAndCategory(product.getTitle(), currentCategory);
        Product duplicate = new Product();
//        int chosenStatusId = statuses.getId();
//        if ( duplicate != null && duplicate.getId() != statuses.getId()) {
//            errors.rejectValue("title", "", "Такой статус уже имеется");
//        }
        switch (operation) {
            /**Удаление товара  используемого в заказах покупателей -запрещено.  Удаление товара используемого в корзинах покупателей - разрешено, при этом записи с этим товаром в корзинах пользователей - исчезнут. */
            case "del":
               /** не удалять если в заказах имеется удаляемый товар **/
                for ( Order order : orderService.getAllOrders()){ /** проверка наличия по id */
                    if ( order.getProduct().getId() == product.getId()){
                      //  System.out.println(order.getProduct().getTitle()+"==Удаление==> " + product.getTitle()); //тест
                        errors.rejectValue("title","","Удаляемый продукт ещё используется в заказе № " + order.getNumber());
                        break;
                    }
                 //  System.out.println(order.getProduct().getTitle()+"==Перебор==> " + product.getTitle()); //тест
                }
                break;

            case "new":
              /**  проверка дублирования имён при добавлении - совпадает с edit */
//                break;
            case "edit":
                /**  проверка дублирования имён при редактированиии  */
//               System.out.println("=====size===>"+duplicatesArr.size()+"\n===duplicatesArr.get(0).title=="+duplicatesArr.get(0).getTitle() );// test
                if  (duplicatesArr.size()>0) {
                    duplicate = duplicatesArr.get(0);
                    System.out.println("duplicate.getId() != product.getId() ==>"+ duplicate.getId()+ "!="+ product.getId() );
                    if (duplicate.getId() != product.getId() || duplicatesArr.size() > 1) {
                        errors.rejectValue("title", "", "В категории: '" + currentCategory.getName() + "',  имя '" + product.getTitle() + "' - уже используется " + duplicatesArr.size() + " раз");
                    }
                }
                break;
        }

    }
}
