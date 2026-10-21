
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final BookMyShowService service =
            new BookMyShowService();

    private static final BookingDAO bookingDAO = new BookingDAO();
    private static final SeatDAO seatDAO = new SeatDAO();

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("     BOOKMYSHOW CONSOLE APP");
        System.out.println("================================");

        boolean running = true;

        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. View Movies");
            System.out.println("2. View Shows");
            System.out.println("3. View Available Seats");
            System.out.println("4. Book Tickets");
            System.out.println("5. View My Bookings");
            System.out.println("6. View Booking by ID");
            System.out.println("7. Cancel Booking");
            System.out.println("8. Admin Menu");
            System.out.println("0. Exit");
            System.out.println("================================");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    service.displayMovies();
                    break;

                case 2:
                    service.displayShows();
                    break;

                case 3:
                    viewSeats();
                    break;

                case 4:
                    bookTickets();
                    break;

                case 5:
                    String customer =
                            readText("Enter your customer name: ");

                    if (!customer.isBlank()) {
                        service.displayBookings(customer);
                    } else {
                        System.out.println("Customer name cannot be empty.");
                    }
                    break;

                case 6:
                    int viewId = readInt("Enter booking ID: ");

                    if (viewId > 0) {
                        bookingDAO.viewBooking(viewId);
                    } else {
                        System.out.println("Booking ID must be positive.");
                    }
                    break;

                case 7:
                    cancelBooking();
                    break;

                case 8:
                    adminMenu();
                    break;

                case 0:
                    running = false;
                    System.out.println(
                            "Thank you for using BookMyShow!"
                    );
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    private static void viewSeats() {
        service.displayShows();

        int showId = readInt("\nEnter show ID: ");

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

        System.out.println("=======================");
    }

    private static void bookTickets() {
        service.displayShows();

        int showId = readInt("\nEnter show ID: ");

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

        String customerName =
                readText("\nEnter customer name: ");

        if (customerName.isBlank()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        String seatInput = readText(
                "Enter seat numbers separated by commas (e.g. 1,2): "
        );

        if (seatInput.isBlank()) {
            System.out.println("Please select at least one seat.");
            return;
        }

        List<Integer> seatNumbers = new ArrayList<>();

        try {
            for (String value : seatInput.split(",")) {
                if (value.trim().isEmpty()) {
                    throw new NumberFormatException();
                }

                seatNumbers.add(Integer.parseInt(value.trim()));
            }
        } catch (NumberFormatException e) {
            System.out.println(
                    "Invalid seat input. Example: 1,2,3"
            );
            return;
        }

        int bookingId = bookingDAO.bookSeats(
                customerName, showId, seatNumbers
        );

        if (bookingId > 0) {
            System.out.println("\n===== UPDATED SEAT STATUS =====");

            for (Seat seat : seatDAO.getSeatsForShow(showId)) {
                seat.displaySeat();
            }
        }
    }

    private static void cancelBooking() {
        int bookingId = readInt("Enter booking ID to cancel: ");

        if (bookingId <= 0) {
            System.out.println("Booking ID must be positive.");
            return;
        }

        System.out.print(
                "Are you sure you want to cancel booking "
                + bookingId + "? (yes/no): "
        );

        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("yes")) {
            boolean cancelled = bookingDAO.cancelBooking(bookingId);

            if (cancelled) {
                System.out.println("\nUpdated booking details:");
                bookingDAO.viewBooking(bookingId);
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    private static void adminMenu() {
        boolean inAdminMenu = true;

        while (inAdminMenu) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View Movies");
            System.out.println("2. Add Movie");
            System.out.println("0. Back to Main Menu");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    service.displayMovies();
                    break;

                case 2:
                    String name = readText("Movie name: ");
                    String genre = readText("Genre: ");
                    int duration = readInt("Duration in minutes: ");
                    double rating = readDouble("Rating out of 10: ");

                    if (name.isBlank() || genre.isBlank()
                            || duration <= 0
                            || rating < 0 || rating > 10) {
                        System.out.println(
                                "Invalid movie details. Please try again."
                        );
                        break;
                    }

                    service.addMovie(name, genre, duration, rating);
                    break;

                case 0:
                    inAdminMenu = false;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid whole number."
                );
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
