
import java.util.ArrayList;
import java.util.List;

public class Show {
    private int showId;
    private Movie movie;
    private Theatre theatre;
    private String showTime;
    private double ticketPrice;
    private List<Seat> seats;

    public Show(int showId, Movie movie, Theatre theatre,
                String showTime, double ticketPrice, int totalSeats) {
        this.showId = showId;
        this.movie = movie;
        this.theatre = theatre;
        this.showTime = showTime;
        this.ticketPrice = ticketPrice;
        this.seats = new ArrayList<>();

        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new Seat(i));
        }
    }

    public int getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Seat findSeat(int seatNumber) {
        for (Seat seat : seats) {
            if (seat.getSeatNumber() == seatNumber) {
                return seat;
            }
        }

        return null;
    }

    public void displayShow() {
        System.out.println("Show ID: " + showId
                + " | Movie: " + movie.getName()
                + " | Theatre: " + theatre.getName()
                + " | Time: " + showTime
                + " | Price: Rs." + ticketPrice);
    }

    public void displaySeats() {
        System.out.println("\nSeats for " + movie.getName());

        for (Seat seat : seats) {
            System.out.print(
                    seat.getSeatNumber() + "["
                    + (seat.isBooked() ? "X" : "O") + "] "
            );
        }

        System.out.println("\nO = Available, X = Booked");
    }
}
