package hellocloud.pricer.service;

import hellocloud.pricer.exception.InvalidRequestException;
import hellocloud.pricer.exception.UnknownResourceException;
import hellocloud.pricer.model.Price;
import hellocloud.pricer.model.PriceRequest;
import hellocloud.pricer.model.transformer.PriceTransformer;
import hellocloud.pricer.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PricerService {
    @Autowired
    private PriceRepository repository;

    private PriceTransformer transformer = new PriceTransformer();

    public void createPrice(PriceRequest priceRequest) {
        validatePriceRequest(priceRequest);
        if (repository.get(priceRequest.getSymbol()) != null) {
            throw new UnknownResourceException(String.format("Price with symbol %s already exists.", priceRequest.getSymbol()));
        }

        repository.create(transformer.transform(priceRequest));
    }

    public Price requestPrice(String symbol) {
        Price price = repository.get(symbol);

        if (null == price) {
            throw new UnknownResourceException(String.format("Price with symbol %s does not exist.", symbol));
        }

        return price;
    }

    public Collection<Price> requestAllPrices() {
        return repository.getAll();
    }

    public void updatePrice(PriceRequest priceRequest) {
        validatePriceRequest(priceRequest);
        if (repository.get(priceRequest.getSymbol()) == null) {
            throw new UnknownResourceException(String.format("Price with symbol %s does not exist.", priceRequest.getSymbol()));
        }

        repository.update(transformer.transform(priceRequest));
    }

    public void deletePrice(String symbol) {
        if (repository.get(symbol) == null) {
            throw new UnknownResourceException(String.format("Price with symbol %s does not exist.", symbol));
        }
        repository.delete(symbol);
    }

    private void validatePriceRequest(PriceRequest priceRequest) {
        if (null == priceRequest.getSymbol()) {
            throw new InvalidRequestException("a symbol is required");
        }
        if (null == priceRequest.getValue()) {
            throw new InvalidRequestException("a value is required");
        }
    }
}
