package com.developing.shop.items.model;

import javax.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Column(name = "id", nullable = false)
    @org.springframework.data.annotation.Id
    @GeneratedValue
    @Id
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private int amount;

    @Column(name = "price")
    private long price;

    Item(String name, int amount, long price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
