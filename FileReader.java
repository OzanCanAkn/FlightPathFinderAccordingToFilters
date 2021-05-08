import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileReader {
    static String airportPath="";
    static String flightPath="";

    ArrayList<City> readAirports(String airportPath){
        try {
            File myObj = new File(this.airportPath);
            Scanner myReader = new Scanner(myObj);
            ArrayList<City> cities=new ArrayList<>();

            while (myReader.hasNextLine()) {
                String datum = myReader.nextLine();
                String[] data=datum.split("\t");
                City city=new City();
                city.setName(data[0]);
                city.setPort(data[1]);
                if (data.length>2){
                    for (int i=2;i<data.length;i++){
                        City c=new City();
                        c.setName(data[0]);
                        c.setPort(data[i]);
                        cities.add(c);
                    }
                }
                cities.add(city);
            }
            myReader.close();
            return cities;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
    ArrayList<Flight> readFlights(String  flightPath){
        try {
            File myObj = new File(this.flightPath);
            Scanner myReader = new Scanner(myObj);
            ArrayList<Flight> flights=new ArrayList<>();

            while (myReader.hasNextLine()) {
                String datum = myReader.nextLine();
                String[] data=datum.split("\\s+");
                Flight flight=new Flight(data[0].substring(0,2),data[0].substring(2),data[1].split("->")[0],data[1].split("->")[1],data[2],data[3],data[4],data[5],Integer.parseInt(data[6]));
                flights.add(flight);
            }
            myReader.close();
            return flights;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;

    }
    void readCommands(Commander commander,String path){
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            ArrayList<Flight> flights=new ArrayList<>();

            while (myReader.hasNextLine()) {
                String datum = myReader.nextLine();
                String[] data=datum.split("\\s+");
                if (data[0].equals("listAll")){
                    commander.listAll(data[1],data[2]);
                }if (data[0].equals("listProper")){
                    commander.listProper(data[1],data[2]);
                }if (data[0].equals("listQuickest")){
                    commander.listQuickest(data[1],data[2]);
                }if (data[0].equals("listCheapest")){
                    commander.listCheapest(data[1],data[2]);
                }if (data[0].equals("listCheaper")){
                    commander.listCheaper(data[1],data[2],Integer.parseInt(data[3]));
                }if (data[0].equals("listQuicker")){
                    commander.listQuicker(data[1],data[2],data[3],data[4],data[5]);
                }if (data[0].equals("listExcluding")){
                    commander.listExcluding(data[1],data[2],data[3]);
                }if (data[0].equals("listOnlyFrom")){
                    commander.listOnlyFrom(data[1],data[2],data[3]);
                }if (data[0].equals("diameterOfGraph")){
                    commander.diameterOfGraph();
                }if (data[0].equals("pageRankOfNodes")){
                    commander.pageRankOfNodes();
                }
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
