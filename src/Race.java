import sun.misc.Signal;

import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;

public class Race {
    private ArrayList<Stage> stages;
    private ArrayList<Car> player;
    private HashMap<SignalType, Signal> signals;
    private AtomicReference<Car> winner;


    public Race(Stage[] stages, int playerSum) {
        Car[] cars = new Car[playerSum];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(20 + (int) (Math.random() * 10));

        }
        this.stages = new ArrayList<>(Arrays.asList(stages));
        this.player = new ArrayList<>(Arrays.asList(cars));
        this.signals = new HashMap<>();
        this.winner = new AtomicReference<>();

        signals.put(SignalType.START, new Signal("ÂÀÆÍÎÅ ÎÁÚßÂËÅÍÈÅ >>> Ãîíêà íà÷àëàñü!!!",
                playerSum));
        signals.put(SignalType.FINISH, new Signal("ÂÀÆÍÎÅ ÎÁÚßÂËÅÍÈÅ >>> Ãîíêà çàêîí÷èëàñü!!!",
                playerSum));
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public CyclicBarrier getStartSignal() {
        return signals.get(SignalType.START).getBarrier();
    }

    public CyclicBarrier getFinishSignal() {
        return signals.get(SignalType.FINISH).getBarrier();
    }

    public boolean setWinner(Car winner) {
        boolean result = this.winner.compareAndSet(null, winner);
        if (result) {
            System.out.println("ÂÀÆÍÎÅ ÎÁÚßÂËÅÍÈÅ >>> " + winner.getName() + " ïîáåäèë!!!");
        }
        return result;
    }

    public void start() {
        for (Car participant : player) {
            participant.addToRace(this);
        }

        System.out.println("ÂÀÆÍÎÅ ÎÁÚßÂËÅÍÈÅ >>> Ïîäãîòîâêà!!!");
        for (Car participant : player) {
            new Thread(participant).start();
        }
    }

    private class Signal {
        private CyclicBarrier barrier;

        private Signal(String message, int size) {
            this.barrier = new CyclicBarrier(size, () -> System.out.println(message));
        }

        public CyclicBarrier getBarrier() {
            return barrier;
        }
    }

    private enum SignalType {
        START, FINISH
    }

}