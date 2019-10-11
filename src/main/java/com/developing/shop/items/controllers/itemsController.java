package com.developing.shop.items.controllers;

import com.developing.shop.items.model.Item;
import com.developing.shop.items.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class itemsController {
    @Autowired
    private ItemRepository repository;


    @GetMapping("/items")
    public List<Item> getItems() {
        return repository.findAll();
    }

    @PostMapping(value = "/items", consumes = "application/json", produces = "application/json")
    public Item createItem(@RequestBody Item item) {
        return repository.save(item);
    }
}
