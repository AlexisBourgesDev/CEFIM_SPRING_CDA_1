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
// findById(Integer id) => Récupère la ligne qui a l'id passé en paramètre
public interface ProductRepository extends JpaRepository<Produit, Integer> {
    // Recherche par nom en utilisant la colonne name de Produit
    // Découpage du nom de la méthode pour comprendre :
    // findBy : prefixe commun
    // Name : nom de l'attribut dans la classe Produit
    // Containing : Mot clé permettant de dire comment écrire le where :: ici name = '%iphone%'
    // IgnoreCase : Recherche sans tenir compte de la casse
    List<Produit> findByNameContainingIgnoreCase(String name);

    // Utilisation d'une requête écrite en JPQL, et pas en SQL natif
    // On remplace juste le nom des tables par le nom des classe contenant nos entités
    // On remplace le nom des champs de notre BDD par le nom des attributs
    @Query("select p from Produit p inner join Category c on p.categoryId = c.categoryId where c.categoryName = ?1")
    List<Produit> findByCategoryName(String categoryName);
}
