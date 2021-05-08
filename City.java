import java.util.ArrayList;

public class City {
    String name;
    Double rank;
    boolean visited=false;
    public ArrayList<Flight> getConnectedCities() {
        return connectedCities;
    }
    ArrayList<City> toThisCity=new ArrayList<>();
    public void setConnectedCities(Flight c) {
        this.connectedCities.add(c);
    }

    ArrayList<Flight> connectedCities=new ArrayList<>();
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    String port;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
