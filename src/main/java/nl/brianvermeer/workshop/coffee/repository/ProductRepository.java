package nl.brianvermeer.workshop.coffee.repository;

import nl.brianvermeer.workshop.coffee.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Serializable> {

    public Product findProductByProductName(String productName);

    public List<Product> findProductByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String desc);

}
