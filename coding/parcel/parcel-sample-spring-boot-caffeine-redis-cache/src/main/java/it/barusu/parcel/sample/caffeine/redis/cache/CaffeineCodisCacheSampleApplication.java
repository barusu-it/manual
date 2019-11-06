package it.barusu.parcel.sample.caffeine.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CaffeineCodisCacheSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaffeineCodisCacheSampleApplication.class, args);
    }
}
