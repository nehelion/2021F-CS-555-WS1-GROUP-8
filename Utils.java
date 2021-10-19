import java.time.format.DateTimeFormatter;  
import java.text.*;  
import java.time.LocalDateTime;    
import java.io.*;
import java.util.*;
import java.util.Date;

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

    public String isMarriageBeforeDeath(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      for(String ids : families.keySet()) {
         Family fam = families.get(ids);
			if(fam.getWifeID() != null && fam.getHusbandID() != null)
			{
				String wife_id = fam.getWifeID();
				String husb_id = fam.getHusbandID();
				Individual temp_wife = individuals.get(wife_id);
				Individual temp_husb = individuals.get(husb_id);
            String marrDate = fam.getMarrDate();
            String husb_death = temp_husb.getDeathday();
            String wife_death = temp_wife.getDeathday();
            SimpleDateFormat tf = new SimpleDateFormat("yyyy/MM/dd");
            Date mDate = tf.parse(marrDate);
            if(wife_death != null)
            {
               //Date wDeath = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH)
               Date wDeath = tf.parse(wife_death);
               
               if(wDeath.before(mDate))
               {
                  out = out.concat("ERROR: USO1 conflict with ").concat(wife_id).concat(".\n");
               }
            }

            if(husb_death != null)
            {
               //Date wDeath = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH)
               Date hDeath = tf.parse(husb_death);
               
               if(hDeath.before(mDate))
               {
                  out = out.concat("ERROR: USO1 conflict with ").concat(husb_id).concat(".\n");
               }
            }

			}
		}
      if(out.length() == 0)
      {
         out = out.concat("Correct");
      }
      return out;
    }

    public String isDivorceBeforeDeath(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      System.out.println("sdf");
      for(String ids : families.keySet()) {
         Family fam = families.get(ids);
			if(fam.getWifeID() != null && fam.getHusbandID() != null && fam.getDivDate() != null)
			{
            
				String wife_id = fam.getWifeID();
				String husb_id = fam.getHusbandID();
				Individual temp_wife = individuals.get(wife_id);
				Individual temp_husb = individuals.get(husb_id);
            String divDate = fam.getDivDate();
            String husb_death = temp_husb.getDeathday();
            String wife_death = temp_wife.getDeathday();
            SimpleDateFormat tf = new SimpleDateFormat("yyyy/MM/dd");
            Date dDate = tf.parse(divDate);
            if(wife_death != null)
            {
               //Date wDeath = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH)
               Date wDeath = tf.parse(wife_death);
               
               if(wDeath.before(dDate))
               {
                  out = out.concat("ERROR: USO5 conflict with ").concat(wife_id).concat(".\n");
               }
            }

            if(husb_death != null)
            {
               //Date wDeath = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH)
               Date hDeath = tf.parse(husb_death);
               
               if(hDeath.before(dDate))
               {
                  out = out.concat("ERROR: USO6 conflict with ").concat(husb_id).concat(".\n");
               }
            }

			}
		}
      if(out.length() == 0)
      {
         out.concat("Correct");
      }
      return out;
    }
    
    

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