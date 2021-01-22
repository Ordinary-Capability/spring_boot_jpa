package com.example.demo.controller;

import com.example.demo.mode.ChildRepository;
import com.example.demo.mode.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.mode.*;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequestMapping(path="/api")
public class DemoController {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ChildRepository childRepository;

    @PostConstruct
    private void initDb(){
        Parent parent = new Parent("father");
        Child child1 = new Child("son1");
        Child child2 = new Child("son2");
        Child child3 = new Child("son3");
        child1.setParent(parent);
        child2.setParent(parent);
        child3.setParent(parent);
        parentRepository.save(parent);
        childRepository.save(child1);
        childRepository.save(child2);
        childRepository.save(child3);

    }

    @GetMapping("/parents")
    public Iterable<Parent> getAllParents(){
        return parentRepository.findAll();
    }

    @GetMapping("/childs")
    public Iterable<Child> getAllChilds(){
        return childRepository.findAll();
    }

    @GetMapping("/test")
    public Parent test(){
        Optional<Child> child = childRepository.findByName("son1");
        return child.map(Child::getParent).orElse(null);
    }
}
