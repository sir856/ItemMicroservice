package com.developing.shop.items.service;

import com.developing.shop.items.model.Item;
import com.developing.shop.items.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class ItemServiceImpl implements ItemService {
    public String ORDER_BY = "orderBy";
    public String NAME = "name";
    public String AMOUNT = "amount";
    public String AMOUNT_FROM = "amountFrom";
    public String AMOUNT_TO = "amountTo";
    public String PRICE = "price";
    public String PRICE_FROM = "priceFrom";
    public String PRICE_TO = "priceTo";

    private final ItemRepository repository;

    private final EntityManager em;

    private CriteriaBuilder cb;
    private CriteriaQuery<Item> cq;

    private Map<String, Function<String, Predicate>> predicatesMap;
    private Map<String, Order> ordersMap;

    @Autowired
    public ItemServiceImpl(EntityManager em, ItemRepository repository) {
        this.em = em;
        this.repository = repository;

        this.cb = em.getCriteriaBuilder();
        this.cq = cb.createQuery(Item.class);
        Root<Item> root = cq.from(Item.class);

        predicatesMap = new HashMap<>();

        predicatesMap.put(NAME, name -> cb.like(root.get(NAME), name + "%"));
        predicatesMap.put(AMOUNT, amount -> cb.equal(root.get(AMOUNT), amount));
        predicatesMap.put(PRICE, price -> cb.equal(root.get(PRICE), price));
        predicatesMap.put(AMOUNT_FROM, amountFrom -> cb.ge(root.get(AMOUNT), Long.valueOf(amountFrom)));
        predicatesMap.put(PRICE_FROM, priceFrom -> cb.ge(root.get(PRICE), Long.valueOf(priceFrom)));
        predicatesMap.put(AMOUNT_TO, amountTo -> cb.le(root.get(AMOUNT), Long.valueOf(amountTo)));
        predicatesMap.put(PRICE_TO, priceTo -> cb.le(root.get(PRICE), Long.valueOf(priceTo)));

        ordersMap = new HashMap<>();
        ordersMap.put(NAME, cb.asc(root.get(NAME)));
        ordersMap.put(PRICE, cb.asc(root.get(PRICE)));
        ordersMap.put(AMOUNT, cb.asc(root.get(AMOUNT)));
        ordersMap.put("-" + NAME, cb.desc(root.get(NAME)));
        ordersMap.put("-" + PRICE, cb.desc(root.get(PRICE)));
        ordersMap.put("-" + AMOUNT, cb.desc(root.get(AMOUNT)));
    }

    @Override
    public Item addItem(Item item) {
        return repository.save(item);
    }

    @Override
    public List<Item> getItems(Map<String, String> params) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();

        if (params != null) {

            if (params.containsKey(ORDER_BY)) {
                orders = getOrders(params.remove(ORDER_BY));
            }

            normalizeParams(params);

            for (String key : params.keySet() ) {
                if (predicatesMap.containsKey(key)) {
                    predicates.add(predicatesMap.get(key).apply(params.get(key)));
                }
            }
        }

        cq.where(predicates.toArray(new Predicate[0])).orderBy(orders.toArray(new Order[0]));
        return em.createQuery(cq).getResultList();
    }

    private ArrayList<Order> getOrders(String orders) {
        ArrayList<Order> result = new ArrayList<>();

        for (String orderStr : orders.split(",")) {
            orderStr = orderStr.replaceAll("\\s", "");
            if (ordersMap.containsKey(orderStr)) {
                result.add(ordersMap.get(orderStr));
            }
        }
        return result ;
    }

    private void normalizeParams(Map<String, String> params) {
        if (params.containsKey(PRICE)) {
            params.remove(PRICE_TO);
            params.remove(PRICE_FROM);
        }

        if (params.containsKey(AMOUNT)) {
            params.remove(AMOUNT_TO);
            params.remove(AMOUNT_FROM);
        }
    }

    @Override
    public Item getItemById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Item delete(long id) {
        Item item = getItemById(id);
        repository.delete(item);
        return item;
    }

    @Override
    public Item alterItem(Item item, long id) {
        Item alteringItem = repository.findById(id).orElse(null);
        alteringItem.alter(item);
        return repository.save(alteringItem);
    }

}
