package spring.attest.zuev.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.attest.zuev.enumm.PersonRoles;
import spring.attest.zuev.models.*;
import spring.attest.zuev.services.*;
import spring.attest.zuev.util.CategoryValidator;
import spring.attest.zuev.util.PersonValidator;
import spring.attest.zuev.util.ProductValidator;
import spring.attest.zuev.util.StatusesValidator;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    /** функционал администратора 'ROLE_ADMIN' */

    @Value("${upload.path}")
    /** путь сохранения фотографий изделий */
    private  String uploadPath;
    private final StatusesValidator statusesValidator;
    private final ProductValidator productValidator;
    private final CategoryValidator categoryValidator;
    private  final PersonValidator personValidator;

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final StatusesService statusesService;
    private final PersonService personService;
    @Autowired
    public AdminController(StatusesValidator statusesValidator, ProductValidator productValidator, CategoryValidator categoryValidator, PersonValidator personValidator, ProductService productService, CategoryService categoryService, OrderService orderService, StatusesService statusesService, PersonService personService) {
        this.statusesValidator = statusesValidator;
        this.productValidator = productValidator;
        this.categoryValidator = categoryValidator;
        this.personValidator = personValidator;
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.statusesService = statusesService;
        this.personService = personService;
    }

    @GetMapping("") /** переход на стартовую страницу администрирования */
    public String admin(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "admin/admin";
    }
    /** ============================================================== */
    /**  ==========    Операции с категориями товаров ================ */
    @GetMapping("/product/category")
    public String editCategory(Model model){
        categoryService.initFirstCategory();
        model.addAttribute("categories", categoryService.findAllCategory());
        model.addAttribute("new_category", new Category());
        return "/product/editCategory";
    }
    @PostMapping("/product/category")
    public String editCategory(@RequestParam("operation")String operation, @RequestParam("categoryId") String categoryId, @ModelAttribute("new_category") @Valid Category newCategory, BindingResult bindingResult, Model model){
        categoryValidator.validate(newCategory, bindingResult);/** передаём в .validate() 2 объекта: сам объект категории и созданный объект ошибки.*/
        int id = Integer.parseInt(categoryId);
        /** ошибка именования категории **/
        if (bindingResult.hasErrors() && (operation.equals("new") || operation.equals("edit"))){
            model.addAttribute("categories", categoryService.findAllCategory());

            /**реализовано валидатором: Дополнить проверкой уникальности имени категории замена - @UniqueElements(message = "Такая категория уже есть") // мешает начальному запуску с пустым списком. или заменить  на автопереименование с ID в имени*/
            return "/product/editCategory";
        }
//        System.out.println(">>>>>> операция: "+operation+" ID категории: "+categoryId); // посмотреть id категории
//        System.out.println(">>>>>>> Категория>"+categoryService.editCategory(operation, categoryService.getCategoryById(id), newCategory));
//        System.out.println("Категория>"+categoryService.editCategory(operation, categoryService.findById(id).orElse(null), newName));
//        categoryService.editCategory(operation, categoryService.findById(id).orElse(null), newName);
        categoryService.editCategory(operation, categoryService.getCategoryById(id), newCategory);
        return "redirect:/admin";
    }
    /** ============================================================== */
    /** ===========  Операции с товарами ============================= */

    @GetMapping("/product/add")
    public String addProduct (Model model){
        model.addAttribute("category", categoryService.findAllCategory());
        model.addAttribute("product",new Product());
        return "product/addProduct";
    }
    @PostMapping ("/product/add") /** добавление нового товара */
    public String addProduct(Model model, @ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one") MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five) throws IOException {


        /** передаём в detailedValidate - 3 объекта:  объект прокукт, созданный объект ошибки и название операции (в .validate()-2)*/
        productValidator.detailedValidate(product, bindingResult, "new");


        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryService.findAllCategory());
            return "product/addProduct";
        }
        if(file_one != null)
        {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);}

        if(file_two != null)
        {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_three != null)
        {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_four != null)
        {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_five != null)
        {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product);
        return "redirect:/admin";

    }
    @GetMapping ("/product/delete/{id}")
    /** Удаление продукта */
    public String deleteProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "product/deleteProduct";
    }
    @PostMapping("/product/delete/{id}")
     public String deleteProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult) {//@PathVariable("id") int id,
        productValidator.detailedValidate(product, bindingResult, "del");
        if(bindingResult.hasErrors()){
            return "product/deleteProduct";
        }
       productService.delete(product.getId());
        return "redirect:/admin";
    }

    @GetMapping("/product/edit/{id}")
    /** Редактирование продукта */
    public String editProduct (Model model, @PathVariable("id") int id){
        model.addAttribute("category", categoryService.findAllCategory());
        model.addAttribute("product", productService.getProductId(id));
        return "product/editProduct";
    }
    @PostMapping("/product/edit/{id}")
    /** обработка ошибки дублирования имён ПРОДУКТОВ TITLE - detailedValidate (повторения имён в одноимённой категории - не разрешены  )**/
    public String editProduct (Model model, @ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id){
        /** передаём в detailedValidate - 3 объекта:  объект прокукт, созданный объект ошибки и название операции (в .validate()-2)*/
        productValidator.detailedValidate(product, bindingResult, "edit");
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryService.findAllCategory());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }
    /** ###################################################################### */
    /** ####### Методы редактирования статусов заказа  /admin/statuses ##### */
    @GetMapping("/statuses")
    /**Просмотр статусов заказов */
    public String editStatuses(Model model){
        model.addAttribute("statuses", statusesService.getAllStatuses());
        return "admin/statuses";
    }
    @GetMapping("/editStatuses/{id}")
    /**Редактирование статусов заказа (схоже с editCategory)*/
    public String editStatuses(Model model, @PathVariable("id") int id){ //, @ModelAttribute("newStatus") Statuses newStatus)
      //  model.addAttribute("statuses", statusesService.getAllStatuses());
        model.addAttribute("chosenStatus", statusesService.getStatusesById(id));
        return "admin/editStatuses";
    }
    @PostMapping("/editStatuses/{id}")
    public String editStatuses(@RequestParam("operation")String operation, @ModelAttribute("chosenStatus") @Valid Statuses newStatus, BindingResult bindingResult){ // @PathVariable("id") int id,
        //  System.out.println(">>>>>> операция: "+operation+" ID : "+id+"-"+newStatus.getName()+"-"+newStatus.getId()+"-"+newStatus.getName()); //test
        /** обработка ошибки дублирования имён статусов - detailedValidate (также не разрешается удалять начальный статус заказа "Принят" и не разрешается удалять если в заказах имеется удаляемый статус;  )**/
        statusesValidator.detailedValidate(newStatus, bindingResult, operation); /** передаём в .validate() 2 объекта:(detailedValidate - 3)  объект newStatus и созданный объект ошибки.*/
        /** ошибка именования статуса **/
        if (bindingResult.hasErrors() ){// перенесено в detailedValidate // && (operation.equals("new") || operation.equals("edit"))){
            return "admin/editStatuses";
        }  // заменено detailedValidate /** если editStatuses возвращает ошибку - добавление её в bindingResult, иначе "no errors" editStatuses - выполнит операцию */
        //  перенесено в detailedValidate // String txtErrors = statusesService.editStatuses(operation, id, newStatus); if (txtErrors != "no errors" ){ObjectError error = new ObjectError("error", txtErrors); bindingResult.addError(error); return "admin/editStatuses"; }
        statusesService.editStatuses(operation, newStatus);
        return "redirect:/admin/statuses";
    }


    /** ###################################################################### */
    /** ####### Методы администрирования заказов /admin/orders ############# */
    @GetMapping("/orders")
    /** просмотро заказов */
    public String adminOrders(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "/admin/ordersAdmin";
    }
    /** редактирование заказов */
    @GetMapping("/editOrder/{id}")
    public String editOrder(Model model, @PathVariable("id") int id){
        model.addAttribute("orderItem", orderService.getOrderById(id).orElse(null));
        model.addAttribute ("statuses", statusesService.getAllStatuses());
        return "admin/editOrder";
    }

    @PostMapping("/editOrder/{id}")/** редактирование заказов */
    public String editOrder(@RequestParam("operation")String operation, @RequestParam("newStatus")String newStatus, Model model, @PathVariable("id") int orderId){
        model.addAttribute("orderItem", orderService.getOrderById(orderId));

        orderService.editOrder(operation, orderId, statusesService.getStatusByName(newStatus));
//    похоже на categoryService.editCategory(operation, categoryService.getCategoryById(id));
        return "redirect:/admin/orders";
    }
    /** поиск заказов по имени - по 4 последним буквам */
    @PostMapping("/orders/search")
    public String productSearch(@RequestParam("search") String search, Model model){ /** передача строки-окончания поиска в нижнем регистре .toLowerCase */
        model.addAttribute("search_result", orderService.ordersSearch(search.toLowerCase()));
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("value_search", search);
//   **     model.addAttribute("categories", productService.getAllCategories());
//   **    model.addAttribute("value_minPrice", minPrice);
//   **    model.addAttribute("value_maxPrice", maxPrice);
        return "/admin/ordersAdmin";
    }

    /** ###################################################################### */
    /** ####### Методы администрирования пользователей /admin/personList ########################## */

    @GetMapping("/personList")
    /** Администрирование пользователей -  просмотр списка польз-ей */
    public String personList(Model model){
        model.addAttribute("personList", personService.getAllPersons());
        return "admin/personList";
    }

    @GetMapping("/infoPerson/{id}")
    /** Администрирование пользователей - редактирование пользователя */
    public String infoPerson(Model model, @PathVariable("id")int personId){
        model.addAttribute("person", personService.getPersonById(personId));
        model.addAttribute("pRoles", PersonRoles.values() );
        return "/admin/infoPerson";
    }

    @PostMapping("/infoPerson/{id}")
    /** Администрирование пользователей - редактирование пользователя */
    public String infoPerson(@ModelAttribute("person")Person person, BindingResult bindingResult, Model model, @PathVariable("id")int personId){ // @Valid Person person
/**валидация  @ModelAttribute("person") @Valid Person person  и
 * валидация personValidator.validate(person, bindingResult);
 *         model.addAttribute("person", personService.getPersonById(personId));// валидация осуществляется в сервисном слое// */
    personService.validateLogin(person, bindingResult);

         if(bindingResult.hasErrors()){
          return   "/admin/infoPerson";
         }

/** %%  заменено @ModelAttribute("person") - public String infoPerson(Model model, @PathVariable("id")int personId, @RequestParam("newRole") String newRole){    */
/** %%  пароль не перезадать-ошибка. попробовать passwordEncoder.encode(person.getPassword())- System.out.println("==>"+ person.getPassword()); if(bindingResult.hasErrors()){  model.addAttribute("person", personService.getPersonById(personId));  return "/admin/infoPerson";  } */

        personService.changePerson(person);
        return "/admin/infoPerson";
    }


}
