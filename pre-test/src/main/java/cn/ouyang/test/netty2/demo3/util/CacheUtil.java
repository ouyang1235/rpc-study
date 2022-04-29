package cn.ouyang.test.netty2.demo3.util;

import cn.ouyang.test.netty2.demo3.doamin.FileBurstInstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheUtil {

    public static Map<String, FileBurstInstruct> burstDataMap = new ConcurrentHashMap<>();

}
