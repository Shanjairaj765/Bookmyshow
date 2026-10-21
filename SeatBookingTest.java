
import java.util.List;
import java.util.Scanner;

public class SeatBookingTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        SeatDAO seatDAO = new SeatDAO();

        System.out.print("Enter Show ID: ");
        int showId = scanner.nextInt();

        List<Seat> seats = seatDAO.getSeatsForShow(showId);

        System.out.println("\n===== AVAILABLE SEATS =====");

        if (seats.isEmpty()) {
            System.out.println("No seats found for this show.");
            scanner.close();
            return;
        }

        for (Seat seat : seats) {
            seat.displaySeat();
        }

        System.out.print("\nEnter seat number to book: ");
        int seatNumber = scanner.nextInt();

        boolean success = seatDAO.bookSeat(showId, seatNumber);

        if (success) {
            System.out.println("\n===== UPDATED SEAT STATUS =====");

            for (Seat seat : seatDAO.getSeatsForShow(showId)) {
                seat.displaySeat();
            }
        }

        scanner.close();
    }
}
