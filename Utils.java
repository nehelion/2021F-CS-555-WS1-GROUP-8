import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.io.*;
import java.util.*;

public class Utils {

    private String today;

    public Utils() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        this.today = dtf.format(now);
    }

    public int monthToInt(String month)
	{
		switch (month) 
		{
            case "JAN": 
				return 1;
            case "FEB":  
				return 2;
            case "MAR": 
				return 3;
            case "APR":  
				return 4;
            case "MAY": 
				return 5;
            case "JUN": 
				return 6;
            case "JUL": 
				return 7;
            case "AUG":  
				return 8;
            case "SEP": 
				return 9;
            case "OCT": 
				return 10;
            case "NOV": 
				return 11;
            case "DEC": 
				return 12;
            default: 
				return -1;
        }
	}

    public boolean isDeathInFuture(String date) {
        System.out.println(today);
        return false;
    }

   //  public boolean isMarriageBeforeDeath(Family[] families)
   //  {
   //    for (Family fams : families) {
	// 		if(fams.getWifeID() != null && fams.getHusbandID() != null)
	// 		{
	// 			String wife_id = fams.getWifeID();
	// 			String husb_id = fams.getHusbandID();
	// 			Individual temp_wife = indi_dict.get(wife_id);
	// 			Individual temp_husb = indi_dict.get(husb_id);	

	// 		}
	// 	}
   //  }
    

    public String readFile(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
        {
 
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) 
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}