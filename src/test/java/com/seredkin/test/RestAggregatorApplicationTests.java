package com.seredkin.test;

import com.seredkin.test.rest.client.RestClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestAggregatorApplicationTests {

	@Autowired RestClientService restClientServiceAsync;

	@Test
	public void contextLoads() throws InterruptedException, ExecutionException {
		restClientServiceAsync.findUser(1l).get();
	}

}
