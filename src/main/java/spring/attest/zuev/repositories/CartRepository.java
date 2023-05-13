package spring.attest.zuev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spring.attest.zuev.models.Cart;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Получаем корзину по id пользователя
    List<Cart> findByPersonId(int id);
    /** получение первого id по товару пользователя or null **/
    @Query(value = "select * from product_cart where (person_id=?1 and product_id=?2) ",nativeQuery = true)
    Optional<Cart> getCartByProdId(int personId, int productId);
//Cart findFirstByPersonIdAndProductId(int personId, int productId);
    /** удаление товара из корзины */
    @Modifying /** !!!!!! @Modifying - ????? */
    @Transactional
    void deleteCartById(int id);

    @Modifying
    @Transactional
    void deleteCartByProductId(int idProduct);
    /* подобрать запрос для удаления одной(а не всех)записи
    with res(id,ps_id, pr_id) as (select * from product_cart where (person_id=3 and product_id=24));

    declare
    delete  from product_cart where (select * from product_cart where (person_id=3 and product_id=24)
    order by id limit 1))=product_cart.id;

    with result as (select * from product_cart where (person_id=3 and product_id=24) order by id limit 1)
     delete from product_cart where product_cart.id = result.id;

    with result as (select * from product_cart where (person_id=3 and product_id=24) order by id fetch first row only) delete from product_cart where result.id=product_cart.id;

    delete from product_cart  where (select * from product_cart where (person_id=3 and product_id=24) order by id limit 1 );

    with result as (select distinct product_id from product_cart where person_id=3 and product_id=24) delete from product_cart where result.id=product_cart.id
    delete from product_cart where ((select distinct from product_cart where person_id=3 and product_id=24) as result) where result.id=product_cart.id
    declare result_cursor cursor WITH HOLD for select * from product_cart where (person_id=3 and product_id=24) order by product_id limit 1;
    delete from product_cart where current of result_cursuor;

    with result as (select * from product_cart where (person_id=3 and product_id=24) order by product_id limit 1 ) delete from result where ;

    select sum(price) from product prod inner join product_cart cart on prod.id = cart.product_id where cart.person_id = 2;
    delete  from product_cart first where ( (person_id=1 and product_id=3) );
    delete from product_cart where (person_id=3 and product_id=24) order by product_id limit 1 ;
         @Modifying()
         @Query(value = "delete first from product_cart  where (person_id=?1 and product_id=?2)", nativeQuery = true)
         void cartDelProductId (int personId, int ProductId); */
    @Transactional
    void deleteDistinctByPersonIdAndProductId(int personId, int ProductId);
    /**     void deleteCartByPersonIdIsAndProductIdIs(int personId, int ProductId); */
/**     void deleteByPersonIdAndProductId(int personId, int ProductId); */
/**     void deleteByPersonIdEqualsAndProductIdEquals(int personId, int ProductId); */
    // Cart findByPersonIdAndProductId(int personId, int ProductId);
}

