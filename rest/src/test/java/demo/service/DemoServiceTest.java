package demo.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Before;
import org.junit.Test;

import demo.services.DemoService;

public class DemoServiceTest {

	private DemoService service;

	@Before
	public void setUp() throws Exception {
		service = new DemoService();
	}

	@Test
	public void testFetchAndIncreaseCounter() throws Exception {
		final int initialCounter = service.fetchCounter();
		final int increaseCounter = service.fetchAndIncreaseCounter();
		assertThat(initialCounter, lessThan(increaseCounter));
		assertThat(increaseCounter, is(1));
	}

	@Test
	public void testReset() throws Exception {
		service.fetchAndIncreaseCounter();
		assertThat(service.fetchCounter(), is(1));
		service.reset();
		assertThat(service.fetchCounter(), is(0));
	}
}
