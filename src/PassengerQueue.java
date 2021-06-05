import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PassengerQueue {

    List<Passenger> passengerQue1ColomboToBadulla = new ArrayList<>();
    List<Passenger> passengerQue1BadullaToColombo = new ArrayList<>();
    List<Passenger> passengerQue2ColomboToBadulla = new ArrayList<>();
    List<Passenger> passengerQue2BadullaToColombo = new ArrayList<>();

    private int first;
    private int last;
    private int maxStayInQue;
    private int maxLength;

    public boolean isEmpty(List<Passenger> passengerQue) {
        return passengerQue.isEmpty();
    }

    public boolean isFull(int value) {
        return Objects.equals(value, 42);
    }

    public void add(List<Passenger> passengerQue, Passenger passenger) {
        passengerQue.add(passenger);
    }

    public void remove(List<Passenger> passengerQue, int index) {
        passengerQue.remove(index);
    }

    public void setMaxStayInQue(int maxStayInQue) {
        this.maxStayInQue = maxStayInQue;
    }

    public int getMaxStay() {
        return maxStayInQue;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void display() {

    }
}


