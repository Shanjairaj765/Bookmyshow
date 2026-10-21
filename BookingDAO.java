
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;

public class BookingDAO {

    // Book seats and save the complete booking in one transaction.
    public int bookSeats(String customerName, int showId,
                         List<Integer> seatNumbers) {

        if (customerName == null || customerName.isBlank()
                || showId <= 0 || seatNumbers == null
                || seatNumbers.isEmpty()) {
            System.out.println("Invalid booking details.");
            return -1;
        }

        Set<Integer> uniqueSeats = new HashSet<>(seatNumbers);

        if (uniqueSeats.size() != seatNumbers.size()
                || uniqueSeats.stream().anyMatch(n -> n <= 0)) {
            System.out.println("Invalid seat selection.");
            return -1;
        }

        List<Integer> sortedSeats = new ArrayList<>(uniqueSeats);
        Collections.sort(sortedSeats);

        String priceSql =
                "SELECT ticket_price FROM shows WHERE show_id = ?";

        String seatSql = """
                SELECT seat_id, seat_number, is_booked
                FROM seats
                WHERE show_id = ?
                ORDER BY seat_number
                FOR UPDATE
                """;

        String bookingSql = """
                INSERT INTO bookings
                    (customer_name, total_amount, status)
                VALUES (?, ?, 'CONFIRMED')
                """;

        String updateSeatSql = """
                UPDATE seats SET is_booked = TRUE
                WHERE seat_id = ? AND is_booked = FALSE
                """;

        String bookingSeatSql = """
                INSERT INTO booking_seats (booking_id, seat_id)
                VALUES (?, ?)
                """;

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                double ticketPrice;

                try (PreparedStatement ps =
                             connection.prepareStatement(priceSql)) {
                    ps.setInt(1, showId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Show not found.");
                        }
                        ticketPrice = rs.getDouble("ticket_price");
                    }
                }

                List<Integer> seatIds = new ArrayList<>();

