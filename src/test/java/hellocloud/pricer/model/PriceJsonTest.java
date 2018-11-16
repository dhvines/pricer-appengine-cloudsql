package hellocloud.pricer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceJsonTest {
    @Test
    public void serialize() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        Price price = new Price("ABC", new BigDecimal("23.45"), LocalDateTime.now());
        String json = objectMapper.writeValueAsString(price);

        Price deserialized = objectMapper.readValue(json, Price.class);

        Assert.assertEquals(price, deserialized);
    }
}
