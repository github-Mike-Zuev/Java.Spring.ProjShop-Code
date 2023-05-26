package spring.attest.zuev.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.attest.zuev.models.Person;
import spring.attest.zuev.security.PersonDetails;
import spring.attest.zuev.services.CategoryService;
import spring.attest.zuev.services.OrderService;
import spring.attest.zuev.services.PersonService;
import spring.attest.zuev.services.ProductService;
import spring.attest.zuev.util.PersonValidator;

@Controller
public class UserController {
//    @Value("${upload.path}")
//    private String path;
    final PersonValidator personValidator;
    final PersonService personService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService ;
@Autowired
    public UserController(PersonValidator personValidator, PersonService personService, ProductService productService, CategoryService categoryService, OrderService orderService) {
        this.personValidator = personValidator;
        this.personService = personService;
    this.productService = productService;
    this.categoryService = categoryService;
    this.orderService = orderService;
}

    @GetMapping("/authentication")
    public String login(){
        return "authentication/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person){
        return "authentication/registration";
    }
    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @RequestParam(value = "check_admin", required = false, defaultValue = "false") boolean check_admin){
        personValidator.validate(person, bindingResult); /** передаём в .validate() 2 объекта: сам объект person и созданный объект ошибки.*/
        if(bindingResult.hasErrors()){ /** Если валидатор возвращает ошибку - помещаем данную ошибку в bindingResult*/
            return "authentication/registration";
        }
        /** check_admin = true если на форме - отметили галочку - администратор */
        /** И если нет ни одного администратора - будет присвоена роль Admin */
        if (check_admin && personService.noAdmins()){
            personService.registerAdmin(person);
            System.out.println("Зарегистрирован администратор: "+person.getLogin());
            return "redirect:/admin";
        }
        else {personService.register(person);}
        return "redirect:/index";
    }
    @GetMapping("/start page")
    /** начальная страница с проверкой начичия Б/Д (хотя бы 1 категории продукта)*/
    public String startPage(Model model){
        /** для предотвращения возникновения ошибок необходимо обязательное наличие одной категории продуктов. Для этого достаточно вызова метода СategoryService-initFirstCategory(). //Ещё не реализовано// > Но поскольку, без наличия продуктов - продолжение работы безсмысленно, добавлен дополнительный шаблон /admin/emptyBase" администратора для создания минимального количества элементов базы данных. */
       /** //Ещё не реализовано// if (productService.getAllProduct().size() == 0) {
         System.out.println("====> Categories=" + categoryService.findAllCategory().size()); System.out.println("====> Products= " + productService.getAllProduct().size());
            return "/admin/emptyBase"; }*/
        if (categoryService.findAllCategory().size() == 0) {
            categoryService.initFirstCategory();
        }
        return "redirect:/index";
    }

    @GetMapping("/index")
    /** переход в личный кабинет админа(admin/admin) или польз-ля(user/index) согласно роли */
    public String index(Model model){
        model.addAttribute("products",productService.getAllProduct());
        model.addAttribute("categories", productService.getAllCategories());
        /** Не реализовано: , @PathVariable("check_admin") boolean check_admin check_admin = false- вход как user а не admin ; =true если на форме - отметили галочку - администратор  и если нет ни одного администратора - создать Admin - пароль:"Admin
         *    if (check_admin ){ /** можно вести/добавлять в журнал администрирования вход админов
         System.out.println("###> Запрос входа c ролью администратора : " + personDetails.getPerson().getLogin());
         }*/
        /**Ниже: получаем объект аутентификации - > с помощью Spring SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации.
        * Из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутентификации */

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();

        /** если роль ROLE_ADMIN - кабинет админа, иначе(ROLE_USER) user/index */
        if (role.equals("ROLE_ADMIN"))return "redirect:/admin";
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("categories",categoryService.findAllCategory());
        return "user/index";
    }

    @GetMapping("/backFromInfo")
    /** переадресация после просмотра подробной информации в зависимости от авторизации */
    public String backFromInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /** если anonymousUser - переадр./product, иначе(ROLE_USER) index */
        if  (authentication.getPrincipal().toString().equals("anonymousUser")) return "redirect:/product";
       return "redirect:/index";
    }

/** подробная инф-я о продукции для польз-ля (аналог product/info)*/
  /** @GetMapping("/info/{id}") - совмещён в один метод /product/info
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product",productService.getProductId(id));
        return "user/infoProduct";
    } */
    /** фильтрация/поиск/сортировка продукции для польз-ля (аналог product/search) позже объединить*/
    @PostMapping("/user/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("minPrice") String minPrice, @RequestParam("maxPrice") String maxPrice, @RequestParam(value = "sorting", required = false, defaultValue = "")String sorting, @RequestParam(value = "inCategory", required = false, defaultValue = "")int inCategoryId, Model model){ /** передача строки поиска в нижнем регистре .toLowerCase */
        model.addAttribute("search_result", productService.productSearch(search.toLowerCase(), minPrice, maxPrice, sorting, inCategoryId));
        model.addAttribute("categories", productService.getAllCategories());

        model.addAttribute("value_search", search);
        model.addAttribute("value_minPrice", minPrice);
        model.addAttribute("value_maxPrice", maxPrice);
        model.addAttribute("products", productService.getAllProduct());
        return "user/index";
    }
    /** ################################################# */
    /** ####### Методы корзины ######################## */

    @GetMapping("/cart/add/{id}")
    /** добавление в корзину товара ид товара и по ид польз-ля из авторизации */
    public String addProductInCart(@PathVariable("id")int ProductId, Model model){
        productService.addCart(ProductId);
        return "redirect:/cart";}

     @GetMapping("/cart")
     /** Добавить продукт в корзину (из личного кабинета) */
     public String cart(Model model){
        /** получение корзины покупок польз-ля,  */
         model.addAttribute("cartCurrent", productService.cartPerson());
         /** передача списка товаров польз-ля */
         model.addAttribute("cartProducts",productService.cart());
         /** подсчёт итога цены корзины **/
         float totalCart = productService.totalCartPerson();
         //  System.out.println("!!!!!!!!!!!!!!!!  total= "+totalCart);//test
         model.addAttribute("totalCart", totalCart);
         return "user/cart";
     }

    @GetMapping("/cart/del/{id}")
    /** удаление товара из корзины */
    public String cartDelProduct(@PathVariable("id") int productId, Model model){
        productService.cartDelProductId(productId);
        return "redirect:/cart";
    }
    /** ################################################# */
    /** ####### Методы заказов ########################## */
    @GetMapping("/order/create")
    /** формирование заказа и очистка корзины */
    public String order(){
        productService.createOrder();
        // Вычисление итоговой цена
       /** float totalCart = productService.totalCartPerson();*/
        return "redirect:/orders";
    }
    @GetMapping("/orders")
    /** просмотр заказов */
    public String orderUser(Model model){

        model.addAttribute("orders", productService.orderUser());
        /** передача количества заказов(уникальных номеров) клиента*/
        model.addAttribute("totalNumbersOrder",orderService.totalNumbersOrder());
        return "/user/orders";
    }
}
