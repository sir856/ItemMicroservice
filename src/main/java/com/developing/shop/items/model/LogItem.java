package com.developing.shop.items.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "logItem")
@IdClass(LogItemIdClass.class)
public class LogItem implements Serializable {

    @Column(nullable = false)
    @Id
    private long orderId;

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "itemId", nullable = false)
    @JsonIgnoreProperties({"logItems", "price", "amount"})
    private Item item;

    @Column
    private int amount;

    @Column
    private long price;

    public LogItem(long orderId, Item item, int amount) {
        this.orderId = orderId;
        this.item = item;
        this.amount = amount;
        this.price = item.getPrice();
    }

    LogItem() {
    }

    public long getOrderId() {
        return orderId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}