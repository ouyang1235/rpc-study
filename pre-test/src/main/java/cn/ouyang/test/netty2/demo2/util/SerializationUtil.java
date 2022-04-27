package cn.ouyang.test.netty2.demo2.util;


import com.dyuproject.protostuff.Schema;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializationUtil {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd();

    private SerializationUtil(){

    }

    public static <T> byte[] serialize(T obj){

    }


}
