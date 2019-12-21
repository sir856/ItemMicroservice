package com.developing.shop.items.messageListeners;

import com.developing.shop.items.messageListeners.data.MessageChosenItem;
import com.developing.shop.items.model.Item;
import com.developing.shop.items.model.LogItem;
import com.developing.shop.items.repository.LogItemRepository;
import com.developing.shop.items.repository.ItemRepository;
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
    public void itemAddedToOrderHandler(MessageChosenItem chosenItem) {
        Item item = itemRepository.findById(chosenItem.getItemId()).orElseThrow(
                () -> new IllegalArgumentException("No item with id: " + chosenItem.getItemId()));

        LogItem logItem = logItemRepository.getByCompositeKey(chosenItem.getItemId(), chosenItem.getOrderId());
        if (logItem != null) {
            item.decreaseAmount(-logItem.getAmount());
        }
        item.decreaseAmount(chosenItem.getAmount());

        logItemRepository.save(new LogItem(chosenItem.getOrderId(), item, chosenItem.getAmount()));
    }

    @RabbitListener(queues = "deleteItemFromOrder")
    public void itemDeletedFromOrderHandler(MessageChosenItem chosenItem) {
        Item item = itemRepository.findById(chosenItem.getItemId()).orElseThrow(()
                -> new IllegalArgumentException("Wrong chosen item" ));
        item.decreaseAmount(-chosenItem.getAmount());
        itemRepository.save(item);
        logItemRepository.deleteByCompositeKey(chosenItem.getItemId(), chosenItem.getOrderId());
    }

    @RabbitListener(queues = "cancelToItem")
    public void cancelOrder(long id) {
        for (LogItem logItem : logItemRepository.getLogItemsFromOrder(id)) {
            Item item = itemRepository.findById(logItem.getItem().getId()).orElseThrow(()
                    -> new IllegalArgumentException("Wrong chosen item" ));
            if (!logItem.isCancelled()) {
                logItem.getItem().decreaseAmount(-logItem.getAmount());
                logItem.setCancelled(true);
                logItemRepository.save(logItem);
            }
        }
    }


}
