package uz.pdp.springsecuritydatabseemailauditing.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.springsecuritydatabseemailauditing.entity.Product;

import java.util.UUID;

@RepositoryRestResource(path = "product")
public interface ProductRepository extends CrudRepository<Product, UUID> {
}
