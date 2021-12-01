
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class sprint4Test {
    

    public static void main(String[] args) throws Exception
	{
    
        System.out.println("---------------");
        
        System.out.println("Bad file Sprint 3");
        String testBad = "resources/sprint4test.ged";
        main_project testproj = new main_project();
        testproj.runGEDFile(testBad);
    }
}