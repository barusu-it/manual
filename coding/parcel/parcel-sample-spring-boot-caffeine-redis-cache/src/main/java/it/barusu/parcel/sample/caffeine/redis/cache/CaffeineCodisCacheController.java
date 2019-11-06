package it.barusu.parcel.sample.caffeine.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("codis/cache")
public class CaffeineCodisCacheController {

    private CaffeineCodisCacheService caffeineCodisCacheService;

    public CaffeineCodisCacheController(CaffeineCodisCacheService caffeineCodisCacheService) {
        this.caffeineCodisCacheService = caffeineCodisCacheService;
    }

    @RequestMapping(value = "hi", method = RequestMethod.GET)
    public String hi(@RequestParam("name") String name) {
        return caffeineCodisCacheService.hi(name);
    }

    @RequestMapping(value = "bye", method = RequestMethod.POST)
    public String bye(@RequestParam("name") String name) {
        return caffeineCodisCacheService.bye(name);
    }

}
