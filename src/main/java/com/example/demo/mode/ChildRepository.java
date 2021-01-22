package com.example.demo.mode;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChildRepository extends CrudRepository<Child, Integer> {
    Optional<Child> findByName(String name);
}
