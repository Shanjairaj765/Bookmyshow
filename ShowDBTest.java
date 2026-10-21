
import java.util.List;

public class ShowDBTest {

    public static void main(String[] args) {
        ShowDAO showDAO = new ShowDAO();
        List<Show> shows = showDAO.getAllShows();

        System.out.println("================================");
        System.out.println("       BOOKMYSHOW SHOWS");
        System.out.println("================================");

        if (shows.isEmpty()) {
            System.out.println("No shows found in database.");
        } else {
            for (Show show : shows) {
                show.displayShow();
                System.out.println("--------------------------------");
            }
        }
    }
}

