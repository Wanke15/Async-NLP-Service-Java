package com.java.ml.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisUtil {
    public final static JedisPool jedisPool = new JedisPool("localhost",6379);

    public void lpush(String key, List<String> values)
    {
        Jedis jedis =null;
        try {
            //1.从连接池获取jedis对象
            jedis=jedisPool.getResource();
            //2.执行操作
//            for (String v: values
//                 ) {
//                jedis.lpush(key, v);
//            }
            if (!jedis.keys(key).isEmpty())
            {
                jedis.del(key);
            }
            for (int i = values.size() - 1; i >= 0; i--)
            {
                jedis.lpush(key, values.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(jedis!=null){
                //如果使用JedisPool,close操作不是关闭连接，代表归还连接池
                jedis.close();
            }
        }
    }

    public List<String> lrange (String key, int start, int end)
    {
        Jedis jedis =null;
        try {
            //1.从连接池获取jedis对象
            jedis=jedisPool.getResource();
            //2.执行操作
            List<String> results = jedis.lrange(key, start, end);
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }finally{
            if(jedis!=null){
                //如果使用JedisPool,close操作不是关闭连接，代表归还连接池
                jedis.close();
            }
        }
    }
}
