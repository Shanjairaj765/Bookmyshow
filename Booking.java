
import java.util.ArrayList;
import java.util.List;

public class Booking {

    private int bookingId;
    private String customerName;
    private Show show;
    private List<Seat> bookedSeats;
    private double totalAmount;
    private boolean active;

    public Booking(int bookingId, String customerName,
                   Show show, List<Seat> bookedSeats) {

        this.bookingId = bookingId;
        this.customerName = customerName;
        this.show = show;
        this.bookedSeats = new ArrayList<>(bookedSeats);

        this.totalAmount =
                show.getTicketPrice() * bookedSeats.size();

        this.active = true;
    }

    // Getters

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getBookedSeats() {
        return new ArrayList<>(bookedSeats);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isActive() {
        return active;
    }

    // Cancel booking in the Java object
    public void cancel() {
        if (!active) {
            return;
        }

        for (Seat seat : bookedSeats) {
            seat.release();
        }

        active = false;
    }

    // Display booking details
    public void displayBooking() {

        System.out.println("\n================================");
        System.out.println("       BOOKING CONFIRMATION");
        System.out.println("================================");

        System.out.println("Booking ID : " + bookingId);
        System.out.println("Customer   : " + customerName);
        System.out.println("Movie      : " + show.getMovie().getName());
        System.out.println("Theatre    : " + show.getTheatre().getName());

        System.out.print("Seats      : ");

        for (Seat seat : bookedSeats) {
            System.out.print(seat.getSeatNumber() + " ");
        }

        System.out.println();
        System.out.println("Ticket Price: Rs."
                + show.getTicketPrice());

        System.out.println("Total Amount: Rs."
                + String.format("%.2f", totalAmount));

        System.out.println("Status      : "
                + (active ? "CONFIRMED" : "CANCELLED"));

        System.out.println("================================");
    }
}
