
public class Movie {
    private int movieId;
    private String name;
    private String genre;
    private int duration;
    private double rating;

    public Movie(int movieId, String name, String genre,
                 int duration, double rating) {
        this.movieId = movieId;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public double getRating() {
        return rating;
    }

    public void displayMovie() {
        System.out.println("Movie ID: " + movieId
                + " | " + name
                + " | " + genre
                + " | " + duration + " mins"
                + " | Rating: " + rating);
    }
}
