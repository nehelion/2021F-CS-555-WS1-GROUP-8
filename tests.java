
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class tests {
    public static void main(String[] args) throws Exception
	{
        System.out.println("Good file");
        String testGood = "resources/Kulpin_Family.ged";
		main_project testproj = new main_project();
	    testproj.runGEDFile(testGood);
        //does not produce errors

        System.out.println("---------------");

        System.out.println("Bad file Sprint 1");
        String testBad1 = "resources/sprint1test.ged";
	    testproj.runGEDFile(testBad1);
        //should produce errors for 
        /*
            @I1@ INDI for US05
            @I8@ INDI for US05
            @I8@ INDI for US06
            */
        System.out.println("---------------");
        System.out.println("Bad file Sprint 2");
        String testBad2 = "resources/sprint2test.ged";
        testproj.runGEDFile(testBad2);
        //should produce errors for 
        /*
            @I1@ INDI for US05
            @I8@ INDI for US05
            @I8@ INDI for US06
            */

    }
}
