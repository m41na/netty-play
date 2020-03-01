package works.hop.wait.notify.threads;

import java.util.Random;
import java.util.Scanner;

public class FlowSrc implements Runnable, FlowHandler<FlowHandler.Signal<String>> {

    private final FlowSignal<FlowHandler.Signal, FlowHandler.Signal> signals;

    public FlowSrc(FlowSignal signals) {
        this.signals = signals;
    }

    public void produce(String question) {
        System.out.println("producing question -> '" + question + "'");
        signals.dispatch(this, "questions", newSignal("questions", question));
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println("Thread '" + name + "' has started");

        //inputPrompt();
        simulateInput();
        System.out.println(Thread.currentThread().getName() + " has completed");
    }

    @Override
    public void onSignal(Signal<String> signal) {
        //receive response
        System.out.println("consuming message -> " + signal.getBody());
    }

    private void inputPrompt(){
        String line;
        Scanner scanner = new Scanner(System.in);
        while ((line = scanner.nextLine()) != null) {
            //System.out.println("waiting for return key");
            produce(line);
           // System.out.println("return key pressed");
        }
    }

    private void simulateInput(){
        int count = 0;
        Random rand = new Random();
        String name = Thread.currentThread().getName();
        while(count++ < 5){
            produce(name + ": " + rand.nextInt(100));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
