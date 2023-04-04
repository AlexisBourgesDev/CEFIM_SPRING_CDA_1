package fr.alexisbourges.cefimtest.feature.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping(value = "/world", produces = MediaType.TEXT_PLAIN_VALUE)
    public String helloWorld(){
        logger.error("HELLO");
        return "Hello World !!!";
    }

}
