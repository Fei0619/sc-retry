package com.test.instance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 费世程
 * @date 2020/7/17 15:12
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.test")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
