package fr.alexisbourges.cefimtest.feature.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Produit, Integer> {
}
