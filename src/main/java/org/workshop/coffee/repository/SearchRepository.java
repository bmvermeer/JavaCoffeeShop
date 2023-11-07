package org.workshop.coffee.repository;

import org.workshop.coffee.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;

@Repository
public class SearchRepository {

    @Autowired
    EntityManager em;

    @Autowired
    DataSource dataSource;

    public List<Product> searchProduct (String input) {
        var lowerInput = input.toLowerCase(Locale.ROOT);

        // create a query matching input to description and product_name
        var query = em.createNativeQuery("Select * from Product where lower(description) like '%" + lowerInput + "%' OR lower(product_name) like '%" + lowerInput + "%'", Product.class);
        // instead of the above dangerous query, use a named parameter input to description and product_name
        // var query = em.createNativeQuery("Select * from Product where lower(description) like :input OR lower(product_name) like :input", Product.class);

        // set the named parameter input to the value of lowerInput
        // query.setParameter("input", "%" + lowerInput + "%");

        // execute the query and return the result
        var resultList = (List<Product>) query.getResultList();
        return resultList;
    }

}
