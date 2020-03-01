package works.hop.wait.notify;

public class WaitNotifyDialog {

    public static void main(String[] args) {
        CleanHair clean = new CleanHair();
        new Hair("Lather", clean);
        new Hair("Rinse", clean);
    }

    static class CleanHair {

        String state;

        synchronized void lather() {
            state = "Lather";
            System.out.println(state);
            notify();
            try {
                while(state.equals("Lather")) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }

        synchronized void rinse() {
            state = "Rinse";
            System.out.println(state);
            notify();
            try {
                while(state.equals("Rinse")) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    static class Hair extends Thread {

        CleanHair hair;

        Hair(String name, CleanHair target) {
            setName(name);
            this.hair = target;
            start();
        }

        @Override
        public void run() {
            try {
                while (getName().equals("Rinse")) {
                    Thread.sleep(1000l);
                    hair.rinse();
                }
                while (getName().equals("Lather")) {
                    Thread.sleep(1000l);
                    hair.lather();
                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
