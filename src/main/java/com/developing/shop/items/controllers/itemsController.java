package com.developing.shop.items.controllers;

import com.developing.shop.items.model.Item;
import com.developing.shop.items.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/warehouse")
public class itemsController {
    @Autowired
    private ItemService itemService;


    @GetMapping("/items")
    public List<Item> getItems(@RequestBody @Nullable Map<String, String> params) {
        return itemService.getItems(params);
    }

    @PostMapping(value = "/items")
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

    @PutMapping("/items/{id}")
    public Item alterItem(@PathVariable("id") long id, @RequestBody Item item) {
        return itemService.alterItem(item, id);
    }
}
