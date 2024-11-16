/* (C) 2024 */
package com.objectvault.objectvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
public class ObjectvaultApplication {

  public static void main(String[] args) {
    SpringApplication.run(ObjectvaultApplication.class, args);
  }
}
