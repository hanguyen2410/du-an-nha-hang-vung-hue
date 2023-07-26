package com.cg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpbModuleWhiteLotusRestaurantApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpbModuleWhiteLotusRestaurantApplication.class);

    public static void main(String[] args) {
         SpringApplication.run(SpbModuleWhiteLotusRestaurantApplication.class, args);
        logger.info("White Lotus Restaurant Application Started........");
    }

}
