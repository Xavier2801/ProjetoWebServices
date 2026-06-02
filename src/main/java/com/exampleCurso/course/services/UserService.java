package com.exampleCurso.course.services;

import com.exampleCurso.course.entities.User;
import com.exampleCurso.course.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    // metodo para retornar todos os usuarios do banco de dados
    public List<User> findAll(){
        return repository.findAll();
    }

    // Recuperando um usario por id
    public User  findById(Long id){
        Optional<User> obj = repository.findById(id);
        return obj.get();
    }

}
