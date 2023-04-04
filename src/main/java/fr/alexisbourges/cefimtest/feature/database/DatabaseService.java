package fr.alexisbourges.cefimtest.feature.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    @Autowired
    private EntityManager entityManager;
    public List<String> getListProductNames(){
        String request = "select name from produit";
        Query query = entityManager.createNativeQuery(request, Tuple.class);
        List<Tuple> resultList = query.getResultList();
        List<String> collect = resultList.stream().map(tuple -> (String) tuple.get(0)).collect(Collectors.toList());

        Query nativeQuery2 = entityManager.createNativeQuery(request);
        List<String> resultList1 = (List<String>) nativeQuery2.getResultList();

        return collect;
        // TODO
    }

    public List<ProduitDto> getListProduct(){
        return null;
    }
}
