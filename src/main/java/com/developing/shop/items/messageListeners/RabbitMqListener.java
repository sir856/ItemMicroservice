package com.developing.shop.items.messageListeners;

import com.developing.shop.items.model.Item;
import com.developing.shop.items.model.LogItem;
import com.developing.shop.items.repository.LogItemRepository;
import com.developing.shop.items.repository.ItemRepository;
import com.developing.shop.orders.model.ChosenItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final LogItemRepository logItemRepository;
    private final ItemRepository itemRepository;

    @Autowired
    RabbitMqListener(LogItemRepository logItemRepository, ItemRepository itemRepository) {
        this.logItemRepository = logItemRepository;
        this.itemRepository = itemRepository;
    }

    @RabbitListener(queues = "addItemToOrder")
    public void itemAddedToOrderHandler(ChosenItem chosenItem) {
        Item item = itemRepository.findById(chosenItem.getItem().getId()).orElseThrow(
                () -> new IllegalArgumentException("No item with id: " + chosenItem.getItem().getId()));

        LogItem logItem = logItemRepository.getByCompositeKey(chosenItem.getItem().getId(), chosenItem.getOrder().getId());
        if (logItem != null) {
            item.changeAmount(-logItem.getAmount());
        }
        item.changeAmount(chosenItem.getAmount());

        logItemRepository.save(new LogItem(chosenItem.getOrder().getId(), item, chosenItem.getAmount()));
    }

    @RabbitListener(queues = "deleteItemFromOrder")
    public void itemDeletedFromOrderHandler(ChosenItem chosenItem) {
        logItemRepository.deleteByCompositeKey(chosenItem.getItem().getId(), chosenItem.getOrder().getId());
    }


}