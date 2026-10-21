
public class Seat {

    private int seatNumber;
    private boolean booked;

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.booked = false;
    }

    public Seat(int seatNumber, boolean booked) {
        this.seatNumber = seatNumber;
        this.booked = booked;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public boolean book() {
        if (booked) {
            return false;
        }

        booked = true;
        return true;
    }

    public void release() {
        booked = false;
    }

    public void displaySeat() {
        System.out.println(
            "Seat " + seatNumber + ": "
            + (booked ? "BOOKED" : "AVAILABLE")
        );
    }
}
