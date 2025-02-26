package entregas.lianoJavier.RetoCarrefur;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import entregas.lianoJavier.RetoCarrefur.Persona;
import entregas.lianoJavier.RetoCarrefur.Caja;

public class Carrefur {
    private static final int OPENING_HOUR = 9;
    private static final int CLOSING_HOUR = 21;
    private static final int MIN_PACKS = 5;
    private static final int MAX_PACKS = 15;
    private static final double ARRIVAL_PROBABILITY = 0.6;
    private static final int NUM_CASHIERS = 4;
    private static int minutesWithoutQueue = 0;
    private static int totalCustomersServed = 0;
    private static int totalItemsSold = 0;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        Queue<Persona> queue = new LinkedList<>();
        Caja[] cajas = initializeCajas(NUM_CASHIERS);
        Random random = new Random();
        int totalMinutes = calculateTotalMinutes(OPENING_HOUR, CLOSING_HOUR);

        for (int minute = 0; minute < totalMinutes; minute++) {
            if (isCustomerArriving(random, ARRIVAL_PROBABILITY)) {
                queue.add(createNewCustomer(random, MIN_PACKS, MAX_PACKS));
                System.out.println("New customer arrived with " + queue.peek().getPacks() + " packs.");
            }
            processCajas(cajas, queue);
            printStatus(minute, queue, cajas);
        }
        printSummary(queue);
    }

    private static Caja[] initializeCajas(int numCashiers) {
        Caja[] cajas = new Caja[numCashiers];
        for (int i = 0; i < numCashiers; i++) {
            cajas[i] = new Caja();
        }
        return cajas;
    }

    private static int calculateTotalMinutes(int openingHour, int closingHour) {
        return (closingHour - openingHour) * 60;
    }

    private static boolean isCustomerArriving(Random random, double arrivalProbability) {
        return random.nextDouble() < arrivalProbability;
    }

    private static Persona createNewCustomer(Random random, int minPacks, int maxPacks) {
        int packs = minPacks + random.nextInt(maxPacks - minPacks + 1);
        return new Persona(packs);
    }

    private static void processCajas(Caja[] cajas, Queue<Persona> queue) {
        for (Caja caja : cajas) {
            if (caja.isFree() && !queue.isEmpty()) {
                Persona customer = queue.poll();
                caja.attend(customer);
                totalCustomersServed++;
                totalItemsSold += customer.getPacks();
            }
            caja.processMinute();
        }
        if (queue.isEmpty()) {
            minutesWithoutQueue++;
        }
    }

    private static void printStatus(int minute, Queue<Persona> queue, Caja[] cajas) {
        clearScreen();
        System.out.println(ANSI_BLUE + "========================================" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "Minute: " + minute + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "Queue: " + generateQueueAscii(queue) + ANSI_RESET);
        for (int i = 0; i < cajas.length; i++) {
            System.out.print(ANSI_PURPLE + "[" + (i + 1) + "]: " + ANSI_RESET);
            int remainingItems = cajas[i].getRemainingItems();
            for (int j = 0; j < remainingItems; j++) {
                System.out.print(ANSI_YELLOW + "[X] " + ANSI_RESET);
            }
            System.out.println();
        }
        System.out.println(ANSI_BLUE + "========================================" + ANSI_RESET);
        try {
            TimeUnit.MILLISECONDS.sleep(0); // Add a delay for better visualization
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String generateQueueAscii(Queue<Persona> queue) {
        StringBuilder ascii = new StringBuilder();
        int count = 0;
        for (Persona persona : queue) {
            if (count < 5) {
                ascii.append("_O_ ");
            }
            count++;
        }
        if (count > 5) {
            ascii.append("+ ").append(count - 5);
        }
        return ascii.toString();
    }

    private static void printSummary(Queue<Persona> queue) {
        System.out.println("Summary of the day:");
        System.out.println("Minutes without queue: " + minutesWithoutQueue);
        System.out.println("People left in queue: " + queue.size());
        System.out.println("Total customers served: " + totalCustomersServed);
        System.out.println("Total items sold: " + totalItemsSold);
    }
}