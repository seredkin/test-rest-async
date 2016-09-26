package com.seredkin.test;

import com.seredkin.test.rest.client.RestClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static junit.framework.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestAggregatorApplicationTests {

    @Autowired RestClientService restClientService;

    @Test
    public void contextLoads() throws InterruptedException, ExecutionException {
        Map<String, Object> map = restClientService.findUserAndPosts(1l).get();
        assertNotNull(map);
        assertNotNull(map.get("user"));
        assertNotNull(map.get("posts"));
    }

    @Test
    public void inexistentUser(){
        try {
            restClientService.findUserAndPosts(Long.MAX_VALUE).get();
        } catch (Exception e) {
            if(!e.getMessage().contains("404 Not Found"))
                throw new RuntimeException("Remote endpoint returns wrong error code: " + e.getLocalizedMessage());
        }
    }
}
