package hellocloud.pricer.model.transformer;

import hellocloud.pricer.model.Price;
import hellocloud.pricer.model.PriceRequest;

import java.time.LocalDateTime;

public class PriceTransformer {
    public Price transform(PriceRequest priceRequest) {
        return new Price(priceRequest.getSymbol(), priceRequest.getValue(), LocalDateTime.now());
    }
}
