package com.seredkin.test.rest.controller;

/**
 * Created by Anton on 22.09.2016.
 */

import com.seredkin.test.rest.data.User;
import com.seredkin.test.rest.client.RestClientService;
import com.seredkin.test.rest.data.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Controller
public class GreetingController {


    private final String destination = "/topic/greetings";
    @Autowired  SimpMessagingTemplate messagingTemplate;
    @Autowired RestClientService restClientService;

    @MessageMapping("/hello")
    //@SendTo("/topic/greetings")
    public void greeting(HelloMessage message) throws Exception {
        Map map = restClientService.findUser(Long.parseLong(message.getName())).get();

        Post startPost = new Post();
        User user = (User)map.get("user");
        startPost.setBody("Posts by user "+ user.getName());
        this.messagingTemplate.convertAndSend(destination, startPost);

        if(!map.containsKey("error")) {
            sendPostsToClient(map);
        }
    }

    private void sendPostsToClient(Map map) throws InterruptedException {
        Post[] posts = (Post[]) map.get("posts");
        for (Post post : Arrays.asList(posts)) {
            Thread.sleep(1000);
            post.setAdditionalProperty("time", new Date().toString());
            this.messagingTemplate.convertAndSend(destination, post);
        }
    }

}
