
import java.util.Scanner;

public class BookingHistoryTest {

    public static void main(String[] args) {

        BookingDAO bookingDAO = new BookingDAO();

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("===== BOOKMYSHOW =====");
            System.out.println("1. View Booking");
            System.out.println("2. Cancel Booking");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid option.");
                return;
            }

            int choice = scanner.nextInt();

            if (choice != 1 && choice != 2) {
                System.out.println("Choose 1 or 2.");
                return;
            }

            System.out.print("Enter Booking ID: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid Booking ID.");
                return;
            }

            int bookingId = scanner.nextInt();

            if (bookingId <= 0) {
                System.out.println("Booking ID must be positive.");
                return;
            }

            if (choice == 1) {
                bookingDAO.viewBooking(bookingId);
            } else {
                System.out.print(
                    "Are you sure you want to cancel? (yes/no): "
                );

                String confirmation = scanner.next();

                if (confirmation.equalsIgnoreCase("yes")) {
                    boolean cancelled =
                            bookingDAO.cancelBooking(bookingId);

                    if (cancelled) {
                        System.out.println("\nUpdated booking details:");
                        bookingDAO.viewBooking(bookingId);
                    }
                } else {
                    System.out.println("Cancellation aborted.");
                }
            }
        }
    }
}
