package fr.alexisbourges.cefimtest.feature.product;

import fr.alexisbourges.cefimtest.model.ProductRepository;
import fr.alexisbourges.cefimtest.model.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Produit updateProduct(Integer productId, Produit produit) {
        Optional<Produit> byId = productRepository.findById(productId);
        if (byId.isPresent()){
            Produit findProduct = byId.get();
            findProduct.setDescription(produit.getDescription());
            return productRepository.save(findProduct);
        }
        return null;
    }
}