                try (PreparedStatement ps =
                             connection.prepareStatement(seatSql)) {
                    ps.setInt(1, showId);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int number = rs.getInt("seat_number");

                            if (uniqueSeats.contains(number)) {
                                if (rs.getBoolean("is_booked")) {
                                    throw new SQLException(
                                            "Seat " + number
                                            + " is already booked."
                                    );
                                }
                                seatIds.add(rs.getInt("seat_id"));
                            }
                        }
                    }
                }

                if (seatIds.size() != sortedSeats.size()) {
                    throw new SQLException(
                            "One or more selected seats do not exist."
                    );
                }

                int bookingId;
                double total = ticketPrice * sortedSeats.size();

                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     bookingSql,
                                     Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, customerName.trim());
                    ps.setDouble(2, total);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException(
                                    "Could not generate booking ID."
                            );
                        }
                        bookingId = keys.getInt(1);
                    }
                }

                try (PreparedStatement update =
                             connection.prepareStatement(updateSeatSql);
                     PreparedStatement link =
                             connection.prepareStatement(bookingSeatSql)) {

                    for (int seatId : seatIds) {
                        update.setInt(1, seatId);

                        if (update.executeUpdate() != 1) {
                            throw new SQLException(
                                    "Seat booking conflict."
                            );
                        }

                        link.setInt(1, bookingId);
                        link.setInt(2, seatId);
                        link.executeUpdate();
                    }
                }

                connection.commit();

                System.out.println("\nBooking successful!");
                System.out.println("Booking ID: " + bookingId);
                System.out.println("Customer: " + customerName);
                System.out.println("Show ID: " + showId);
                System.out.println("Seats: " + sortedSeats);
                System.out.printf("Total: Rs. %.2f%n", total);

                return bookingId;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                System.out.println("Booking failed: " + e.getMessage());
                return -1;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return -1;
        }
    }

    // Display booking details using the booking ID.
    public void viewBooking(int bookingId) {

        String sql = """
                SELECT b.booking_id, b.customer_name, b.booking_time,
                       b.total_amount, b.status,
                       s.show_id, s.show_time, s.ticket_price,
                       m.name AS movie_name,
                       t.name AS theatre_name,
                       GROUP_CONCAT(seat.seat_number
                           ORDER BY seat.seat_number) AS seat_numbers
                FROM bookings b
                LEFT JOIN booking_seats bs
                    ON b.booking_id = bs.booking_id
                LEFT JOIN seats seat
                    ON bs.seat_id = seat.seat_id
                LEFT JOIN shows s
                    ON seat.show_id = s.show_id
                LEFT JOIN movies m
                    ON s.movie_id = m.movie_id
                LEFT JOIN theatres t
                    ON s.theatre_id = t.theatre_id
                WHERE b.booking_id = ?
                GROUP BY b.booking_id, b.customer_name, b.booking_time,
                         b.total_amount, b.status, s.show_id,
                         s.show_time, s.ticket_price,
                         m.name, t.name
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, bookingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Booking not found.");
                    return;
                }

                System.out.println("\n===== BOOKING DETAILS =====");
                System.out.println("Booking ID: " + rs.getInt("booking_id"));
                System.out.println("Customer: " + rs.getString("customer_name"));
                System.out.println("Movie: " + rs.getString("movie_name"));
                System.out.println("Theatre: " + rs.getString("theatre_name"));
                System.out.println("Show time: " + rs.getString("show_time"));
                System.out.println("Seats: " + rs.getString("seat_numbers"));
                System.out.printf("Total: Rs. %.2f%n",
                        rs.getDouble("total_amount"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("===========================\n");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing booking: " + e.getMessage());
        }
    }

    // Cancel a confirmed booking and release its seats atomically.
    public boolean cancelBooking(int bookingId) {

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                List<Integer> seatIds = new ArrayList<>();

                // Lock the booking record and check its status.
                try (PreparedStatement ps = connection.prepareStatement(
                        "SELECT status FROM bookings "
                        + "WHERE booking_id = ? FOR UPDATE")) {

                    ps.setInt(1, bookingId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Booking not found.");
                        }

                        if (!"CONFIRMED".equals(rs.getString("status"))) {
                            throw new SQLException(
                                    "Booking is already cancelled."
                            );
                        }
                    }
                }

                // Find the seats linked to this booking.
                try (PreparedStatement ps = connection.prepareStatement(
                        "SELECT seat_id FROM booking_seats "
                        + "WHERE booking_id = ?")) {

                    ps.setInt(1, bookingId);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            seatIds.add(rs.getInt("seat_id"));
                        }
                    }
                }

                // Release the seats.
                try (PreparedStatement ps = connection.prepareStatement(
                        "UPDATE seats SET is_booked = FALSE "
                        + "WHERE seat_id = ?")) {

                    for (int seatId : seatIds) {
                        ps.setInt(1, seatId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                // Keep booking_seats as history; mark booking cancelled.
                try (PreparedStatement ps = connection.prepareStatement(
                        "UPDATE bookings SET status = 'CANCELLED' "
                        + "WHERE booking_id = ? AND status = 'CONFIRMED'")) {

                    ps.setInt(1, bookingId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Could not cancel this booking."
                        );
                    }
                }

                connection.commit();
                System.out.println(
                        "Booking " + bookingId
                        + " cancelled. Its seats are now available."
                );
                return true;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                System.out.println("Cancellation failed: " + e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
    
public void displayBookingsByCustomer(String customerName) {
    String sql = """
        SELECT booking_id
        FROM bookings
        WHERE LOWER(customer_name) = LOWER(?)
        ORDER BY booking_time DESC
        """;

    try (java.sql.Connection connection = DBConnection.getConnection();
         java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {

        ps.setString(1, customerName);

        try (java.sql.ResultSet rs = ps.executeQuery()) {
            boolean found = false;

            while (rs.next()) {
                found = true;
                viewBooking(rs.getInt("booking_id"));
            }

            if (!found) {
                System.out.println(
                    "No bookings found for " + customerName
                );
            }
        }

    } catch (java.sql.SQLException e) {
        System.out.println(
            "Error loading booking history: " + e.getMessage()
        );
    }
}

}
