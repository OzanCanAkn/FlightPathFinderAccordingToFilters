import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        FileReader fileReader=new FileReader();
        FileReader.flightPath=args[1];
        FileReader.airportPath=args[0];
        Commander commander=new Commander(args[0],args[1]);
        fileReader.readCommands(commander,args[2]);
    }



}
