
public class Theatre {
    private int theatreId;
    private String name;
    private String location;

    public Theatre(int theatreId, String name, String location) {
        this.theatreId = theatreId;
        this.name = name;
        this.location = location;
    }

    public int getTheatreId() {
        return theatreId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void displayTheatre() {
        System.out.println("Theatre ID: " + theatreId
                + " | " + name
                + " | Location: " + location);
    }
}
