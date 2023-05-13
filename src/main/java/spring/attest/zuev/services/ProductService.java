package spring.attest.zuev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.attest.zuev.models.*;
import spring.attest.zuev.repositories.*;
import spring.attest.zuev.security.PersonDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartRepository cartRepository;
    private final StatusesRepository statusesRepository;
    private final OrderRepository orderRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, CartRepository cartRepository, StatusesRepository statusesRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cartRepository = cartRepository;
        this.statusesRepository = statusesRepository;
        this.orderRepository = orderRepository;
    }
    // передача всех товаров
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }   // товар по id
    public List<Product> getAllProductByCategory(Category category){
        return productRepository.findAllByCategory(category);
    }
    /** получение текущего пользователя из контекста аутентификации*/

    public Person getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//        System.out.println("ID пользователя: " + personDetails.getPerson().getId());
//        System.out.println("Роль пользователя:" + personDetails.getPerson().getRole());
//        System.out.println("Логин пользователя:" + personDetails.getPerson().getLogin());
//        System.out.println("Пароль пользователя:" + personDetails.getPerson().getPassword());
        return personDetails.getPerson();
    }
    /** Методы категорий товаров */
    /** передача списка категорий*/
    public List<Category> getAllCategories(){
        /** создание категории "Все категории" если список категорий пуст **/
        if (categoryRepository.count()==0){ // дубль CategoryService.initFirstCategory()
            Category firstCategory = new Category(1);
            firstCategory.setName("Все категории");
            categoryRepository.save(firstCategory);
        }
        return categoryRepository.findAll();
    } /** передача объекта-категории по ид-номеру */
    public  Category getCategoryById(int id){
        return categoryRepository.findById(id);
    }
    /** не реалзовано методы вызывают ошибки/требуют преобразования Product-Category
     (productRepository.getFirstByCategory / findFirstByCategory)
     public boolean haveAnyCategory (Category category){
     // System.out.println("!!!! have not AnyCategory="+productRepository.getFirstByCategory  (category).toString()+"=");//findFirstByCategory
     //        if (productRepository.findFirstByCategory (category).equals(null)) return false;
     return true;
     } **/
    public List<Product> productSearch( String search, String minPrice, String maxPrice, String sorting, int inCategoryId){
        float min, max;
        Category inCategory = this.getCategoryById(inCategoryId);
        /** обобщение для поиска по ценам (+по умолчанию сортировка asc):
         * если не указана(ы) граница(ы) цены => макс. расширение диапазона иначе приведение */
        if(minPrice.isEmpty()) min =0f;
        else min = Float.parseFloat(minPrice) ;
        if(maxPrice.isEmpty())max = 1.0E18f;
        else max = Float.parseFloat((maxPrice));
//        System.out.println("@@@@@@@@@@@@@ test-search @@@@@@@@@@@@@\nSearch= "+search+"\n minPr= "+ minPrice+"min= "+min+" maxPr= "+maxPrice+"max= "+max+" sorting= " +sorting+ "\n inCategory*2= "+(inCategoryId*2));
        /** Если нет поисковой строки **/
        if (search.isEmpty()){
            /** поиск по всем категориям (public void initFirstCategory()=> name='Все категории'*/
            if (inCategory.getName().equals("Все категории")){
                if (sorting.equals("ascending_price")) {/** сортировка по возрастанию цен **/
                    return productRepository.findByPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(min, max);
                }else{/** сортировка по убыванию цен **/
                    return productRepository.getAllProdDescPrice(min, max);
                }
            }else {/**поиск по отдельным категориям **/
                if (sorting.equals("ascending_price")) {/** сортировка по возрастанию цен **/
                    return productRepository.findByCategoryAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(inCategory, min, max);
                }else{/** сортировка по убыванию цен **/
                    return productRepository.getAllProdInCategoryDescPrice(inCategoryId, min, max);
                }
            }/******* ниже - тоже самое + поиск по наименованию ********/
        }else /** Если нужен поиск по строке-наименованию **/
        {
            /** поиск по всем категориям (public void initFirstCategory()=> name='Все категории'*/
            if (inCategory.getName().equals("Все категории")){
                if (sorting.equals("ascending_price")) {/** сортировка по возрастанию цен **/
                    return productRepository.findByTitleOrderByPriceAsc(search, min, max);
                }else{/** сортировка по убыванию цен **/
                    return productRepository.findByTitleOrderByPriceDest(search, min, max);
                }
            }else {/**поиск по отдельным категориям **/
                if (sorting.equals("ascending_price")) {/** сортировка по возрастанию цен **/
                    return productRepository.findByTitleAndCategoryOrderByPriceAsc(search, min, max, inCategoryId);
                } else {/** сортировка по убыванию цен **/
                    return productRepository.findByTitleAndCategoryOrderByPriceDesc(search, min, max, inCategoryId);
                }    }   }}

    public Product getProductId(int id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }
    @Transactional
    public void saveProduct (Product product){
        productRepository.save(product);
    }
    @Transactional
    public void updateProduct (int id, Product product){
        product.setId(id);
        productRepository.save(product);
    }
    @Transactional
    public void delete(int id){
        productRepository.deleteById(id);
    }

    /** ################################################# */
    /** ####### Методы для корзины ###################### */
    public List<Cart> cartPerson(){ /** получение корзины покупок польз-ля */
        return cartRepository.findByPersonId(getCurrentPerson().getId());
    }

    /** подсчёт итога цены корзины **/
    public float totalCartPerson (){
        return productRepository.totalCartPerson(getCurrentPerson().getId()).orElse(0f);
    }
    /** получение строки товара из корзины текущего польз-ля по id товара, или null*/
    public Optional<Cart> getCartByProdId(int prodId){
        int personId = this.getCurrentPerson().getId();
        return cartRepository.getCartByProdId(personId, prodId);
    }
    @Transactional
    /** добавление по-штучно в корзину товара по id-товара и по ид-польз-ля из авторизации */
    public void  addCart(int productId) {
        Cart cart = getCartByProdId(productId).orElse(new Cart(getCurrentPerson().getId(), productId));
     /**тест    System.out.println("!!!!! CartId =>" + cart.getId());
        System.out.println("!!!!! CartQuant =>" + cart.getQuantity()); */
//        Cart cart = new Cart(personId, productId);
//        if (CartId<0){
        cart.setQuantity(cart.getQuantity()+1);
        cartRepository.save(cart);
//               }

    }
    /** передача списка товаров польз-ля */
    public List<Product> cart(){
        Person currentPerson = this.getCurrentPerson();
        return productRepository.findAllByPersonListEquals(currentPerson);
    }

    @Transactional
    /** удаление продукта из корзины по 1- уменьшение к-ва до 1 и далее удаление*/
    public void cartDelProductId (int productId){
        int personId = this.getCurrentPerson().getId();
        /** получение строки товара из корзины текущего польз-ля */
        Cart cart = getCartByProdId(productId).orElse(null);
        cart.setQuantity(cart.getQuantity()-1);  /** декремент */
        if (cart.getQuantity()<=0){ /** если закончилась - удаление */
            cartRepository.deleteDistinctByPersonIdAndProductId(personId, productId);
        }else /** если ещё осталась продукция в корзине - модификация */
        cartRepository.save(cart);

        //  System.out.println("!!!!!!!!!!!!!!!!!!!! personId= "+personId+"\n productId= "+ productId);
        // cartRepository.cartDelProductId(personId, productId);
        /**   cartRepository.deleteCartByPersonIdIsAndProductIdIs(personId, productId); */
        /**  cartRepository.deleteByPersonIdAndProductId(personId, productId); */
        /**  cartRepository.deleteByPersonIdEqualsAndProductIdEquals(personId, productId); */
    }

    /** ################################################# */
    /** ####### Методы заказов ########################## */
    @Transactional
    /** формирование заказа и очистка корзины */
    public void createOrder(){

        /**  id текущего пользователя */
        int id_person = getCurrentPerson().getId();
//        List<Cart> cartList = cartRepository.findByPersonId(id_person);
//        List<Product> productList = new ArrayList<>();
//        // Получаем продукты из корзины по id товара
//        for (Cart cart: cartList) {
//            productList.add(getProductId(cart.getProductId()));
//        }
        /** Вычисление итоговой стоимости по заказу */
        float totalCart = totalCartPerson();
        /** получение уникальной строки имени */
        String uuid = UUID.randomUUID().toString();
        Statuses status = statusesRepository.findByName("Принят");
        /** цикл по продуктам в корзине текущего польз-ля */
        for(Product product : cart()){
            /** строка корзины по польз-лю и продукту для получения количества */
           Cart cart = cartRepository.getCartByProdId(id_person, product.getId()).orElse(null);
           int quantity = cart.getQuantity(); /** количество продукта в заказе */
           float priceLine = quantity * product.getPrice(); /** сумарная стоимость по продукту */
            Order newOrder = new Order(uuid, product, getCurrentPerson(), quantity, priceLine, totalCart, status);
            orderRepository.save(newOrder);
            cartRepository.deleteCartByProductId(product.getId());
        }
     }

     public List<Order> orderUser(){
        return  orderRepository.findByPerson(getCurrentPerson());
     }


}
