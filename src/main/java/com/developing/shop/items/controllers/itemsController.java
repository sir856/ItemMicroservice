package com.developing.shop.items.controllers;

import com.developing.shop.items.model.Item;
import com.developing.shop.items.repository.ItemRepository;
import com.developing.shop.items.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class itemsController {
    @Autowired
    private ItemService itemService;


    @GetMapping("/items")
    public List<Item> getItems(@RequestParam Map<String, String> params) {
        return itemService.getItems(params);
    }

    @PostMapping(value = "/items", consumes = "application/json", produces = "application/json")
    public Item createItem(@RequestBody Item item) {
        return itemService.addItem(item);
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable("id") long id) {
        return itemService.getItemById(id);
    }

    @DeleteMapping("/items/{id}")
    public Item deleteItem(@PathVariable("id") long id) {
        return itemService.delete(id);
    }
}
