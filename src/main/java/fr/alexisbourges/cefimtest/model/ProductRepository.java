package fr.alexisbourges.cefimtest.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Le repository permet de faire le lien avec la BDD en proposant des méthodes SQL de base
// JpaRepository prend 2 types entre chevrons :
// Le premier est la classe contenant l'entité cible
// Le deuxième contient le type de la clé primaire (toujours type objet, pas de type primitif)

// Méthode utile (vu pour l'instant) :
// findAll() => Récupère toutes les lignes de la base de données
public interface ProductRepository extends JpaRepository<Produit, Integer> {
    List<Produit> findByNameContainingIgnoreCase(String name);

    @Query("select p from Produit p inner join Category c on p.categoryId = c.categoryId where c.categoryName = ?1")
    List<Produit> findByCategoryName(String categoryName);
}
