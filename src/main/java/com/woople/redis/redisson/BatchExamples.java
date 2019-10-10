package com.woople.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import java.util.Map;

public class BatchExamples {
    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.setCodec(new org.redisson.client.codec.StringCodec());
        Class c = Class.forName("org.redisson.client.codec.StringCodec");
        config.setCodec((Codec)c.newInstance());

        config.useClusterServers()
                .setScanInterval(5000)
                .setConnectTimeout(100000)
                .setTimeout(100000)
                .addNodeAddress("redis://10.1.236.179:6379")
                .addNodeAddress("redis://10.1.236.179:6380")
                .addNodeAddress("redis://10.1.236.179:6381");

        RedissonClient redisson = Redisson.create(config);

        RBatch batch = redisson.createBatch(BatchOptions.defaults());

        RMapAsync<String, String> baz = batch.getMap("baz");
        RBucketAsync<String> foo = batch.getBucket("foo");

        RFuture<String> fooFuture = foo.getAsync();
        RFuture<Map<String, String>> barFuture = baz.readAllMapAsync();

        System.out.println("Start to wait foo:" + System.currentTimeMillis());
        fooFuture.thenAccept(result -> System.out.println("Get foo:" + result + " at " + System.currentTimeMillis()));

        System.out.println("Start to wait bar:" + System.currentTimeMillis());
        barFuture.whenComplete((resultMap, throwable) -> System.out.println("Get bar:" + resultMap + " at " + System.currentTimeMillis()));

        batch.execute();

        redisson.shutdown();
    }
}
