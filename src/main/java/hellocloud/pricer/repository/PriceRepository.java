package hellocloud.pricer.repository;

import hellocloud.pricer.model.Price;

import java.util.Collection;

public interface PriceRepository {
    Price get(String symbol);

    Collection<Price> getAll();

    void create(Price price);

    void update(Price price);

    void delete(String symbol);
}
