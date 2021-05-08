import javax.sound.sampled.Port;
import java.io.FileWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Commander {
    String airportPath="";
    String flightPath="";
    public Commander(String airportPath, String flightPath) {
        this.airportPath = airportPath;
        this.flightPath = flightPath;
    }
    Writer writer=new Writer();
    FileReader fileReader=new FileReader();
    ArrayList<Flight>flights=fileReader.readFlights(this.flightPath);
    ArrayList<City> cities = fileReader.readAirports(this.airportPath);


    String difTime(LocalDateTime t1,LocalDateTime t2){
        Duration duration = Duration.between(t1, t2);
        long hours=duration.toHours();
        long minutes=duration.toMinutes();
        minutes=minutes%60;
        if (minutes<10 & hours<10){
            return "0"+hours+":"+"0"+minutes;
        }else if(minutes<10){
            return hours+":"+"0"+minutes;
        }else if(hours<10){
            return "0"+hours+":"+minutes;
        }
        return hours+":"+minutes;
    }

    ArrayList<ArrayList<Flight>> findCheap(ArrayList<ArrayList<Flight>> paths){
        ArrayList<ArrayList<Flight>> cheap=new ArrayList<>();
        int minCost=findCost(paths.get(0));
        cheap.add(paths.get(0));
        for (ArrayList<Flight> p:paths){
            int cost=0;
            for (Flight f:p){
                cost=cost+f.price;
            }
            if (cost<minCost){
                minCost=cost;
                cheap.clear();
                cheap.add(p);
            }else if (cost==minCost){
                if (!cheap.contains(p)){
                    cheap.add(p);
                }
            }
        }
        return cheap;
    }
    int findCost(ArrayList<Flight> flights){
        int cost=0;
        for (Flight f:flights){
            cost=cost+f.price;
        }
        return cost;
    }
    ArrayList<ArrayList<Flight>> findFast(ArrayList<ArrayList<Flight>> paths){
        ArrayList<ArrayList<Flight>> quick=new ArrayList<>();
        LocalDateTime fast = paths.get(0).get(paths.get(0).size()-1).getArriveTime();
        quick.add(paths.get(0));
        for (ArrayList<Flight> p:paths){
            if (p.get(p.size()-1).getArriveTime().isBefore(fast)){
                if (!quick.contains(p)){
                    quick.clear();
                    quick.add(p);
                    fast=p.get(p.size()-1).getArriveTime();
                }
            }else if (p.get(p.size()-1).getArriveTime().isAfter(fast)){
                continue;
            }else{
                if (!quick.contains(p)) {
                    quick.add(p);
                }
            }
        }
        return quick;
    }
    ArrayList<ArrayList<Flight>> findPropers(ArrayList<ArrayList<Flight>> paths){
        ArrayList<ArrayList<Flight>> propers=new ArrayList<>();
        ArrayList<ArrayList<Flight>> cheap=findCheap(paths);
        cheap=findFast(cheap);
        ArrayList<ArrayList<Flight>> fast=findFast(paths);
        fast=findCheap(fast);
        int minCost=findCost(fast.get(0));
        LocalDateTime fastest=cheap.get(0).get(fast.get(0).size()-1).getArriveTime();
        for (ArrayList<Flight> flights:paths){
            if ((findCost(flights)<minCost & flights.get(flights.size()-1).getArriveTime().isBefore(fastest))){
                cheap.add(flights);
            }
        }
        for (ArrayList a:cheap){
            if (!propers.contains(a))
                propers.add(a);
        }for (ArrayList a:fast){
            if (!propers.contains(a))
                propers.add(a);
        }
        return propers;
    }
    void listAll(String direction,String date){
        FindPath pathFinder=new FindPath(flights,cities);
        writer.write("command : listAll"+"\t"+direction+"\t"+date+"\n");
        LocalDateTime time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T00:00:00");
        ArrayList<ArrayList<Flight>> paths=pathFinder.findPath(direction.split("->")[0],direction.split("->")[1]);
        for (ArrayList<Flight> a:paths){
            int cost=0;
            if (a.get(0).getArriveTime().isAfter(time)){
                for (int i=0;i<a.size();i++){
                    cost=cost+a.get(i).price;
                    if (i==a.size()-1){
                        writer.write(a.get(i).company+a.get(i).flightNo+"\t"+a.get(i).dept+"->"+a.get(i).arr+"\t"+difTime(a.get(0).time,a.get(i).getArriveTime())+"/"+cost+"\n");
                    }else
                        writer.write(a.get(i).company+a.get(i).flightNo+"\t"+a.get(i).dept+"->"+a.get(i).arr+"||");
                }
            }
        }
        writer.write("\n\n");

    }
    void listProper(String direction,String date){
        FindPath pathFinder=new FindPath(flights,cities);
        writer.write("command : listProper"+"\t"+direction+"\t"+date+"\n");
        LocalDateTime time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T00:00:00");
        ArrayList<ArrayList<Flight>> paths=pathFinder.findPath(direction.split("->")[0],direction.split("->")[1]);
        ArrayList<ArrayList<Flight>> propers=findPropers(paths);
        for (ArrayList<Flight> a:propers){
            int cost=0;
            if (a.get(0).getArriveTime().isAfter(time)){
                for (int i=0;i<a.size();i++){
                    cost=cost+a.get(i).price;
                    if (i==a.size()-1){
                        writer.write(a.get(i).company+a.get(i).flightNo+"\t"+a.get(i).dept+"->"+a.get(i).arr+"\t"+difTime(a.get(0).time,a.get(i).getArriveTime())+"/"+cost+"\n");
                    }else
                        writer.write(a.get(i).company+a.get(i).flightNo+"\t"+a.get(i).dept+"->"+a.get(i).arr+"||");
                }
                writer.write("\n\n");
            }
        }
    }

    void listQuickest(String direction,String date){
        FindPath pathFinder=new FindPath(flights,cities);
        writer.write("command : listQuickest"+"\t"+direction+"\t"+date+"\n");
        LocalDateTime time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T00:00:00");
        ArrayList<ArrayList<Flight>> paths=pathFinder.findPath(direction.split("->")[0],direction.split("->")[1]);
        ArrayList<ArrayList<Flight>> quickest=findFast(paths);
        for (ArrayList<Flight> a:quickest){
            int cost=0;
            if (a.get(0).getArriveTime().isAfter(time)){
                for (int i=0;i<a.size();i++){
                    cost=cost+a.get(i).price;
                    if (i==a.size()-1){
                        writer.write(a.get(i).company+a.get(i).flightNo+"\t"+a.get(i).dept+"->"+a.get(i).arr+"\t"+difTime(a.get(0).time,a.get(i).getArriveTime())+"/"+cost+"\n");
                    }else
                        writer.write(a.get(i).company+a.get(i).flightNo+"\t"+a.get(i).dept+"->"+a.get(i).arr+"||");
                }
                writer.write("\n\n");
            }
        }
    }
    void listCheapest(String direction,String date){
        FindPath pathFinder=new FindPath(flights,cities);
        writer.write("command : listCheapest"+"\t"+direction+"\t"+date+"\n");
        LocalDateTime time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T00:00:00");
        ArrayList<ArrayList<Flight>> paths=pathFinder.findPath(direction.split("->")[0],direction.split("->")[1]);
        ArrayList<ArrayList<Flight>> cheapest=findCheap(paths);
        for (ArrayList<Flight> a:cheapest){
            int cost=0;
            if (a.get(0).getArriveTime().isAfter(time)){
                for (int i=0;i<a.size();i++){
                    cost=cost+a.get(i).price;
                    if (i==a.size()-1){
                        writer.write(a.get(i).company+a.get(i).flightNo+"\t"+a.get(i).dept+"->"+a.get(i).arr+"\t"+difTime(a.get(0).time,a.get(i).getArriveTime())+"/"+cost+"\n");
                    }else
                        writer.write(a.get(i).company+a.get(i).flightNo+"\t"+a.get(i).dept+"->"+a.get(i).arr+"||");
                }
                writer.write("\n\n");
            }
        }
    }
    void listCheaper(String direction,String date,int minCost){
        FindPath pathFinder=new FindPath(flights,cities);
        writer.write("command : listCheaper"+"\t"+direction+"\t"+date+"\t"+minCost+"\n");
        LocalDateTime time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T00:00:00");
        ArrayList<ArrayList<Flight>> paths=pathFinder.findPath(direction.split("->")[0],direction.split("->")[1]);
        ArrayList<ArrayList<Flight>> propers=findPropers(paths);

        int counter=0;
        for (ArrayList<Flight> a:propers){
            int cost=findCost(a);
            if (a.get(0).getArriveTime().isAfter(time)){
                for (int i=0;i<a.size();i++) {
                    if (i == a.size() - 1 & cost < minCost) {
                        counter++;
                        writer.write(a.get(i).company + a.get(i).flightNo + "\t" + a.get(i).dept + "->" + a.get(i).arr + "\t" + difTime(a.get(0).time, a.get(i).getArriveTime()) + "/" + cost + "\n");
                    } else if (cost < minCost){
                        writer.write(a.get(i).company + a.get(i).flightNo + "\t" + a.get(i).dept + "->" + a.get(i).arr + "||");
                    counter++;
                    }
                }
            }
        }
        if (counter==0)
            writer.write("No suitable flight plan is found\n");
        writer.write("\n\n");
    }
    void listQuicker(String direction,String date,String date1,String clock,String day){
        FindPath pathFinder=new FindPath(flights,cities);
        writer.write("command : listQuicker"+"\t"+direction+"\t"+date+"\t"+date1+" "+clock+" "+day+"\n");
        LocalDateTime time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T00:00:00");
        LocalDateTime time1=LocalDateTime.parse(date1.split("/")[2]+"-"+date1.split("/")[1]+"-"+date1.split("/")[0]+"T"+clock+":00");
        ArrayList<ArrayList<Flight>> paths=pathFinder.findPath(direction.split("->")[0],direction.split("->")[1]);
        ArrayList<ArrayList<Flight>> propers=findPropers(paths);
        int counter=0;
        for (ArrayList<Flight> a:propers){
            int cost=0;
            if (a.get(0).getArriveTime().isAfter(time)){
                for (int i=0;i<a.size();i++) {
                    cost = cost + a.get(i).price;
                    if (i == a.size() - 1 & a.get(a.size()-1).getArriveTime().isBefore(time1)) {
                        counter++;
                        writer.write(a.get(i).company + a.get(i).flightNo + "\t" + a.get(i).dept + "->" + a.get(i).arr + "\t" + difTime(a.get(0).time, a.get(i).getArriveTime()) + "/" + cost + "\n");
                    } else if ( a.get(a.size()-1).getArriveTime().isBefore(time1)){
                        writer.write(a.get(i).company + a.get(i).flightNo + "\t" + a.get(i).dept + "->" + a.get(i).arr + "||");
                        counter++;
                    }
                }
            }
        }
        if (counter==0)
            writer.write("No suitable flight plan is found"+"\n");
        writer.write("\n\n");
    }
    void listExcluding(String direction,String date,String brand){
        FindPath pathFinder=new FindPath(flights,cities);
        writer.write("command : listExcluding"+"\t"+direction+"\t"+date+"\t"+brand+"\n");
        LocalDateTime time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T00:00:00");
        ArrayList<ArrayList<Flight>> paths=pathFinder.findPath(direction.split("->")[0],direction.split("->")[1]);
        ArrayList<ArrayList<Flight>> propers=findPropers(paths);
        int counter=0;
        for (ArrayList<Flight> a:propers){
            int cost=0;
            if (a.get(0).getArriveTime().isAfter(time) & brandCheck(a,brand)){
                for (int i=0;i<a.size();i++) {
                    cost = cost + a.get(i).price;
                    if (i == a.size() - 1) {
                        counter++;
                        writer.write(a.get(i).company + a.get(i).flightNo + "\t" + a.get(i).dept + "->" + a.get(i).arr + "\t" + difTime(a.get(0).time, a.get(i).getArriveTime()) + "/" + cost + "\n");
                    } else{
                        writer.write(a.get(i).company + a.get(i).flightNo + "\t" + a.get(i).dept + "->" + a.get(i).arr + "||");
                        counter++;
                    }
                }
            }
        }

        if (counter==0)
            writer.write("No suitable flight plan is found"+"\n");
        writer.write("\n\n");
    }
    void listOnlyFrom(String direction,String date,String brand){
        FindPath pathFinder=new FindPath(flights,cities);
        writer.write("command : listOnlyFrom"+"\t"+direction+"\t"+date+"\t"+brand+"\n");
        LocalDateTime time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T00:00:00");
        ArrayList<ArrayList<Flight>> paths=pathFinder.findPath(direction.split("->")[0],direction.split("->")[1]);
        ArrayList<ArrayList<Flight>> propers=findPropers(paths);
        int counter=0;
        for (ArrayList<Flight> a:propers){
            int cost=0;
            if (a.get(0).getArriveTime().isAfter(time) & reverseBrandCheck(a,brand)){
                for (int i=0;i<a.size();i++) {
                    cost = cost + a.get(i).price;
                    if (i == a.size() - 1) {
                        counter++;
                        writer.write(a.get(i).company + a.get(i).flightNo + "\t" + a.get(i).dept + "->" + a.get(i).arr + "\t" + difTime(a.get(0).time, a.get(i).getArriveTime()) + "/" + cost + "\n");
                    } else{
                        writer.write(a.get(i).company + a.get(i).flightNo + "\t" + a.get(i).dept + "->" + a.get(i).arr + "||");
                        counter++;
                    }
                }
            }
        }

        if (counter==0)
            writer.write("No suitable flight plan is found"+"\n");
        writer.write("\n\n");
    }
    boolean brandCheck(ArrayList<Flight> flights,String brand){
        for (Flight f:flights){
            if (f.company.equals(brand)){
                return false;
            }
        }
        return true;
    }
    boolean reverseBrandCheck(ArrayList<Flight> flights,String brand){
        for (Flight f:flights){
            if (!f.company.equals(brand)){
                return false;
            }
        }
        return true;
    }
    int findCheapestVal(ArrayList<ArrayList<Flight>> paths){
        int minCost=0;
        for (Flight f:paths.get(0)){
            minCost=minCost+f.price;
        }
        for (ArrayList<Flight> p:paths){
            int cost=0;
            for (Flight f:p){
                cost=cost+f.price;
            }
            if (cost<minCost){
                minCost=cost;
            }
        }
        return minCost;
    }
    void diameterOfGraph(){
        int diameter=0;
        for (City c:this.cities){
            for (City a:this.cities){
                if (a!=c) {
                    FindPath pathFinder=new FindPath(flights,cities);
                    ArrayList<ArrayList<Flight>> paths=new ArrayList<>();
                    paths = pathFinder.findPath(c.name, a.name);
                    if (paths.size() == 0) {
                        continue;
                    }
                    int initialCost = findCheapestVal(paths);
                    if (initialCost > diameter) {
                        diameter = initialCost;
                    }
                }
            }
        }
        writer.write("command : diameterOfGraph"+"\n");
        writer.write("The diameter of graph : "+diameter+"\n\n\n");
    }
    void pageRankOfNodes(){
        double dValue=0.85;
        //creating page array for calculate easy
        ArrayList<Page>pages=new ArrayList<Page>();
        for (City c:cities){
            Page page=new Page(c.name);
            page.portNames.add(c.port);
            pages.add(page);
        }
        for (Flight f:flights){
            try {
                pageSearch(f.dept,pages).outGoing.add(pageSearch(f.arr,pages));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                pageSearch(f.arr, pages).outCome.add(pageSearch(f.dept, pages));
            } finally {

            }
        }
        //End of creating array
        //starting to calculate
        dValue=0.85;
        for (Page p:pages){
            p.prevRank=1.0/pages.size();;//initialing the first value of page ranks which is 1/pages.size()
        }
        for (int i=0;i<1000;i++){//for iteration
            for (Page p:pages){//my algorithm ranking all nodes contains which is dont used in flightlist.txt ,like in the given simulator.
                double totalRanks=0;
                for (Page pp:p.outCome) {
                    totalRanks=totalRanks+pp.prevRank/pp.outGoing.size();// (PR(T1)/C(T1) + … + PR(Tn)/C(Tn))
                                                                            //the mathematical function of pageranks which uses the
                                                                            //previous value of all pages every iterations are unique
                }
                p.rank=Math.round(((1-dValue)/pages.size()+dValue*totalRanks) * 1000.0) / 1000.0;// (1-d) + d (PR(T1)/C(T1) + … + PR(Tn)/C(Tn))
            }
            for (Page p:pages){
                p.prevRank=p.rank;//initializing the new ranks as old ranks for new calculation
            }
        }
        double total=0;
        for (Page p:pages){//re-rangeing to total of ranks
            total=total+p.rank;
        }
        total=1/total;
        writer.write("command : pageRankOfNodes\n");
        for (Page p:pages){
            writer.write(p.portNames.get(0)+"   :   "+Math.round(p.rank*total*1000.0)/1000.0+"\n");//writing ranks to file
        }

    }
    boolean isContains(String city,ArrayList<Page>pages){
        for (Page p:pages){
            if (p.cityName.equals(city)){
                return true;
            }
        }
        return false;
    }
    Page pageSearch(String port,ArrayList<Page>pages){
        for (Page p:pages){
            if (p.portNames.contains(port)){
                return p;
            }
        }
        return null;
    }

}
