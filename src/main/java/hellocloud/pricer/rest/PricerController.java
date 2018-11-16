package hellocloud.pricer.rest;

import hellocloud.pricer.model.Price;
import hellocloud.pricer.model.PriceRequest;
import hellocloud.pricer.service.PricerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "prices")
@Api(value = "prices", description = "Pricer Operations")
public class PricerController {
    private static Logger LOGGER = LoggerFactory.getLogger(PricerController.class);

    @Autowired
    private PricerService pricerService;

    @RequestMapping(method = RequestMethod.POST, consumes = {APPLICATION_JSON_VALUE})
    @ApiOperation("Creates a new price")
    public ResponseEntity<Void> createPrice(@RequestBody PriceRequest price) {
        LOGGER.info("createPrice " + price);

        pricerService.createPrice(price);

        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{symbol}", method = RequestMethod.GET)
    @ApiOperation("Requests an existing price")
    public ResponseEntity<Price> requestPrice(@PathVariable("symbol") String symbol) {
        LOGGER.info("requestPrice for symbol " + symbol);

        return new ResponseEntity<Price>(pricerService.requestPrice(symbol), OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Requests all existing prices")
    public ResponseEntity<Collection<Price>> requestAllPrices() {
        LOGGER.info("requestAllPrices");

        return new ResponseEntity<Collection<Price>>(pricerService.requestAllPrices(), OK);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = {APPLICATION_JSON_VALUE})
    @ApiOperation("Updates an existing price")
    public ResponseEntity<Void> updatePrice(@RequestBody PriceRequest priceRequest) {
        LOGGER.info("updatePrice " + priceRequest);

        pricerService.updatePrice(priceRequest);

        return new ResponseEntity<Void>(OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{symbol}")
    @ApiOperation("Deletes an existng price")
    public ResponseEntity<Void> deletePrice(@PathVariable("symbol") String symbol) {
        LOGGER.info("deletePrice for symbol " + symbol);

        pricerService.deletePrice(symbol);

        return new ResponseEntity<Void>(OK);
    }
}
