
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class sprint3Test {
    public static void main(String[] args) throws Exception
	{
    
        System.out.println("---------------");
        
        System.out.println("Bad file Sprint 3");
        String testBad = "resources/sprint3test.ged";
        main_project testproj = new main_project();
        testproj.runGEDFile(testBad);
    }
}
