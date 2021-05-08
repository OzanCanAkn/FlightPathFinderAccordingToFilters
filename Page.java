import java.util.ArrayList;

public class Page {
    String cityName;
    ArrayList<Page> outGoing=new ArrayList<>();
    ArrayList<Page> outCome=new ArrayList<>();
    ArrayList<String> portNames=new ArrayList<>();
    double rank;
    double prevRank;
    public Page(String cityName) {
        this.cityName = cityName;
    }
}
