package fr.alexisbourges.cefimtest.feature.database;

import jakarta.persistence.Tuple;

import java.util.Objects;

public class ProduitDto {
    private Integer id;
    private String name;
    private String description;

    public ProduitDto(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public ProduitDto(Tuple tuple) {
        this.id = (Integer) tuple.get(0);
        this.name = (String) tuple.get(1);
        this.description = (String) tuple.get(2);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProduitDto that = (ProduitDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
