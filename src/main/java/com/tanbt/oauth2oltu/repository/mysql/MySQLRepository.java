package com.tanbt.oauth2oltu.repository.mysql;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.User;

@Repository
public interface MySQLRepository extends CrudRepository<User, String> {

    List<User> findByEmailAndPassword(String email, String password);
}
