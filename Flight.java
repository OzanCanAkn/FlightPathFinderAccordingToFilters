import java.time.LocalDateTime;

public class Flight {
    String company;
    String flightNo;
    String dept;
    String arr;
    String date;
    String clock;
    String day;
    String duration;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    LocalDateTime time;
    LocalDateTime arriveTime;
    int price;

    public Flight(String company, String flightNo, String dept, String arr, String date, String clock, String day, String duration, int price) {
        this.company = company;
        this.flightNo = flightNo;
        this.dept = dept;
        this.arr = arr;
        this.date = date;
        this.clock = clock;
        this.day = day;
        this.duration = duration;
        this.price = price;
        this.time=LocalDateTime.parse(date.split("/")[2]+"-"+date.split("/")[1]+"-"+date.split("/")[0]+"T"+clock+":00");
    }
    LocalDateTime getArriveTime(){
        this.arriveTime=this.time.plusHours(Integer.parseInt(this.duration.split(":")[0]));
        this.arriveTime=this.arriveTime.plusMinutes(Integer.parseInt(this.duration.split(":")[1]));
        return this.arriveTime;
    }
}
