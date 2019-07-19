package com.demo;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.model.Employee;

@Component
public class BackupDemo {

	@Autowired
	HazelcastInstance instance;

	IMap<Integer, Employee> map;

	@Autowired
	BackupDemo(HazelcastInstance hazelcastInstance) {
		this.instance = hazelcastInstance;
	}

	public void demo1() {
		IMap<Integer, String> mapCustomers = instance.getMap("customers");
		mapCustomers.put(1, "Joe");
		
		mapCustomers.lock(1);
		mapCustomers.put(1, "Joe1111");
		mapCustomers.put(2, "Ali");
		mapCustomers.put(3, "Avi");
		mapCustomers.put(4, "Avi");

		System.out.println("Customer with key 1: " + mapCustomers.get(1));
		System.out.println("Map Size:" + mapCustomers.size());

		Queue<String> queueCustomers = instance.getQueue("customers");
		queueCustomers.offer("Tom");
		queueCustomers.offer("Mary");
		queueCustomers.offer("Jane");
		System.out.println("First customer: " + queueCustomers.poll());
		System.out.println("Second customer: " + queueCustomers.peek());
		System.out.println("Queue size: " + queueCustomers.size());
	}

	public void demo2() {
		ClientConfig clientConfig = new ClientConfig();
		// clientConfig.addAddress("127.0.0.1:5701");
		HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		IMap map = client.getMap("customers");
		System.out.println("Map Size:" + map.size());
	}

	@SuppressWarnings("deprecation")
	public void lockdemo() {
		// Get a distributed lock called "my-distributed-lock"
		Lock lock = instance.getLock("my-map");
		// Now create a lock and execute some guarded code.
		lock.lock();
		try {
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// do something here
		} finally {
			lock.unlock();
		}
		// Shutdown this Hazelcast Cluster Member
		// instance.shutdown();
	}

}
