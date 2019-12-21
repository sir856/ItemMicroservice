package com.developing.shop.items.messageListeners.data;

import java.io.Serializable;

public class MessageChosenItem implements Serializable {
    private long itemId;
    private long orderId;
    private int amount;

    public MessageChosenItem(long itemId, long orderId, int amount) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.amount = amount;
    }

    public long getItemId() {
        return itemId;
    }

    public long getOrderId() {
        return orderId;
    }

    public int getAmount() {
        return amount;
    }
}
