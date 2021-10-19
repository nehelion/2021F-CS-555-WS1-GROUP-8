
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

        System.out.println("Bad file");
        String testBad = "resources/sprint1test.ged";
	    testproj.runGEDFile(testBad);
        //should produce errors for 
        /*
            @I1@ INDI for US05
            @I8@ INDI for US05
            @I8@ INDI for US06
            */
    }
}
