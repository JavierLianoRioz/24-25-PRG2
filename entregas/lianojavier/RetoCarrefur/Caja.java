package entregas.lianoJavier.RetoCarrefur;

public class Caja {
    private Persona currentCustomer;
    private int remainingTime;

    public void attend(Persona customer) {
        this.currentCustomer = customer;
        this.remainingTime = customer.getPacks();
    }

    public void processMinute() {
        if (currentCustomer != null) {
            remainingTime--;
            if (remainingTime <= 0) {
                currentCustomer = null;
            }
        }
    }

    public boolean isFree() {
        return currentCustomer == null;
    }

    public int getRemainingItems() {
        return remainingTime;
    }
}
