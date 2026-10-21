
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowDAO {

    public List<Show> getAllShows() {
        List<Show> shows = new ArrayList<>();

        String sql = """
            SELECT s.show_id, s.show_time, s.ticket_price,
                   m.movie_id, m.name, m.genre, m.duration, m.rating,
                   t.theatre_id, t.name AS theatre_name, t.location,
                   COUNT(se.seat_id) AS total_seats
            FROM shows s
            JOIN movies m ON s.movie_id = m.movie_id
            JOIN theatres t ON s.theatre_id = t.theatre_id
            LEFT JOIN seats se ON s.show_id = se.show_id
            GROUP BY s.show_id, s.show_time, s.ticket_price,
                     m.movie_id, m.name, m.genre, m.duration, m.rating,
                     t.theatre_id, t.name, t.location
            ORDER BY s.show_time
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("movie_id"),
                    rs.getString("name"),
                    rs.getString("genre"),
                    rs.getInt("duration"),
                    rs.getDouble("rating")
                );

                Theatre theatre = new Theatre(
                    rs.getInt("theatre_id"),
                    rs.getString("theatre_name"),
                    rs.getString("location")
                );

                int showId = rs.getInt("show_id");
                int totalSeats = rs.getInt("total_seats");

                Show show = new Show(
                    showId,
                    movie,
                    theatre,
                    rs.getString("show_time"),
                    rs.getDouble("ticket_price"),
                    totalSeats
                );

                shows.add(show);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching shows: " + e.getMessage());
        }

        return shows;
    }
}
