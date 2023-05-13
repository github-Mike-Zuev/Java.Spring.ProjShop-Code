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
import spring.attest.zuev.models.Category;
import spring.attest.zuev.models.Image;
import spring.attest.zuev.models.Person;
import spring.attest.zuev.models.Product;
import spring.attest.zuev.services.*;

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

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final StatusesService statusesService;
    private final PersonService personService;
    @Autowired
    public AdminController(ProductService productService, CategoryService categoryService, OrderService orderService, StatusesService statusesService, PersonService personService) {
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
        int id = Integer.parseInt(categoryId);
        /** ошибка именования категории **/
        if (bindingResult.hasErrors() && (operation.equals("new") || operation.equals("edit"))){
            model.addAttribute("categories", categoryService.findAllCategory());

            /** Дополнить проверкой уникальности имени категории @UniqueElements(message = "Такая категория уже есть") // мешает начальному запуску с пустым списком. Заменено на автопереименование с ID в имени
            return "/product/editCategory"; */
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
    public String deleteProduct(@PathVariable("id") int id){
        productService.delete(id);
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
    public String editProduct (Model model, @ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryService.findAllCategory());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }
    /** ###################################################################### */
    /** ####### Методы администрирования заказов /admin/orders ########################## */
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

        orderService.editStatuses(operation, orderId, statusesService.getStatusByName(newStatus));
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
    public String infoPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, Model model, @PathVariable("id")int personId){
/** %%  заменено @ModelAttribute("person") - public String infoPerson(Model model, @PathVariable("id")int personId, @RequestParam("newRole") String newRole){    */
/** %%  пароль не перезадать-ошибка. попробовать passwordEncoder.encode(person.getPassword())- System.out.println("==>"+ person.getPassword()); if(bindingResult.hasErrors()){  model.addAttribute("person", personService.getPersonById(personId));  return "/admin/infoPerson";  } */
        //model.addAttribute("person", personService.getPersonById(personId));
        personService.changePerson(person);
        return "/admin/infoPerson";
    }
}
