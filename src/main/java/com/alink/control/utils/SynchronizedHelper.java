package com.alink.control.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SynchronizedHelper {

    Map<String,Object> mutexCache = new ConcurrentHashMap<>();

    public void exec(String key,Runnable statement){
        Object mutex4Key = mutexCache.computeIfAbsent(key,k->new Object());

        synchronized (mutex4Key){
            try{
                statement.run();
            }finally {
                mutexCache.remove(key);
            }
        }
    }
}
