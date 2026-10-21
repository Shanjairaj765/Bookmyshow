
import java.util.List;

public class MovieDBTest {

    public static void main(String[] args) {
        MovieDAO movieDAO = new MovieDAO();

        List<Movie> movies = movieDAO.getAllMovies();

        System.out.println("================================");
        System.out.println("       BOOKMYSHOW MOVIES");
        System.out.println("================================");

        if (movies.isEmpty()) {
            System.out.println("No movies found in database.");
        } else {
            for (Movie movie : movies) {
                movie.displayMovie();
                System.out.println("--------------------------------");
            }
        }
    }
}
