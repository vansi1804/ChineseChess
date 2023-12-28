package com;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// @SpringBootApplication
// @EnableWebMvc
// @EnableScheduling
// public class ChineseChessAPI {

//   public static void main(String[] args) {
//     SpringApplication.run(ChineseChessAPI.class, args);
//   }
// }

import java.util.Random;

public class ChineseChessAPI {

  public static void main(String[] args) {
    Thread thread1 = new Thread(new RandomNumberGenerator());
    Thread thread2 = new Thread(new SquareCalculator());

    thread1.start();
    thread2.start();
  }

  static class RandomNumberGenerator implements Runnable {

    private static final int MAX_NUMBERS = 10;
    private Random random = new Random();
    private int generatedNumbers = 0;

    @Override
    public void run() {
      while (generatedNumbers < MAX_NUMBERS) {
        int randomNumber = random.nextInt(50) + 1;
        System.out.println("Luồng 1: Số ngẫu nhiên: " + randomNumber);

        synchronized (SquareCalculator.lock) {
          Thread.sleep(1000);
          SquareCalculator.number = randomNumber;
          SquareCalculator.lock.notify();
          try {
            SquareCalculator.lock.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        generatedNumbers++;
      }
    }
  }

  static class SquareCalculator implements Runnable {

    public static final Object lock = new Object();
    public static int number;
    private static boolean shouldStop = false;

    @Override
    public void run() {
      while (true) {
        synchronized (lock) {
          try {
            lock.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          
          Thread.sleep(1000);

          if (number != 0) {
            int square = number * number;
            System.out.println("Luồng 2: Bình phương của số: " + square);
          }

          lock.notify();

          if (
            RandomNumberGenerator.generatedNumbers ==            RandomNumberGenerator.MAX_NUMBERS
          ) {
            shouldStop = true;
          }

          if (shouldStop) {
            break;
          }
        }
      }
    }
  }
}
