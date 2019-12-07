package com.developing.shop.items.repository;

import com.developing.shop.items.model.LogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogItemRepository extends JpaRepository<LogItem, Long> {
    @Query(value = "select i from LogItem i where order_id=:order_id and item_id=:item_id")
    LogItem getByCompositeKey(@Param("item_id") long itemId, @Param("order_id") long orderId);

    @Query(value = "delete from LogItem i where order_id=:order_id and item_id=:item_id")
    void deleteByCompositeKey(@Param("item_id") long itemId, @Param("order_id") long orderId);
}
