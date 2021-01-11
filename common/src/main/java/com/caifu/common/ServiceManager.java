package com.caifu.common;

import java.util.ServiceLoader;

public class ServiceManager {

    public static <T> T getServices(Class<T> clazz) {
        try {
            return ServiceLoader.load(clazz).iterator().next();
        } catch (Exception e) {
            return null;
        }
    }
}
