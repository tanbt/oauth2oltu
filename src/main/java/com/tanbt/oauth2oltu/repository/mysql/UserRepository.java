package com.tanbt.oauth2oltu.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByEmailAndPassword(String email, String password);
}
