package com.anji.tmsexpand;

import javax.websocket.server.PathParam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TmsExpandApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmsExpandApplication.class, args);
	}
	
	@RequestMapping("/")
    String index(@PathParam(value = "a") String a, @PathParam(value = "b") String b){
      return a + "," + b;
    }
}
