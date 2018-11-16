package hellocloud.pricer.repository.jpa;

import hellocloud.pricer.model.entity.PriceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceCrudRepository extends CrudRepository<PriceEntity, String> {
}
