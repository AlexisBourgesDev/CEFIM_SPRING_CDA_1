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
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class CefimtestApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private DatabaseService databaseService;

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
		List<String> expectedNames = Arrays.asList("iphone", "PS5");

		List<String> listProductNames = databaseService.getListProductNames();

		assert listProductNames.containsAll(expectedNames);
	}

	@Test
	void testProductList(){
		ProduitDto p1 = new ProduitDto(1, "iphone", "portable");
		ProduitDto p2 = new ProduitDto(2, "PS5", "console");

		List<ProduitDto> listProduits = databaseService.getListProduct();

		assert listProduits.containsAll(Arrays.asList(p1, p2));
	}


	@Test
	void contextLoads() {
	}

	@Test
	void testHelloWorld() throws Exception{
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/hello/world").contentType(MediaType.TEXT_PLAIN_VALUE);
		ResultMatcher resultStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher resultContent = MockMvcResultMatchers.content().string("Hello World !!!");
		mockMvc.perform(requestBuilder)
				.andExpect(resultStatus)
				.andExpect(resultContent);
	}



}
