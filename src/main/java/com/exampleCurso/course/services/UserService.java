package com.exampleCurso.course.services;

import com.exampleCurso.course.entities.User;
import com.exampleCurso.course.repositories.UserRepository;
import com.exampleCurso.course.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    //Operação para salvar no banco de dados um dado do usuario

    public User insert(User obj){
        return repository.save(obj);
    }

    // operação para deletar um usuario do banco de dados

    public void delete (Long id){
        repository.deleteById(id);
    }

    // operação para atualizar os dados de um usuario

    public User update(Long id, User obj){
        User entity = repository.getReferenceById(id);
        updateData(entity,obj);
        return repository.save(entity);
    }

    private void updateData(User entity, User obj) {
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPhone(obj.getPhone());
    }
}
