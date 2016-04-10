package ru.spbau.mit;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class SecondPartTasks {

    public static final int N_ITERATIONS = 1000;
    public static final double CENTER = 0.5;
    public static final double SQUARED_RADIUS = 0.25;

    private SecondPartTasks() {
    }


    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().flatMap(s -> {
            Stream<String> stream = null;
            try {
                stream = Files.lines(Paths.get(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stream;
        }).filter(s -> s.contains(sequence)).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random random = new Random();
        int hit = IntStream.range(0, N_ITERATIONS).map(value -> {
            double x = random.nextDouble();
            double y = random.nextDouble();
            if (Math.pow(x - CENTER, 2) + Math.pow(y - CENTER, 2) <= SQUARED_RADIUS) {
                return 1;
            } else {
                return 0;
            }
        }).sum();
        return (double) hit / N_ITERATIONS;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream()
                .sorted(Comparator.comparing(stringListEntry -> stringListEntry.getValue().stream()
                        .mapToInt(String::length).sum(), Comparator.reverseOrder()))
                .limit(1).collect(Collectors.toList()).get(0).getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap(stringIntegerMap -> stringIntegerMap.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey)).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, stringListEntry -> stringListEntry.getValue().stream()
                        .mapToInt(Map.Entry::getValue).sum()));
    }
}
