package fr.alexisbourges.cefimtest.model;

import jakarta.persistence.*;

// 2 annotations par entité :
// @Entity : Permet de faire le lien entre classe et BDD
// @Table : Permet de préciser le nom de la table à mapper
@Entity
@Table(name = "produit")
public class Produit {

    // Définit l'ID de notre BDD (clé primaire)
    // @Id : Permet de définir la clé primaire
    // @GeneratedValue : Permet de préciser la génération de la clé primaire
    // Strategy IDENTITY : Permet de préciser que la génération se fait à partir d'une colonne spécifique en BDD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    // @Column : Permet de mapper l'attribut à son champ en BDD
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "unitPrice")
    private Double unitPrice;

    @Column(name = "category_id")
    private Integer categoryId;

    @ManyToOne
    @JoinColumn(name = "category_id", updatable = false, insertable = false)
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Produit() {
    }
}
