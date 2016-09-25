package com.seredkin.test.rest.client;

import com.seredkin.test.rest.data.User;
import com.seredkin.test.rest.data.Post;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Anton on 21.09.2016.
 */
@Service @Log4j
public class RestClientService {

    private final String userUrl = "http://jsonplaceholder.typicode.com/users/%s";
    private final String postUrl = "http://jsonplaceholder.typicode.com/posts?userId=%s";

    RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());

    @Async
    public CompletableFuture<Map> findUser(@NotNull Long user) throws InterruptedException {

        final Map map = new LinkedHashMap<>();

        return CompletableFuture.supplyAsync(() -> getUserData(user))
                .thenCombineAsync(
                        CompletableFuture.supplyAsync(() -> getPostData(user)),
                        (user1, posts) -> {
                            map.put("user", user1);
                            map.put("posts", posts);
                            return map;
                        }).exceptionally(throwable -> {
                    log.error(throwable);
                    map.put("error", throwable.getMessage());
                    return map;
                });

    }

    private User getUserData(Long user) {
        return restTemplate.exchange(String.format(userUrl, user), HttpMethod.GET, buildHttpEntity(), User.class).getBody();
    }

    private Post[] getPostData(Long user) {
        return restTemplate.exchange(String.format(postUrl, user), HttpMethod.GET, buildHttpEntity(), Post[].class).getBody();
    }

    private HttpEntity<String> buildHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        Remote endpoint doesn't accept requests without this header
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

        return new HttpEntity<>("parameters", headers);
    }


}
