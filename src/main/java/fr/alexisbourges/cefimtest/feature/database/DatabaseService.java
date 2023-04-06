package fr.alexisbourges.cefimtest.feature.database;

import fr.alexisbourges.cefimtest.model.ProductRepository;
import fr.alexisbourges.cefimtest.model.Produit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Bien penser à annoter votre classe de Service par l'annotation @Service (dans les docs, elle est identique à @Component si vous la voyez)
@Service
public class DatabaseService {

    // On injecte notre EntityManager pour pouvoir faire nos requêtes
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

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
        // Requete identique à notre méthode getListProduct, avec le champ unit_price en plus
        String request = "select product_id, name, description, unit_price from produit";
        Query query = entityManager.createNativeQuery(request, Tuple.class);
        List<Tuple> resultList = query.getResultList();
        // IDEM méthode getListProduct, sauf qu'on va créer des ProduitWithPriceDto pour stocker le unit_price de notre requête
        List<ProduitWithPriceDto> collect = resultList.stream().map(ProduitWithPriceDto::new).toList();
        return collect;
    }

    public List<Produit> getListProductFromEntity(){
        // Utilisation du repository pour récupérer toutes les lignes de la BDD
        return productRepository.findAll();
    }

    public ProduitWithPriceDto getOneProductById(Integer id) {
        // Requete identique à notre méthode getListProduct, avec le champ unit_price en plus
        String request = "select product_id, name, description, unit_price from produit where product_id = :productId";
        Query query = entityManager.createNativeQuery(request, Tuple.class)
                // setParameter : Permet de remplacer un paramètre nommé (1er paramètre) dans la requête par la valeur du 2e paramètre (Ici productId)
                // Dans ma requête, un paramètre est identifié par le prefix ":"
                .setParameter("productId", id);
        Tuple result = (Tuple) query.getSingleResult();
        // IDEM méthode getListProduct, sauf qu'on va créer des ProduitWithPriceDto pour stocker le unit_price de notre requête
        return new ProduitWithPriceDto(result);
    }

    public List<ProduitWithPriceDto> getProductByName(String name) {
        String request = "select product_id, name, description, unit_price from produit where name LIKE :name";
        Query query = entityManager.createNativeQuery(request, Tuple.class)
                // setParameter : Permet de remplacer un paramètre nommé (1er paramètre) dans la requête par la valeur du 2e paramètre (Ici productId)
                // Dans ma requête, un paramètre est identifié par le prefix ":"
                .setParameter("name", "%"+name+"%");
        List<Tuple> resultList = (List<Tuple>) query.getResultList();
        // IDEM méthode getListProduct, sauf qu'on va créer des ProduitWithPriceDto pour stocker le unit_price de notre requête
        return resultList.stream().map(ProduitWithPriceDto::new).toList();
    }

    public Produit getOneProductEntity(Integer id) {
        // 2 façons de récupérer un élément par son ID
        // findById : renvoie un optionnel pour gérer le cas où l'element n'existe pas (ici on renvoie null)
        // getReferenceById : renvoie le produit et lève une exception dans le cas où il n'existe pas
        return productRepository.findById(id).orElse(null);
    }

    public List<Produit> getProductEntityByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<ProduitWithPriceDto> getProductByCategory(String categoryName) {
        String request = "select product_id, name, description, unit_price from produit inner join category on category.category_id = produit.category_id where category_name = :categoryName";
        Query query = entityManager.createNativeQuery(request, Tuple.class)
                // setParameter : Permet de remplacer un paramètre nommé (1er paramètre) dans la requête par la valeur du 2e paramètre (Ici productId)
                // Dans ma requête, un paramètre est identifié par le prefix ":"
                .setParameter("categoryName", categoryName);
        List<Tuple> resultList = (List<Tuple>) query.getResultList();
        // IDEM méthode getListProduct, sauf qu'on va créer des ProduitWithPriceDto pour stocker le unit_price de notre requête
        return resultList.stream().map(ProduitWithPriceDto::new).toList();
    }

    public List<Produit> getProductEntityByCategoryName(String name) {
        return productRepository.findByCategoryName(name);
    }
}
