package fr.alexisbourges.cefimtest.feature.product;

import fr.alexisbourges.cefimtest.model.ProductRepository;
import fr.alexisbourges.cefimtest.model.Produit;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public Produit insertNewProduct(Produit produit){
        return productRepository.save(produit);
    }
    public void deleteProduct(Integer produitId){
        productRepository.deleteById(produitId);
    }

    public Produit updateProduct(Integer productId, Produit produit) throws EntityNotFoundException, ConstraintViolationException {
        Optional<Produit> byId = productRepository.findById(productId);

        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Produit with ID %d not exist".formatted(productId));
        }

        List<Produit> byName = productRepository.findByNameContainingIgnoreCase(produit.getName());
        if (byName.size() > 0) {
            throw new ConstraintViolationException("Name for product " + produit.getName() + " already exist", new SQLException(), "produit::name INDEX UNIQUE");
        }

        Produit findProduct = byId.get();
        findProduct.setName(produit.getName());
        findProduct.setDescription(produit.getDescription());
        return productRepository.save(findProduct);
    }
}
