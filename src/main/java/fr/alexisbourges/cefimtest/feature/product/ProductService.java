package fr.alexisbourges.cefimtest.feature.product;

import fr.alexisbourges.cefimtest.model.ProductRepository;
import fr.alexisbourges.cefimtest.model.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
