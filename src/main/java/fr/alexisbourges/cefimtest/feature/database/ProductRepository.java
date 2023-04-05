package fr.alexisbourges.cefimtest.feature.database;

import org.springframework.data.jpa.repository.JpaRepository;

// Le repository permet de faire le lien avec la BDD en proposant des méthodes SQL de base
// JpaRepository prend 2 types entre chevrons :
// Le premier est la classe contenant l'entité cible
// Le deuxième contient le type de la clé primaire (toujours type objet, pas de type primitif)

// Méthode utile (vu pour l'instant) :
// findAll() => Récupère toutes les lignes de la base de données
public interface ProductRepository extends JpaRepository<Produit, Integer> {
}
