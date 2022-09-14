package com.alink.control;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class AlinkControlApplicationTests {

    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;

    @Test
    void contextLoads() {

    }

    @Test
    public void testString() {
        strRedisTemplate.opsForValue().set("such", "hello");
        System.out.println(strRedisTemplate.opsForValue().get("such"));
    }

}
