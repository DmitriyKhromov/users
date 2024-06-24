package ru.skillbox.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findUsersByDeleted(Boolean deleted);

    boolean existsUserByEmail(String email);

    User findUserByEmail(String email);
}
