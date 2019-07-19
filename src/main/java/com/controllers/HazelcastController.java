package com.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@RestController
@RequestMapping("/hazelcast")
public class HazelcastController {

    private final HazelcastInstance hazelcastInstance;

    @Autowired
    HazelcastController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostMapping(value = "/write-data")
    public String writeDataToHazelcast(@RequestParam String key, @RequestParam String value) {
        IMap<String, String> hazelcastMap = hazelcastInstance.getMap("my-map");
        hazelcastMap.lock(key);
        hazelcastMap.put(key, value);
        hazelcastMap.unlock(key);
        return "Data is stored.";
    }

    @GetMapping(value = "/read-data")
    public String readDataFromHazelcast(@RequestParam String key) {
        Map<String, String> hazelcastMap = hazelcastInstance.getMap("my-map");
        return hazelcastMap.get(key);
    }

    @GetMapping(value = "/read-all-data")
    public Map<String, String> readAllDataFromHazelcast() {
        Map<String, String> hazelcastMap = hazelcastInstance.getMap("my-map");
        return hazelcastMap;
    }

}
