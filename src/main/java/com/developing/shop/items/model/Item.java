package com.developing.shop.items.model;

import javax.persistence.*;

@Entity
@Table(name = "item")
public class Item {

    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "price")
    private Long price;

    Item(String name, Integer amount, Long price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }
    Item(){

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

    public void alter(final Item item) {
        this.name = item.name == null ? this.name : item.name;
        this.amount = item.amount == null ? this.amount : item.amount;
        this.price = item.price == null ? this.price : item.price;
    }
}
