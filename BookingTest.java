
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookingTest {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            BookingDAO bookingDAO = new BookingDAO();
            SeatDAO seatDAO = new SeatDAO();

            System.out.println("===== BOOKMYSHOW =====");

            System.out.print("Enter customer name: ");
            String customerName = scanner.nextLine().trim();

            System.out.print("Enter Show ID: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid Show ID.");
                return;
            }
            int showId = scanner.nextInt();

            if (showId <= 0) {
                System.out.println("Show ID must be positive.");
                return;
            }

            List<Seat> seats = seatDAO.getSeatsForShow(showId);

            if (seats.isEmpty()) {
                System.out.println("No seats found for this show.");
                return;
            }

            System.out.println("\n===== SEAT STATUS =====");

            for (Seat seat : seats) {
                seat.displaySeat();
            }

            System.out.print(
                "\nHow many seats do you want to book? "
            );

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid seat count.");
                return;
            }

            int count = scanner.nextInt();

            if (count <= 0 || count > seats.size()) {
                System.out.println("Invalid number of seats.");
                return;
            }

            List<Integer> selectedSeats = new ArrayList<>();

            for (int i = 1; i <= count; i++) {
                System.out.print("Enter seat number " + i + ": ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid seat number.");
                    return;
                }

                selectedSeats.add(scanner.nextInt());
            }

            int bookingId = bookingDAO.bookSeats(
                customerName, showId, selectedSeats
            );

            if (bookingId != -1) {
                System.out.println("\nBooking confirmed!");
                System.out.println(
                    "Keep your Booking ID: " + bookingId
                );

                System.out.println("\n===== UPDATED SEATS =====");

                for (Seat seat : seatDAO.getSeatsForShow(showId)) {
                    seat.displaySeat();
                }
            } else {
                System.out.println(
                    "\nBooking was not completed. "
                    + "Please check the details and try again."
                );
            }
        }
    }
}
