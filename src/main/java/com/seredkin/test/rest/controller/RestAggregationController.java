package com.seredkin.test.rest.controller;

import com.seredkin.test.rest.client.RestClientService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton on 21.09.2016.
 */
@RestController @Log4j
public class RestAggregationController {

    @Autowired RestClientService restClientService;

    @RequestMapping("/users-and-posts/{id}")
    public ResponseEntity<Map> loadRemoteUsersAndPosts(@PathVariable Long id){
        try {
            return new ResponseEntity<>(restClientService.findUserAndPosts(id).get(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e);
            Map m = new HashMap();
            m.put("error", e.getMessage());
            return new ResponseEntity<>(m, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
