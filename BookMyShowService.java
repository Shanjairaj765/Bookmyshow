
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookMyShowService {

    private final MovieDAO movieDAO = new MovieDAO();
    private final ShowDAO showDAO = new ShowDAO();
    private final SeatDAO seatDAO = new SeatDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    public void displayMovies() {
        System.out.println("\n===== MOVIES =====");

        List<Movie> movies = movieDAO.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }

        for (Movie movie : movies) {
            movie.displayMovie();
        }
    }

    public void displayShows() {
        System.out.println("\n===== AVAILABLE SHOWS =====");

        List<Show> shows = showDAO.getAllShows();

        if (shows.isEmpty()) {
            System.out.println("No shows found.");
            return;
        }

        for (Show show : shows) {
            show.displayShow();
        }
    }

    public void displaySeats(int showId) {
        List<Seat> seats = seatDAO.getSeatsForShow(showId);

        if (seats.isEmpty()) {
            System.out.println("No seats found for this show.");
            return;
        }

        System.out.println("\n===== SEAT STATUS =====");

        for (Seat seat : seats) {
            seat.displaySeat();
        }
    }

    public void displayBookings(String customerName) {
        if (customerName == null || customerName.isBlank()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        bookingDAO.displayBookingsByCustomer(customerName.trim());
    }

    public void cancelBooking(int bookingId) {
        bookingDAO.cancelBooking(bookingId);
    }

    public void addMovie(String name, String genre,
                         int duration, double rating) {
        System.out.println(
                "Database movie insertion is not connected yet."
        );
        System.out.println(
                "We will connect the admin feature next."
        );
    }

    public void bookTickets(String customerName, int showId,
                            List<Integer> seatNumbers) {
        if (customerName == null || customerName.isBlank()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        if (seatNumbers == null || seatNumbers.isEmpty()) {
            System.out.println("Select at least one seat.");
            return;
        }

        Set<Integer> uniqueSeats = new HashSet<>(seatNumbers);

        if (uniqueSeats.size() != seatNumbers.size()) {
            System.out.println("Duplicate seat numbers are not allowed.");
            return;
        }

        int bookingId = bookingDAO.bookSeats(
                customerName, showId, seatNumbers
        );

        if (bookingId > 0) {
            System.out.println("Booking completed. Booking ID: " + bookingId);
        }
    }
}
