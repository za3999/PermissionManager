package com.cf.common;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ServiceManager {

    public static <T> T getServices(Class<T> clazz) {
        Iterator<T> iterator = ServiceLoader.load(clazz).iterator();
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
}
