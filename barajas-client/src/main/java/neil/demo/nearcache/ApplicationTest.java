package neil.demo.nearcache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.monitor.NearCacheStats;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplicationTest implements CommandLineRunner {

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Override
	public void run(String... args) throws Exception {
		
		log.info("------------------------");
		log.info("Start");
		log.info("------------------------");
		
		IMap<Integer, PersonSlow> without = this.hazelcastInstance.getMap("without");
		IMap<Integer, PersonFast> with = this.hazelcastInstance.getMap("with");

		this.addData1(without);
		this.addData2(with);
		
		TimeUnit.SECONDS.sleep(2);
		log.info("Data loaded");
		log.info("------------------------");		
		
		this.runTests(with);
		this.runTests(without);

		TimeUnit.SECONDS.sleep(2);
		log.info("Test run");
		log.info("------------------------");		
		
		this.reportStatistics(with);
		this.reportStatistics(without);
		
		log.info("------------------------");
		log.info("End");
		log.info("------------------------");
	}

	private void addData1(IMap<Integer, PersonSlow> iMap) {
		PersonSlow p1 = new PersonSlow();
		p1.setCount(0);
		p1.setFirstName("Mickey");
		p1.setLastName("Mouse");
		
		PersonSlow p2 = new PersonSlow();
		p2.setCount(0);
		p2.setFirstName("Minnie");
		p2.setLastName("Mouse");
		
		PersonSlow p3 = new PersonSlow();
		p3.setCount(0);
		p3.setFirstName("Donald");
		p3.setLastName("Duck");
		
		iMap.set(1, p1);
		iMap.set(2, p2);
		iMap.set(3, p3);
	}
	
	private void addData2(IMap<Integer, PersonFast> iMap) {
		PersonFast p1 = new PersonFast();
		p1.setCount(0);
		p1.setFirstName("Alpha");
		p1.setLastName("A");
		
		PersonFast p2 = new PersonFast();
		p2.setCount(0);
		p2.setFirstName("Beta");
		p2.setLastName("B");
		
		PersonFast p3 = new PersonFast();
		p3.setCount(0);
		p3.setFirstName("Charlie");
		p3.setLastName("C");
		
		iMap.set(1, p1);
		iMap.set(2, p2);
		iMap.set(3, p3);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void runTests(IMap iMap) {
		
		iMap.get(1); // Miss
		
		iMap.get(2); // Miss
		iMap.get(2); // Hit
		iMap.get(2); // Hit
		
		iMap.get(3); // Miss
		iMap.get(3); // Hit
		Object o = iMap.get(3); // Hit
		iMap.set(3, o);
		iMap.get(3); // Miss
		o = iMap.get(3); // Hit
		iMap.set(3, o);
		iMap.get(3); // Miss
		iMap.get(3); // Hit
		
	}

	private void reportStatistics(IMap<?,?> iMap) {
		System.out.println("=======================");
		System.out.println("IMap: " + iMap.getName());

		LocalMapStats localMapStats = iMap.getLocalMapStats();
		if (localMapStats == null) {
			System.out.println("No stats");
		} else {
			NearCacheStats nearCacheStats = localMapStats.getNearCacheStats();
			
			if (nearCacheStats == null) {
				System.out.println("No stats");
			} else {
	            System.out.println("Near Cache size... : " + nearCacheStats.getOwnedEntryCount());
	            System.out.println("Near Cache hits... : " + nearCacheStats.getHits());
	            System.out.println("Near Cache misses. : " + nearCacheStats.getMisses());
			}
		}
		
		System.out.println("-----------------------");
	}
	
}