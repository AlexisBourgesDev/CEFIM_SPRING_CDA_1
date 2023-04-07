package fr.alexisbourges.cefimtest.feature.product;

import fr.alexisbourges.cefimtest.model.Produit;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @PostMapping("")
    public Produit insertNewProduct(@RequestBody Produit produit){
        // GET produit.name == "table"
        // SI listProduits IS NOT EMPTY
        ResponseEntity.status(HttpStatus.CONFLICT).body("Product with name table already exists");
        //
        return productService.insertNewProduct(produit);
    }

    @DeleteMapping("/{productId}")
    public void deleteNewProduct(@PathVariable("productId") Integer productId){
        productService.deleteProduct(productId);
    }

    @DeleteMapping("")
    public void deleteNewProduct(@RequestParam("productName") String productName){
        //
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Produit> updateProduct(@RequestBody Produit produit, @PathVariable("productId") Integer productId){
        try{
            return ResponseEntity.ok(productService.updateProduct(productId, produit));
        } catch (EntityNotFoundException notFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ConstraintViolationException constraintViolationException){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


}
