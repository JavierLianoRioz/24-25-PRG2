import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import entregas.lianoJavier.RetoCarrefur.Persona;
import entregas.lianoJavier.RetoCarrefur.Caja;

public class Carrefur {
    private static final int OPENING_HOUR = 9;
    private static final int CLOSING_HOUR = 21;
    private static final int MIN_PACKS = 5;
    private static final int MAX_PACKS = 15;
    private static final double ARRIVAL_PROBABILITY = 0.6;
    private static final int NUM_CASHIERS = 4;

    public static void main(String[] args) {
        Queue<Persona> queue = new LinkedList<>();
        Caja[] cajas = initializeCajas(NUM_CASHIERS);
        Random random = new Random();
        int totalMinutes = calculateTotalMinutes(OPENING_HOUR, CLOSING_HOUR);

        for (int minute = 0; minute < totalMinutes; minute++) {
            if (isCustomerArriving(random, ARRIVAL_PROBABILITY)) {
                queue.add(createNewCustomer(random, MIN_PACKS, MAX_PACKS));
            }
            processCajas(cajas, queue);
        }
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
                caja.attend(queue.poll());
            }
            caja.processMinute();
        }
    }
}