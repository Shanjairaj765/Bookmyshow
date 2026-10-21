
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        String sql = "SELECT movie_id, name, genre, duration, rating FROM movies";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Movie movie = new Movie(
                    result.getInt("movie_id"),
                    result.getString("name"),
                    result.getString("genre"),
                    result.getInt("duration"),
                    result.getDouble("rating")
                );

                movies.add(movie);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching movies: " + e.getMessage());
        }

        return movies;
    }
}
