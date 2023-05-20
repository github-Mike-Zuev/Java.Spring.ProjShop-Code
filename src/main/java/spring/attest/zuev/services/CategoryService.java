package spring.attest.zuev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.attest.zuev.models.Category;
import spring.attest.zuev.models.Person;
import spring.attest.zuev.repositories.CategoryRepository;
import spring.attest.zuev.security.PersonDetails;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private  final CategoryRepository categoryRepository;
    private final ProductService productService;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    public List<Category> findAllCategory(){
        return categoryRepository.findAll();
    }
    //    public Optional<Category> findById(int id){
//        return  categoryRepository.findById(id);
//    }
    public Category getCategoryById(int id){
        return categoryRepository.findById(id);
    }
    /** поик категории по имени (для валидатора CategoryValidator - на дублирование) */
    public Category getCategoryByName(String name){ return categoryRepository.findByName(name);}
    @Transactional
    public void addCategory (Category category){
        categoryRepository.save(category);
    }
    @Transactional
    public void updateCategoryById(int id, Category category){
        category.setId(id);
        categoryRepository.save(category);
    }
    //    @PrePersist
    @Transactional
    /** создание категории "Все категории" если список категорий пуст **/
    public void initFirstCategory(){
        System.out.println("!!!!!!categoryRepository.count()="+categoryRepository.count());
        if (categoryRepository.count()==0){ // .findAll().isEmpty())
//            Category firstCategory = new Category("Все категории", currentPerson());
            Category firstCategory = new Category();
            //firstCategory.setPersonOwner(currentPerson());
            firstCategory.setId(1);
            firstCategory.setName("Все категории");
            firstCategory.setPersonOwner(currentPerson());// владелец 0 категории  -текущий пользователь
            categoryRepository.save(firstCategory);
        }
    }
    private boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String currentRole = personDetails.getPerson().getRole();
        if (currentRole.equals("ROLE_ADMIN")) return true;
        return false;
    }
    private Person currentPerson(){ /** копия ProductService getCurrentPerson - позже - объединить*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return  personDetails.getPerson();
    }

    private boolean haveChildren(Category category){
        /** наличие продуктов в данной категории товаров **/

        System.out.println("getAllProductByCategory(category).size()"+productService.getAllProductByCategory(category).size());
        /** не реалзовано методы вызывают ошибки/требуют преобразования Product-Category
         (productRepository.getFirstByCategory / findFirstByCategory)
         System.out.println("haveAnyCategory(category)"+productService.haveAnyCategory(category)); **/
        if (productService.getAllProductByCategory(category).size() > 0) return true;
        return false;
    }
    @Transactional
    public String editCategory (String operation, Category category, Category newCategory) {
        if (operation.equals("new")) {
            newCategory.setPersonOwner(currentPerson());
            categoryRepository.save(newCategory);
            return "Категория создана";
        }/* изменение/удаление не админом и не создателем категории */
        if(!isAdmin() && (!category.getPersonOwner().equals(currentPerson()) )) return "Нет прав для изменения/удаления.";
        switch (operation) {
            case "del":
           /**  разрешается удалять последний элемент// if () return "Нельзя удалять  единственный элемент"; */
                /** не удалять если в категории есть товары **/
                if (this.haveChildren(category)) return "Нельзя удалить категорию имеющую товары";
                categoryRepository.delete(category);
                return "Категория удалена";
            case "edit": category.setName(newCategory.getName());
//                    category.setPersonOwner(currentPerson());
//            case "edit": category.setName(newName);
                categoryRepository.save(category);
                return "Категория изменена";
        }
        return "Не выполнено";
    }

}