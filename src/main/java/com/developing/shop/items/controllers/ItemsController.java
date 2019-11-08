package com.developing.shop.items.controllers;

import com.developing.shop.items.model.Item;
import com.developing.shop.items.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/warehouse")
public class ItemsController {

    private Logger logger = LoggerFactory.getLogger(ItemsController.class);

    private final ItemService itemService;

    @Autowired
    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(Exception ex) {
        logger.error("Illegal argument exception", ex);

        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
