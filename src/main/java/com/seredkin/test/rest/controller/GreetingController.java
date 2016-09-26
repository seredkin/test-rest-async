package com.seredkin.test.rest.controller;

import com.seredkin.test.rest.client.RestClientService;
import com.seredkin.test.rest.data.Post;
import com.seredkin.test.rest.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;

/** Receives requests with UserId and sends posts to MQ*/
@Controller
public class GreetingController {

    private final String destination = "/topic/greetings";
    @Autowired SimpMessagingTemplate messagingTemplate;
    @Autowired RestClientService restClientService;

    @MessageMapping("/hello")
    //@SendTo("/topic/greetings")
    public void greeting(Message message) throws Exception {
        Map map = restClientService.findUserAndPosts(message.getUserId()).get();

        Post startPost = new Post();
        User user = (User) map.get("user");
        startPost.setBody("Posts by user " + user.getName());
        this.messagingTemplate.convertAndSend(destination, startPost);

        if (!map.containsKey("error")) {
            sendPostsToClient(map);
        }
    }

    private void sendPostsToClient(Map map) throws InterruptedException {
        Post[] posts = (Post[]) map.get("posts");
        Arrays.stream(posts).forEach(post -> messagingTemplate.convertAndSend(destination, post));
    }

}
