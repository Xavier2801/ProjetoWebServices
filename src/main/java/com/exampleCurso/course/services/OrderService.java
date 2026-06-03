package com.exampleCurso.course.services;

import com.exampleCurso.course.entities.Order;
import com.exampleCurso.course.entities.User;
import com.exampleCurso.course.repositories.OrderRepository;
import com.exampleCurso.course.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order findById(Long id) {
        Optional<Order> obj = repository.findById(id);
        return obj.get();
    }
}
