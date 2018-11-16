package hellocloud.pricer.repository.jpa;

import hellocloud.pricer.model.Price;
import hellocloud.pricer.model.entity.PriceEntity;
import hellocloud.pricer.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JPAPriceRepository implements PriceRepository {
    @Autowired
    PriceCrudRepository priceCrudRepository;

    @Override
    public Price get(String symbol) {
        return priceCrudRepository.findById(symbol).map(PriceEntity::toPrice).orElse(null);
    }

    @Override
    public Collection<Price> getAll() {
        List<Price> prices = new ArrayList<>();
        priceCrudRepository.findAll().forEach(p -> prices.add(p.toPrice()));
        return prices;
    }

    @Override
    public void create(Price price) {
        priceCrudRepository.save(new PriceEntity(price));
    }

    @Override
    public void update(Price price) {
        create(price);
    }

    @Override
    public void delete(String symbol) {
        priceCrudRepository.deleteById(symbol);
    }
}
