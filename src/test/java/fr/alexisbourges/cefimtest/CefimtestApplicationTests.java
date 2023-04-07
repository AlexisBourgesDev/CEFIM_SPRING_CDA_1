package fr.alexisbourges.cefimtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.alexisbourges.cefimtest.feature.database.DatabaseService;
import fr.alexisbourges.cefimtest.feature.database.ProduitDto;
import fr.alexisbourges.cefimtest.feature.database.ProduitWithPriceDto;
import fr.alexisbourges.cefimtest.model.Produit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
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

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
	@Autowired
	private ObjectMapper objectMapper;

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
		ProduitDto p2 = new ProduitDto(2, "PS5", null);

		// Récupération de notre liste de produits
		List<ProduitDto> listProduits = databaseService.getListProduct();

		// Comparaison pour vérifier que la liste des produits (p1 et p2) est présente dans la liste récupéré de notre service
		// Pour que cela fonctionne, bien penser à redéfinir la méthode equals() et hashcode()
		// Ici, puisque ce sont des comparaisons d'objets, ce sera la méthode equals() qui sera utilisé
		assert listProduits.containsAll(Arrays.asList(p1, p2));
	}

	@Test
	void testProductListPrices(){
		// Création d'une Map (clé - valeur) avec en clé l'ID de la base de données et en valeur son prix associé
		// Attention : penser à passer un double et pas un long à la classe BigDecimal pour les tests
		Map<Integer, BigDecimal> listPrices = new HashMap<>();
		listPrices.put(1, BigDecimal.valueOf(1000.0));

		// Autre écriture d'une Map (changement de contexte : à l'intérieur des {{ }}, this devient la Map)
		Map<Integer, BigDecimal> listPricesBis = new HashMap<>(){{
			put(1, BigDecimal.valueOf(1000.0));
		}};



		// Récupération de notre liste de produits avec le prix
		List<ProduitWithPriceDto> listProduits = databaseService.getListProductWithPrices();

		assert listProduits.stream()
				// Filtre notre liste de produits avec seulement ceux qui sont contenus dans notre Map de test
				// la fonction filter attend un boolean après "->"
				.filter(produit -> listPrices.containsKey(produit.getId()))
				// Méthode qui permet de faire une vérification de tous les éléments de notre liste
				// Si un seul élément retourne un false à notre condition, allMatch renvoie false
				.allMatch(produit ->
						produit.getUnitPrice().equals(listPrices.get(produit.getId()))
				);
	}

	boolean testEquality(Produit produitEntity, ProduitWithPriceDto produitWithPriceDto){
		return Objects.equals(produitEntity.getName(), produitWithPriceDto.getName()) && Objects.equals(produitEntity.getDescription(), produitWithPriceDto.getDescription())
				&& Objects.equals(BigDecimal.valueOf(produitEntity.getUnitPrice()), produitWithPriceDto.getUnitPrice());
	}

	@Test
	void testProduitFromEntity(){
		// Création de nos tests
		ProduitWithPriceDto p1 = new ProduitWithPriceDto(1, "iphone", "portable", BigDecimal.valueOf(1000.0));
		ProduitWithPriceDto p2 = new ProduitWithPriceDto(2, "PS5", null, BigDecimal.valueOf(500.0));

		// Récupération de ma liste de produit via mes entity
		List<Produit> listProductFromEntity = databaseService.getListProductFromEntity();

		// Test
		assert listProductFromEntity.stream().allMatch(produit -> testEquality(produit, p1) || testEquality(produit, p2));
	}

	@Test
	void testGetIphone(){
		ProduitWithPriceDto p1 = new ProduitWithPriceDto(1, "iphone", "portable", BigDecimal.valueOf(1000.0));

		ProduitWithPriceDto oneProduct = databaseService.getOneProductById(p1.getId());

		assert oneProduct.equals(p1);
	}

	@Test
	void testGetIphoneByName(){
		ProduitWithPriceDto p1 = new ProduitWithPriceDto(1, "iphone", "portable", BigDecimal.valueOf(1000.0));
		ProduitWithPriceDto p2 = new ProduitWithPriceDto(3, "iphone 13", "portable", BigDecimal.valueOf(1200.0));

		List<ProduitWithPriceDto> listProduits = databaseService.getProductByName("iphone");

		assert listProduits.stream().allMatch(produit -> produit.equals(p1) || produit.equals(p2));
	}

	@Test
	void testGetIphoneByCategory(){
		ProduitWithPriceDto p1 = new ProduitWithPriceDto(1, "iphone", "portable", BigDecimal.valueOf(1000.0));
		ProduitWithPriceDto p2 = new ProduitWithPriceDto(3, "iphone 13", "portable", BigDecimal.valueOf(1200.0));

		List<ProduitWithPriceDto> listProduits = databaseService.getProductByCategory("Mobile");

		assert listProduits.stream().allMatch(produit -> produit.equals(p1) || produit.equals(p2));
	}

	@Test
	void testGetIphoneEntitiesByName(){
		ProduitWithPriceDto p1 = new ProduitWithPriceDto(1, "iphone", "portable", BigDecimal.valueOf(1000.0));
		ProduitWithPriceDto p2 = new ProduitWithPriceDto(3, "iphone 13", "portable", BigDecimal.valueOf(1200.0));

		List<Produit> listProduits = databaseService.getProductEntityByName("iphone");

		assert listProduits.stream().allMatch(produit -> testEquality(produit, p1) || testEquality(produit, p2));
	}

	@Test
	void testGetIphoneEntitiesByCategoryName(){
		ProduitWithPriceDto p1 = new ProduitWithPriceDto(1, "iphone", "portable", BigDecimal.valueOf(1000.0));
		ProduitWithPriceDto p2 = new ProduitWithPriceDto(3, "iphone 13", "portable", BigDecimal.valueOf(1200.0));

		List<Produit> listProduits = databaseService.getProductEntityByCategoryName("mobile");

		assert listProduits.stream().allMatch(produit -> testEquality(produit, p1) || testEquality(produit, p2));
	}

	@Test
	void testGetIphoneFromEntity(){
		ProduitWithPriceDto p1 = new ProduitWithPriceDto(1, "iphone", "portable", BigDecimal.valueOf(1000.0));

		Produit oneProduct = databaseService.getOneProductEntity(p1.getId());

		assert testEquality(oneProduct, p1);
	}

	// CRUD

	@Test
	void testInsertProduit() throws Exception {
		ProduitWithPriceDto p1 = new ProduitWithPriceDto(null, "table", "table basique", BigDecimal.valueOf(100.0));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/product").content("{\"name\": \"table\",\n" +
				"\"description\" : \"table basique\",\n" +
				"\"unitPrice\": 100.0,\n" +
				"\"categoryId\": 3}").contentType(MediaType.APPLICATION_JSON);

		ResultMatcher resultStatus = MockMvcResultMatchers.status().isOk();

		ResultMatcher resultNameProduct = MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(p1));


		JsonNode contentResponse = new ObjectMapper().readTree(mockMvc.perform(requestBuilder)
				.andExpect(resultStatus)
				.andReturn().getResponse().getContentAsString());


		Assertions.assertEquals("table2", contentResponse.get("name").asText());

	}

	@Test
	void testDeleteProduct() throws Exception{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/product/5");
		ResultMatcher resultStatus = MockMvcResultMatchers.status().isOk();

		mockMvc.perform(requestBuilder)
				.andExpect(resultStatus);
	}

	@Test
	void testDeleteProductByName() throws Exception{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/product?productName=table");
		ResultMatcher resultStatus = MockMvcResultMatchers.status().isOk();

		mockMvc.perform(requestBuilder)
				.andExpect(resultStatus);
	}

	@Test
	void testUpdateProduct() throws Exception{

		Assertions.assertNull(databaseService.getOneProductById(2).getDescription());

		Map<String, String> listFields = new HashMap<>(){{
			put("description", "console");
			put("name", "Playstation 5");
		}};

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/api/product/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(listFields));
		ResultMatcher resultStatus = MockMvcResultMatchers.status().isOk();

		JsonNode resultat = objectMapper.readTree(mockMvc.perform(requestBuilder)
				.andExpect(resultStatus)
				.andReturn().getResponse().getContentAsString());

		Assertions.assertEquals(resultat.get("description").asText(), listFields.get("description"));
		Assertions.assertEquals(resultat.get("name").asText(), listFields.get("name"));
	}

	@Test
	void testUpdateProductWithWrongId() throws Exception{
		Map<String, String> listFields = new HashMap<>(){{
			put("description", "console");
			put("name", "Playstation 5");
		}};

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/api/product/10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(listFields));
		ResultMatcher resultStatus = MockMvcResultMatchers.status().isNotFound();

		mockMvc.perform(requestBuilder)
				.andExpect(resultStatus);
	}

	@Test
	void testUpdateProductWithExistingName() throws Exception{
		Map<String, String> listFields = new HashMap<>(){{
			put("description", "console");
			put("name", "iphone");
		}};

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/api/product/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(listFields));
		ResultMatcher resultStatus = MockMvcResultMatchers.status().isConflict();

		mockMvc.perform(requestBuilder)
				.andExpect(resultStatus);
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
