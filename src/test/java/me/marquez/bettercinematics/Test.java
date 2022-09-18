package me.marquez.bettercinematics;

import lombok.AllArgsConstructor;
import lombok.ToString;
import me.marquez.bettercinematics.entity.Cinematic;
import me.marquez.bettercinematics.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Test {

    @AllArgsConstructor
    @ToString
    static class A {
        private String name;
        private int age;
        private LinkedList<String> array;
        private boolean test;
        private List<B> b;
    }

    @AllArgsConstructor
    @ToString
    static class B {
        private String name;
        private double a;
        private C c;
    }

    static enum C {
        HELLO,
        WORLD;
    }

    public static void main(String[] args) {
//        Cinematic cinematic = new Cinematic("TEST");
//        FileUtils.saveToJsonAsync(new File("TEST.json"), Cinematic.class, cinematic).whenCompleteAsync((unused, throwable) -> {
//            if(throwable == null) {
//                System.out.println("Complete");
//            }else {
//                throwable.printStackTrace();
//            }
//        });
        A a = new A("TEST", 24, new LinkedList<>(Arrays.asList("1234", "ASDF")), true, Arrays.asList(new B("BBBB", 0.5D, C.HELLO), new B("QWER", 1.24D, C.WORLD)));
        System.out.println(a);
        FileUtils.saveToJsonAsync(new File("TEST.json"), A.class, a).whenCompleteAsync((unused, throwable) -> {
            if(throwable == null) {
                System.out.println("Complete");
            }else {
                throwable.printStackTrace();
            }
        });
    }

}
