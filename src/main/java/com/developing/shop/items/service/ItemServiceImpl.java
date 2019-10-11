package com.developing.shop.items.service;

import com.developing.shop.items.model.Item;
import com.developing.shop.items.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository repository;

    @Override
    public Item addItem(Item item) {
        return repository.save(item);
    }

    @Override
    public List<Item> getItems(Map<String, String> params) {
        return repository.findAll();
    }

    @Override
    public Item getItemById(long id) {
        // Todo throw exception if item not found
        return repository.findById(id).orElse(null);
    }

    @Override
    public Item delete(long id) {
        Item item = getItemById(id);
        repository.delete(item);
        return item;
    }

    @Override
    public Item alterItem(Item item, long id) {

        // Todo throw exception if item not found
        Item alteringItem = repository.findById(id).orElse(null);
        alteringItem.alter(item);
        return repository.save(alteringItem);
    }

}
