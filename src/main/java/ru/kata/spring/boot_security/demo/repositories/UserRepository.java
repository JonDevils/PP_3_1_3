package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long > {

     @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = (:username)")
     public User findByUserNameAndFetchRoles(@Param("username") String username);

    User findByUsername(String username);

//     @Query("FROM User u JOIN FETCH u.roles")
//     public List<User> getAllUsersAndFetchRoles(); // **query that you would use!**
}
