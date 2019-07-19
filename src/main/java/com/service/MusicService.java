package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "instrument")
public class MusicService {

    private static Logger log = LoggerFactory.getLogger(MusicService.class);

    @CacheEvict(allEntries = true)
    public void clearCache(){}

    @Cacheable(condition = "#instrument.equals('trombone')")
    public String play(String instrument) {
        log.info("Executing: " + this.getClass().getSimpleName() + ".play(\"" + instrument + "\");");
        return "paying " + instrument + "!";
    }

}