package spring.attest.zuev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.attest.zuev.models.Category;
import spring.attest.zuev.services.CategoryService;

@Component
public class CategoryValidator implements Validator {
    private final CategoryService categoryService;
    @Autowired
    public CategoryValidator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override /** валидатор статусов категорий */
    public boolean supports(Class<?> clazz) {
        return Category.class.equals(clazz);
    }

    @Override
    /** ошибка повторенмя названий категорий  */
    public void validate(Object target, Errors errors) {
        /**принимая тип Object down-кастим до Category*/
        Category category = (Category) target;
        if (categoryService.getCategoryByName(category.getName()) != null) {
            errors.rejectValue("name", "", "Такая категория уже имеется");
        }
    }
}
