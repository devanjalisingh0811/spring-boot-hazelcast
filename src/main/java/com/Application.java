package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.service.MusicService;
import com.demo.BackupDemo;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Application implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(Application.class);
    
    @Autowired
    private MusicService musicService;
    
    @Autowired
    private BackupDemo demo;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Spring Boot Hazelcast Caching Example Configuration");

        musicService.clearCache();

        play("trombone");
        play("guitar");
        play("trombone");
        play("bass");
        play("trombone");
        
		HazelcastInstance client = HazelcastClient.newHazelcastClient();
		IMap map = client.getMap("instrument");
		System.out.println("*******************Map Size:" + map.size());
		System.out.println("*******************Map:" + map);
        demo.demo1();
        demo.demo1();
        demo.demo2();
        demo.lockdemo();
    }

    private void play(String instrument){
        log.info("Calling: " + MusicService.class.getSimpleName() + ".play(\"" + instrument + "\");");
        musicService.play(instrument);
    }
}
