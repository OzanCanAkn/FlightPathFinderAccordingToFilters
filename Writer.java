import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    public Writer() {
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write("");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while file writing");
            e.printStackTrace();
        }
    }

    void write(String writings){
        try {
            FileWriter myWriter = new FileWriter("output.txt",true);
            myWriter.write(writings);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while file writing");
            e.printStackTrace();
        }
    }
}
