
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    public List<Seat> getSeatsForShow(int showId) {
        List<Seat> seats = new ArrayList<>();

        String sql = """
            SELECT seat_number, is_booked
            FROM seats
            WHERE show_id = ?
            ORDER BY seat_number
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, showId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat(
                            rs.getInt("seat_number"),
                            rs.getBoolean("is_booked")
                    );
                    seats.add(seat);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error loading seats: " + e.getMessage()
            );
        }

        return seats;
    }

    // Book one seat for a particular show
    public boolean bookSeat(int showId, int seatNumber) {

        String sql = """
            UPDATE seats
            SET is_booked = TRUE
            WHERE show_id = ?
              AND seat_number = ?
              AND is_booked = FALSE
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, showId);
            statement.setInt(2, seatNumber);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 1) {
                System.out.println(
                        "Seat " + seatNumber
                        + " booked successfully!"
                );
                return true;
            } else {
                System.out.println(
                        "Booking failed! Seat does not exist "
                        + "or is already booked."
                );
                return false;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error booking seat: " + e.getMessage()
            );
            return false;
        }
    }
}
