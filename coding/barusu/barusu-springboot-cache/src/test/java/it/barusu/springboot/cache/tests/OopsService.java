package it.barusu.springboot.cache.tests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OopsService {

    @Cacheable(value = "cn.oops", key = "#name")
    public String oops(String name) {
        log.info("oops...");
        return "oops! " + name;
    }

    @Cacheable(value = "cn.oopsAndResultIsNull", key = "#name")
    public String oopsAndResultIsNull(String name) {
        log.info("oops and result is null...");
        return null;
    }


}
