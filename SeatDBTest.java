
import java.util.List;

public class SeatDBTest {

    public static void main(String[] args) {
        SeatDAO seatDAO = new SeatDAO();

        // Test the first show in the database.
        int showId = 1;

        List<Seat> seats = seatDAO.getSeatsForShow(showId);

        System.out.println("===== SEATS FOR SHOW " + showId + " =====");

        if (seats.isEmpty()) {
            System.out.println("No seats found.");
        } else {
            for (Seat seat : seats) {
                seat.displaySeat();
            }
        }
    }
}
