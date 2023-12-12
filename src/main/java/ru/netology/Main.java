package ru.netology;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq =  new HashMap<>();
    public static Thread threadGenerateRoute(String letters, int length, int routeNumber) {
        return new Thread (
                ()-> {
                    Random random = new Random();
                    StringBuilder route = new StringBuilder();
                    for (int i = 0; i < length; i++) {
                        route.append(letters.charAt(random.nextInt(letters.length())));
                    }
                    //return route.toString();
                    String routeText = route.toString();
                    int count = 0;
                    for (int i = 0; i < routeText.length(); i++) {
                        if (routeText.charAt(i) == 'R') {
                            count++;
                        }
                    }
                    synchronized (sizeToFreq) {
                        if (sizeToFreq.containsKey(count)) {
                            int oldFreq = sizeToFreq.get(count);
                            sizeToFreq.put(count, oldFreq + 1);
                        } else {
                            sizeToFreq.put(count, 1);
                        }
                    }
                    System.out.println("В маршруте " + routeText + " (№ " + routeNumber + ") количество поворотов направо " + count);
                }
        );
    }
    public static void main(String[] args) throws InterruptedException {
        //generateRoute("RLRFR", 100);
        final int NUMBER_OF_ROUTES = 1000;
        String[] texts = new String[NUMBER_OF_ROUTES];
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ROUTES; i++) {
            threadList.add(threadGenerateRoute("RLRFR", 100, i+1));
        }

        for (Thread thread: threadList) {
            thread.start();
        }

        for (Thread thread: threadList)  {
            thread.join();
        }
        List<Integer> freq = new ArrayList<>();
        for (Integer key : sizeToFreq.keySet()) {
            Integer value = sizeToFreq.get(key);
            freq.add(value);
        }
        Collections.reverse(freq);
        Integer maxFreq = freq.get(0);
        for (Integer key : sizeToFreq.keySet()) {
           if (maxFreq.equals(sizeToFreq.get(key))) {
               System.out.println("Самое частое количество повторений " + key +"(встретилось " + maxFreq + " раз)");
           }
        }
        System.out.println("Другие размеры: ");
        for (Integer key : sizeToFreq.keySet()) {
            if (!maxFreq.equals(sizeToFreq.get(key))) {
                System.out.println("- " + key + "(" + sizeToFreq.get(key) + " раз)");
            }
        }



    }
}