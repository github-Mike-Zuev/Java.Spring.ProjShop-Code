package spring.attest.zuev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.attest.zuev.models.Category;
import spring.attest.zuev.models.Person;
import spring.attest.zuev.models.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByTitleContainingIgnoreCase(String name);
    List<Product> findAllByCategory(Category category);
    /** подсчёт итоговой стоимости товаров корзины sum(кол-во*цену) по ID польз-ля **/
    @Query(value ="select sum(price * cart.quantity) from product prod inner join product_cart cart on prod.id = cart.product_id where cart.person_id = ?1" ,nativeQuery = true)
    Optional<Float> totalCartPerson(int personId);
    /** не реалзовано - методы вызывают ошибки/требуют преобразования Product-Category
     (productRepository.getFirstByCategory / findFirstByCategory)
     Category getFirstByCategory (Category category);//findFirstByCategory **/
    /** получение товаров из корзины пользователя столбец PersonList в Cart **/
    List<Product> findAllByPersonListEquals(Person person);

    /** все продукты сортировка по возрастанию цены**/
    List<Product> findByPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc(float min, float max);
    /** все продукты сортировка по убыванию цены**/
    @Query(value = "select * from product where (price >= ?1 and price <=?2) order by price desc", nativeQuery = true)
    List<Product> getAllProdDescPrice(float min, float max);
    /** список продуктов по категории, сортировка по возрастанию  цен */
    List<Product> findByCategoryAndPriceGreaterThanEqualAndPriceLessThanEqualOrderByPriceAsc (Category category, float min, float max);
    /** список продуктов по категории, сортировка по убыванию цен */
    @Query(value = "select * from product where category_id =?1 and price>=?2 and price<=?3 order by price desc",nativeQuery = true)
    List<Product> getAllProdInCategoryDescPrice(int CategoryId, float min, float max);

    /** Поиск по наименованию, фильтрация по диапазону цены, без сортировки */
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3)", nativeQuery = true)
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String searchInTitle, float min, float max);
    /**Поиск по наименованию, фильтрация по диапазону цены, сортировка по возрастанию цены*/
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by  price", nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String searchInTitle, float min, float max);

    /** Поиск по наименованию, фильтрация по диапазону цены, сортировка по убыванию цены */
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by  price desc ", nativeQuery = true)
    List<Product> findByTitleOrderByPriceDest(String searchInTitle, float min, float max);

    // Поиск по наименованию, по категории, фильтрация по диапазону цены, сортировка по возрастанию цены
    @Query(value = "select * from product where category_id=?4 and ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by  price", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String searchInTitle, float min, float max, int category);

    // Поиск по наименованию, по категории, фильтрация по диапазону цены, сортировка по убыванию цены
    @Query(value = "select * from product where category_id=?4 and ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by  price desc ", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String searchInTitle, float min, float max, int category);
}

