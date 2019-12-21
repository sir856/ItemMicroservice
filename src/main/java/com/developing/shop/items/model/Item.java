package com.developing.shop.items.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "item")
public class Item implements Serializable {

    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column
    private String name;

    @Column
    private Integer amount;

    @Column
    private Long price;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "item")
    @JsonProperty
    private List<LogItem> logItems;

    Item() {
    }

    Item(String name, Integer amount, Long price) {
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

    public void decreaseAmount(int amount) {
        if (this.amount >= amount) {
            this.amount -= amount;
        }
        else {
            throw new IllegalArgumentException("Lacks " + (amount - this.amount) + " items with id : " + this.id );
        }
    }
    public void addLogItem(LogItem item) {
        this.logItems.add(item);
    }

    public void change(final Item item) {
        this.name = item.name == null ? this.name : item.name;
        this.amount = item.amount == null ? this.amount : item.amount;
        this.price = item.price == null ? this.price : item.price;
    }
}
