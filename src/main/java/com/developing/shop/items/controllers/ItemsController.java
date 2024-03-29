package com.developing.shop.items.controllers;

import com.developing.shop.items.model.Item;
import com.developing.shop.items.service.ItemService;
import com.developing.shop.orders.messageListeners.data.MessageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ItemsController(ItemService itemService, RabbitTemplate template) {
        this.itemService = itemService;
        this.rabbitTemplate = template;
    }

    @GetMapping("/items")
    public List<Item> getItems(@RequestBody @Nullable Map<String, String> params) {
        return itemService.getItems(params);
    }

    @PostMapping(value = "/items")
    public Item createItem(@RequestBody Item item) {
        item = itemService.addItem(item);
        rabbitTemplate.setExchange("orderExchange");
        MessageItem messageItem = new MessageItem(item.getId(), item.getName(), item.getAmount(), item.getPrice());
        rabbitTemplate.convertAndSend("add", messageItem);
        return item;
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable("id") long id) {
        return itemService.getItemById(id);
    }

    @DeleteMapping("/items/{id}")
    public Item deleteItem(@PathVariable("id") long id) {
        Item item = itemService.delete(id);
        rabbitTemplate.setExchange("orderExchange");
        rabbitTemplate.convertAndSend("delete", id);
        return item;
    }

    @PutMapping("/items/{id}")
    public Item alterItem(@PathVariable("id") long id, @RequestBody Item item) {
        item = itemService.alterItem(item, id);
        rabbitTemplate.setExchange("orderExchange");
        rabbitTemplate.convertAndSend("add", item);
        return item;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(Exception ex) {
        logger.error("Illegal argument exception", ex);

        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
