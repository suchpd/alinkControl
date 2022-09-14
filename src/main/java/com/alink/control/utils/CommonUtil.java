package com.alink.control.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.util.FieldUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonUtil {


    /**
     * 获取对象中指定属性的值
     * @param object    指定对象
     * @param fieldName     属性名
     * @return  指定属性的值
     */
    public static Object getDeclaredField(Object object, String fieldName) {
        if(fieldName == null || fieldName.isEmpty()){
            return object;
        }

        Object fieldValue = new Object();
        try {
            fieldValue = FieldUtils.getFieldValue(object,fieldName);
        }catch (Exception e){
            e.printStackTrace();
        }

        return fieldValue;
    }

    /**
     * 判断你一个类是否存在某个属性(字段)
     * @param field 字段
     * @param obj 类对象
     * @return true:存在，false:不存在, null:参数不合法
     */
    public static Boolean isExistField(Object obj,String field) {
        if (obj == null || field.isEmpty()) {
            return null;

        }

        Object o = JSON.toJSON(obj);

        JSONObject jsonObj = new JSONObject();

        if (o instanceof JSONObject) {
            jsonObj = (JSONObject) o;

        }
        return jsonObj.containsKey(field);

    }



    /**
     * 计算两点之间距离
     * @param sourceCoordinates 起始坐标
     * @param targetCoordinates 终止坐标
     * @return  距离
     */
    public static double calculateDistance(double[] sourceCoordinates, double[] targetCoordinates){
        if(sourceCoordinates.length != targetCoordinates.length || sourceCoordinates.length == 0){
            throw new RuntimeException("坐标信息不正确！");
        }

        int len = sourceCoordinates.length;

        //三位坐标系中两点距离计算方式为  distance^2 = (x2-x1)^2 + (y2-y1)^2 + (z2-z1)^2
        //坐标差积之和
        double distanceProductSum = 0;

        for (int i=0;i<len;i++){
            distanceProductSum += Math.pow(targetCoordinates[i] - sourceCoordinates[i],2);
        }

        return Math.sqrt(distanceProductSum);
    }

    /**
     * 异步执行
     * @param statement 可运行函数
     */
    public static void asyncExecute(Runnable statement){
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        CompletableFuture.runAsync(()->{
            try{
                statement.run();
            }catch (Exception e){
                e.printStackTrace();
            }
        },executorService);
    }
}
