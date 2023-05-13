package spring.attest.zuev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.attest.zuev.models.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    /** поиск по логину */
    Optional<Person> findByLogin(String login);
    /** притсутствие хотя бы 1 АДМИНА */
    boolean existsByRoleIsLike(String role);

}
