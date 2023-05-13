package spring.attest.zuev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.attest.zuev.models.Statuses;

import java.util.List;

@Repository
public interface StatusesRepository extends JpaRepository<Statuses, Integer> {
    Statuses findByName(String name);
//        Statuses findById(int id);
//   List<Statuses> getAllBy();
 //   int countAll();


        }