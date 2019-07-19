package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.NetworkConfig;

@Configuration
public class HazelcastConfiguration {
    @Bean
    public Config hazelCastConfig(){
        Config config = new Config();
        config.setInstanceName("hazelcast-instance")
                .addMapConfig(
                        new MapConfig()
                                .setName("instrument")
                                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                                .setEvictionPolicy(EvictionPolicy.LRU)
                                .setTimeToLiveSeconds(-1));
        
        config.getManagementCenterConfig().setEnabled(true);
        config.getManagementCenterConfig().setUrl("http://192.168.20.249:8080/hazelcast-mancenter");
        

		/*SerializerConfig sc = new SerializerConfig().setTypeClass(Employee.class).setClass(EmployeeSerializer.class);
        config.getSerializationConfig().addSerializerConfig(sc);*/
        
        NetworkConfig network = config.getNetworkConfig();
        network.setPort(5701).setPortCount(20);
        network.setPortAutoIncrement(true);
        
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig()
        .addMember("192.168.1.102")
        .addMember("192.168.20.249")
        .addMember("192.168.20.61").setEnabled(true);
        return config;
    }
}