import java.util.ArrayList;

public class FindPath {
    ArrayList<Flight>flights;
    ArrayList<City>cities;
    ArrayList<ArrayList<Flight>> Paths=new ArrayList<>();

    public FindPath(ArrayList<Flight> flights, ArrayList<City> cities) {
        this.flights = flights;
        this.cities = cities;
    }
    void determineEdges(){
        for (City c:cities){
            for (Flight f:flights){
                if (f.dept.equals(c.port)){
                    if (!c.connectedCities.contains(f)){
                        c.setConnectedCities(f);
                    }
                }
            }
        }
    }
    ArrayList<ArrayList<Flight>> findPath(String dep,String arr){
        determineEdges();

        ArrayList<City> depPorts=citySearch(dep);
        ArrayList<City> arrPorts=citySearch(arr);
        for (City c:depPorts){
            for (City cc:arrPorts){
                ArrayList<Flight> css=new ArrayList<>();
                recursive(c,cc,css);
            }
        }
        return this.Paths;
    }
    void visit(City c,boolean to){
        c.visited=to;
        for (City city:cities){
            if (c.getName().equals(city.getName())){
                city.visited=to;
            }
        }
    }
    void recursive(City dep,City arr,ArrayList<Flight> initialList){
        ArrayList<Flight> inFoncList= (ArrayList<Flight>) initialList.clone();
        visit(dep,true);
        int counter=0;
        if (dep==arr){
            Paths.add(inFoncList);
            visitCity(dep.getName(),false);
            return;
        }
        for (int i=0;i<dep.connectedCities.size();i++){

            if(portSearch(dep.connectedCities.get(i).arr).visited){
                continue;
            }

            if (inFoncList.size()>0){
                if (checkTime(inFoncList.get(inFoncList.size()-1),dep.connectedCities.get(i))){
                    inFoncList.add(dep.connectedCities.get(i));
                    recursive(portSearch(dep.connectedCities.get(i).arr),arr,inFoncList);
                    inFoncList.remove(dep.connectedCities.get(i));
                }
            }else {
                inFoncList.add(dep.connectedCities.get(i));
                recursive(portSearch(dep.connectedCities.get(i).arr),arr,inFoncList);
                inFoncList.remove(dep.connectedCities.get(i));
            }

        }
        visit(dep,false);
    }


    City portSearch(String port){
        for(City c:cities){
            if (c.getPort().equals(port)){
                return c;
            }
        }
        return null;
    }
    void visitCity(String city,boolean ch){
        for(City c:cities){
            if (c.getName().equals(city)){

                c.visited=ch;

            }
        }
    }
    City getCity(String city){
        for(City c:cities){
            if (c.getName().equals(city)){
                if (c.visited){
                    return c;
                }
            }
        }
        return null;
    }
    ArrayList<City> citySearch(String city){
        ArrayList<City> cs=new ArrayList<>();
        for(City c:cities){
            if (c.getName().equals(city)){
                cs.add(c);
            }
        }
        return cs;
    }
    boolean checkTime(Flight f1,Flight f2){
        if (f1.getArriveTime().isAfter(f2.time)){
            return false;
        }
        return true;
    }

}
