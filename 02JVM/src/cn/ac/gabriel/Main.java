package cn.ac.gabriel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        TestClassLoader testClassLoader = new TestClassLoader();
        try {
            Class<?> testClass = testClassLoader.findClass("cn.ac.gabriel.Test");
            Object test = testClass.newInstance();
            Method say = testClass.getMethod("say");
            say.invoke(test, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
