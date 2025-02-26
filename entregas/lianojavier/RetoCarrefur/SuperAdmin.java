package entregas.lianoJavier.RetoCarrefur;

public class SuperAdmin {
    private Caja[] cajas;
    private static final int MAX_CASHIERS = 6;

    public SuperAdmin(Caja[] cajas) {
        this.cajas = cajas;
    }

    public void openCaja() {
        for (int i = 0; i < MAX_CASHIERS; i++) {
            if (i >= cajas.length) {
                Caja[] newCajas = new Caja[cajas.length + 1];
                System.arraycopy(cajas, 0, newCajas, 0, cajas.length);
                newCajas[cajas.length] = new Caja();
                cajas = newCajas;
                System.out.println("Opened a new caja. Total cajas: " + cajas.length);
                break;
            }
        }
    }

    public void closeCaja() {
        if (cajas.length > 1) {
            Caja[] newCajas = new Caja[cajas.length - 1];
            System.arraycopy(cajas, 0, newCajas, 0, cajas.length - 1);
            cajas = newCajas;
            System.out.println("Closed a caja. Total cajas: " + cajas.length);
        }
    }

    public Caja[] getCajas() {
        return cajas;
    }
}
