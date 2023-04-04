package fr.alexisbourges.cefimtest;

import fr.alexisbourges.cefimtest.feature.database.DatabaseService;
import fr.alexisbourges.cefimtest.feature.database.ProduitDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class CefimtestApplicationTests {

	// Auto instanciation via @Autowired
	@Autowired
	private MockMvc mockMvc;
	// Classe pour faire des requêtes avec la base de données
	@Autowired
	private EntityManager entityManager;
	// Classe de Service
	@Autowired
	private DatabaseService databaseService;

	// LOGGER pour écrire des logs (bien prendre le package slf4j)
	private Logger logger = LoggerFactory.getLogger(CefimtestApplicationTests.class);
	@Test
	void testDatabase(){
		Query query = entityManager.createNativeQuery("show tables;");
		List<String> results = ((List<String>) query.getResultList());
		String resultList = String.join(" - ", results);
		logger.info("Connexion à la BDD :: SUCCESS");
		logger.info("Table list of databases = [{}]", resultList);
	}

	@Test
	void testProductNames(){
		// Création d'une liste de noms de produits (tiré en dur de la BDD)
		// Arrays.asList prend une liste infinie de String et créé une List<String>
		List<String> expectedNames = Arrays.asList("iphone", "PS5");

		// Récupération du résultat de notre traitement
		List<String> listProductNames = databaseService.getListProductNames();

		// Comparaison pour vérifier que notre liste de noms en dur est bien contenu dans le résultat
		assert listProductNames.containsAll(expectedNames);
	}

	@Test
	void testProductList(){
		// Création d'instances de notre classe avec des valeurs extraites de la BDD en dur
		// DTO -> Data Transfert Object => Objet utilisé pour transférer des données entre différentes parties de l'application
		// Il s'agit juste d'un suffixe pour préciser qu'il ne s'agit pas d'une classe métier ou d'une entité ...
		ProduitDto p1 = new ProduitDto(1, "iphone", "portable");
		ProduitDto p2 = new ProduitDto(2, "PS5", "console");

		// Récupération de notre liste de produits
		List<ProduitDto> listProduits = databaseService.getListProduct();

		// Comparaison pour vérifier que la liste des produits (p1 et p2) est présente dans la liste récupéré de notre service
		// Pour que cela fonctionne, bien penser à redéfinir la méthode equals() et hashcode()
		// Ici, puisque ce sont des comparaisons d'objets, ce sera la méthode equals() qui sera utilisé
		assert listProduits.containsAll(Arrays.asList(p1, p2));
	}


	@Test
	void contextLoads() {
	}

	@Test
	void testHelloWorld() throws Exception{
		// Création de notre requête
		RequestBuilder requestBuilder =
				//get : Prend le chemin du point d'API
				MockMvcRequestBuilders.get("/api/hello/world")
						//Précise le type de contenu attendu en réponse
						.contentType(MediaType.TEXT_PLAIN_VALUE);
		// Test pour vérifier que le statut de la réponse est bien 200
		ResultMatcher resultStatus = MockMvcResultMatchers.status().isOk();
		// Test pour vérifier que le contenu du body est bien : Hello World !!!
		ResultMatcher resultContent = MockMvcResultMatchers.content().string("Hello World !!!");
		// perform prend votre RequestBuilder pour simuler votre requête
		mockMvc.perform(requestBuilder)
				// Il suffit juste ensuite de chainer tout vos ResultMatcher pour faire vos tests sur la réponse
				.andExpect(resultStatus)
				.andExpect(resultContent);
	}



}
