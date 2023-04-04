package fr.alexisbourges.cefimtest.feature.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController : permet d'indiquer à Spring qu'il s'agit d'un Controller, d'une liste de points d'API (1 par méthode)
@RestController
// @RequestMapping : Permet de préfixer tous les points d'API contenu dans cette classe
// Pour la méthode helloWorld(), le point d'API sera donc "/api/hello/world"
@RequestMapping("/api/hello")
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(HelloController.class);

    // @GetMapping : Permet de créer un nouveau point d'entrée qui va utiliser le GET
    // Paramètre value : chemin du point d'API (qui sera concaténé à la valeur de RequestMapping si défini)
    // Paramètre produces : Permet, si besoin, de spécifier explicitement le type de contenu de la réponse
    @GetMapping(value = "/world", produces = MediaType.TEXT_PLAIN_VALUE)
    public String helloWorld(){
        logger.error("HELLO");
        return "Hello World !!!";
    }

}
