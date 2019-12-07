package com.developing.shop.items.service;

import com.developing.shop.items.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    Item addItem(Item item);

    List<Item> getItems(Map<String, String> params);

    Item getItemById(long id);

    Item delete(long id);

    Item alterItem(Item item, long id);
}
