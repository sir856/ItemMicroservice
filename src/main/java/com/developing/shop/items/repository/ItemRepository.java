package com.developing.shop.items.repository;

import com.developing.shop.items.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {
}
