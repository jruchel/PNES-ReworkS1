package org.ekipa.pnes.utils;

import java.util.List;
import java.util.Random;

public class MyRandom {

    public static int getRandom(int a, int b) {
        return (Math.abs(new Random().nextInt())) % (b - a + 1) + a;
    }

    public static <E> E getRandom(List<E> list) {
        if (list.isEmpty()) return null;
        return list.get(getRandom(0, list.size() - 1));
    }
}
