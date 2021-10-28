
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class sprint2Test {
    public static void main(String[] args) throws Exception
	{
        System.out.println("Good file");
        String testGood = "resources/Kulpin_Family.ged";
		main_project testprojGood = new main_project();
	    testprojGood.runGEDFile(testGood);
        //does not produce errors

        System.out.println("---------------");
        
        System.out.println("Bad file Sprint 2");
        String testBad = "resources/sprint2test.ged";
        main_project testproj = new main_project();
        testproj.runGEDFile(testBad);
        //should produce errors for 
        /*
            @I11@ INDI for US12
            @I8@ INDI for US05
            @I8@ INDI for US06
            */

    }
}
