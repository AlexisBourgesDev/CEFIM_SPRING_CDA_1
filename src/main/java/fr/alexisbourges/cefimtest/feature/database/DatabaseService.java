package fr.alexisbourges.cefimtest.feature.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Bien penser à annoter votre classe de Service par l'annotation @Service (dans les docs, elle est identique à @Component si vous la voyez)
@Service
public class DatabaseService {

    // On injecte notre EntityManager pour pouvoir faire nos requêtes
    @Autowired
    private EntityManager entityManager;
    public List<String> getListProductNames(){
        // On commence par écrire notre requête (penser à l'executer directement dans une console
        // ou dans phpMyAdmin pour éviter les problèmes de requête très verbeux
        String request = "select name from produit";
        // On créé ensuite notre Query qui prend en paramètre notre requete (String) et le type de résultat (en général Tuple.class)
        Query query = entityManager.createNativeQuery(request, Tuple.class);
        // On demande ensuite à notre query de nous donner la liste des lignes de notre requête et de les transformer en Tuple
        // Voyez un Tuple comme une liste de taille fixe et en lecture seule !!!
        List<Tuple> resultList = query.getResultList();
        // Ensuite, on utilise les stream qui nous permettent de faire des actions sur notre List très facilement
        // Commencer par utiliser .stream() sur votre liste pour voir accès à plein de méthodes très utiles
        // map() : méthode qui vous permet d'appliquer une transformation sur chaque élément de votre liste :: ici transformer votre tuple en String
        // en récupérant le premier champ de votre requête et en pensant bien à le caster en String en ajoutant "(String)" devant
        // Il vous suffit juste ensuite de déterminer le format de sortie : ici une liste donc .toList()
        List<String> collect = resultList.stream().map(tuple -> (String) tuple.get(0)).toList();

        // On peut, puisque qu'il s'agit d'une requête avec un seul élément, caster notre liste de lignes directement en List<String>
        // puisque le seul champ de notre requête est une String
        Query nativeQuery2 = entityManager.createNativeQuery(request);
        List<String> resultList1 = (List<String>) nativeQuery2.getResultList();

        return collect;
    }

    public List<ProduitDto> getListProduct(){
        // IDEM que pour les noms
        String request = "select product_id, name, description from produit";
        Query query = entityManager.createNativeQuery(request, Tuple.class);
        List<Tuple> resultList = query.getResultList();
        // La seule différence est qu'au lieu de transformer notre Tuple en String, on le transforme dans notre classe de données ProduitDto
        // La notation "ProduitDto::new" est identique à "tuple -> new ProduitDto(tuple)" dans la méthode map()
        List<ProduitDto> collect = resultList.stream().map(ProduitDto::new).toList();
        return collect;
    }

    public List<ProduitWithPriceDto> getListProductWithPrices(){
        // IDEM que pour les noms
        String request = "select product_id, name, description, unit_price from produit";
        Query query = entityManager.createNativeQuery(request, Tuple.class);
        List<Tuple> resultList = query.getResultList();
        // La seule différence est qu'au lieu de transformer notre Tuple en String, on le transforme dans notre classe de données ProduitDto
        // La notation "ProduitDto::new" est identique à "tuple -> new ProduitDto(tuple)" dans la méthode map()
        List<ProduitWithPriceDto> collect = resultList.stream().map(ProduitWithPriceDto::new).toList();
        return collect;
    }
}
