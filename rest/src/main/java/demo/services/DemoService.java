package demo.services;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DemoService {

	private static final Logger log = LoggerFactory.getLogger(DemoService.class);

	private int counter;

	public int fetchAndIncreaseCounter() {
		log.info("Counter is: {}", counter);
		return ++counter;
	}

	public int fetchCounter() {
		return counter;
	}

	public void reset() {
		counter = 0;
	}
}
