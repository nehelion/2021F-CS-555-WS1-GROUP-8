import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.text.*;  
import java.time.LocalDateTime;    
import java.io.*;
import java.util.*;
import java.util.Date;
import java.time.*;

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

    public String isDeathInFuture(Map<String, Individual> individuals) throws Exception {
       String out = "";
       for (String id : individuals.keySet()) {
          Individual indiv = individuals.get(id);
          if (indiv.getDeathday() == null) continue;

          String death = indiv.getDeathday();

          SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH);
          Date deathdate = formatter.parse(death);

          Date now = new Date();
          if (now.before(deathdate)) {
             out += "ERROR: US01 conflict with " + indiv.getID() + "\n";
          } else {
             out += "Correct\n";
          }
       }
       return out;
    }

    public String isDeathBeforeBirth(Map<String, Individual> individuals) throws Exception {
       String out = "";
      for (String id : individuals.keySet()) {
         Individual indiv = individuals.get(id);
         if (indiv.getID() == null) continue;

         String birth = indiv.getBirthday();
         String death = indiv.getDeathday();

         if (death == null) continue;

         SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH);

         Date birthdate = formatter.parse(birth);
         Date deathdate = formatter.parse(death);

         if (deathdate.before(birthdate)) {
            out += "ERROR: US03 conflict with " + indiv.getID() + "\n";
         }
         out += "Correct\n";
      }
      return out;
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
                  out = out.concat("ERROR: USO5 conflict with ").concat(wife_id).concat(".\n");
               }
            }

            if(husb_death != null)
            {
               //Date wDeath = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH)
               Date hDeath = tf.parse(husb_death);
               
               if(hDeath.before(mDate))
               {
                  out = out.concat("ERROR: USO5 conflict with ").concat(husb_id).concat(".\n");
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
                  out = out.concat("ERROR: USO6 conflict with ").concat(wife_id).concat(".\n");
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
    
    public String isThereBigamy(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      for(String ids : individuals.keySet())
      {
         Individual indi = individuals.get(ids);
         ArrayList<String> fams = indi.getFams();
         int spot = 0;
         if(fams != null && fams.size() > 1)
         {
            String firstFam = fams.get(spot);
            String marrDate = families.get(firstFam).getMarrDate();
            String divDate = families.get(firstFam).getDivDate();
            SimpleDateFormat tf = new SimpleDateFormat("yyyy/MM/dd");
            for( int i = spot+1; i < fams.size(); i++)
            {
               if(indi.getGender().equalsIgnoreCase("M"))
               {
                  String tempMarrDate = families.get(fams.get((i))).getMarrDate();
                  if(divDate != null)
                  {
                     Date dDate = tf.parse(divDate);
                     Date tmDate = tf.parse(divDate);

                  }
               }
            }
         }
      }
      return "hi";
   }

    public String areParentsTooOld(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      for(String ids : families.keySet()) {
         Family fam = families.get(ids);
         if(!fam.getChildren().isEmpty())
         {
            System.out.println(fam.getID());
            Individual mom = individuals.get(fam.getWifeID());
            Individual dad = individuals.get(fam.getHusbandID());
            long momAge = getAge(mom);
            long dadAge = getAge(dad);
            for(String child : fam.getChildren()) {
               System.out.println(child);
               Individual chilIndi = individuals.get(child);
               long childAge = getAge(chilIndi);
               if(Math.abs(momAge - childAge) > 60)
               {
                  out = out.concat("ERROR: US12 conflict with Mother " + mom.getName() + " (" + mom.getID() + ") being too old for child " + chilIndi.getName() + "(" + chilIndi.getID() +"). \n");
               }
               if(Math.abs(dadAge - childAge) > 80)
               {
                  out = out.concat("ERROR: US12 conflict with Father " + dad.getName() + " (" + dad.getID() + ") being too old for child " + chilIndi.getName() + "(" + chilIndi.getID() +"). \n");
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

    public String areSibilingsMarried(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      for(Family fam : families.values())
      {
         String famId = fam.getID();
         if(fam.getChildren().size() > 1)
         {
            ArrayList<String> children = fam.getChildren();
            for(Family tempFam : families.values())
            {
               String tempFamId = tempFam.getID();
               String wife = tempFam.getWifeID();
               String husb = tempFam.getHusbandID();
               if(children.contains(husb) && children.contains(wife))
               {
                  out = out + "ERROR: US18 conflict with Family " + tempFamId + ".  " + individuals.get(wife).getName() + wife + " and " + individuals.get(husb).getName() + husb + " are sibilings and married. \n";
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
	
	public String bornBeforeDead(Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		for(Individual ind : individuals.values())
		{
			if(ind.getDeathday() != null)
			{
				String[] deathDaySects = ind.getDeathday().split("/");
				String[] birthDaySects = ind.getBirthday().split("/");
				
				if(Integer.parseInt(birthDaySects[0]) > Integer.parseInt(deathDaySects[0]))
				{
					out = out + "ERROR: US03 conflict with Individual " + ind.getName() + " death year (" + ind.getDeathday() + ") before birth year (" + ind.getBirthday() + ") \n";
				}
				else if(Integer.parseInt(birthDaySects[1]) > Integer.parseInt(deathDaySects[1]))
				{
					out = out + "ERROR: US03 conflict with Individual " + ind.getName() + " death year/month (" + ind.getDeathday() + ") before birth year/month (" + ind.getBirthday() + ") \n";
				}
				else if(Integer.parseInt(birthDaySects[2]) > Integer.parseInt(deathDaySects[2]))
				{
					out = out + "ERROR: US03 conflict with Individual " + ind.getName() + " death year/month/day (" + ind.getDeathday() + ") before birth year/month/day (" + ind.getBirthday() + ") \n";
				}
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }
	
	public String marriageBeforeDevorce(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		for(Family fam : families.values())
		{
			if(fam.getDivDate() != null)
			{
				String[] marrDaySects = fam.getMarrDate().split("/");
				String[] divDaySects = fam.getDivDate().split("/");
				
				if(Integer.parseInt(marrDaySects[0]) > Integer.parseInt(divDaySects[0]))
				{
					out = out + "ERROR: US04 conflict with Family " + fam.getID() + " divorce year (" + fam.getDivDate() + ") before marriage year (" + fam.getMarrDate() + ") \n";
				}
				else if(Integer.parseInt(marrDaySects[1]) > Integer.parseInt(divDaySects[1]))
				{
					out = out + "ERROR: US04 conflict with Family " + fam.getID() + " divorce year/month (" + fam.getDivDate() + ") before marriage year/month (" + fam.getMarrDate() + ") \n";
				}
				else if(Integer.parseInt(marrDaySects[2]) > Integer.parseInt(divDaySects[2]))
				{
					out = out + "ERROR: US04 conflict with Family " + fam.getID() + " divorce year/month/day (" + fam.getDivDate() + ") before marriage year/month/day (" + fam.getMarrDate() + ") \n";
				}
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }
	
	public String birthBeforeParentDeath(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		for(Family fam : families.values())
		{
			for(String chil : fam.getChildren())
			{
				Individual child = null;
				Individual mother = null;
				Individual father = null;
				
				for(Individual ind : individuals.values())
				{
					if(ind.getID().equals(chil))
					{
						child = ind;
					}
					if(ind.getID().equals(fam.getHusbandID()))
					{
						mother = ind;
					}
					if(ind.getID().equals(fam.getWifeID()))
					{
						father = ind;
					}
				}
				
				if(mother.getDeathday() != null || father.getDeathday() != null)
				{
					String[] childBirthDaySects = child.getBirthday().split("/");
					
					if(mother.getDeathday() != null)
					{
						String[] motherDeathDaySects = mother.getDeathday().split("/");
						if(Integer.parseInt(childBirthDaySects[0]) > Integer.parseInt(motherDeathDaySects[0]))
						{
							out = out + "ERROR: US09 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") after mothers death year (" + mother.getDeathday() + ") \n";
						}
						else if(Integer.parseInt(childBirthDaySects[1]) > Integer.parseInt(motherDeathDaySects[1]))
						{
							out = out + "ERROR: US09 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") after mothers death year/month (" + mother.getDeathday() + ") \n";
						}
						else if(Integer.parseInt(childBirthDaySects[2]) > Integer.parseInt(motherDeathDaySects[2]))
						{
							out = out + "ERROR: US09 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") after mothers death year/month/day (" + mother.getDeathday() + ") \n";
						}
					}
					if(father.getDeathday() != null)
					{
						String[] fatherDeathDaySects = father.getDeathday().split("/");
						if(Integer.parseInt(childBirthDaySects[0]) > Integer.parseInt(fatherDeathDaySects[0]))
						{
							out = out + "ERROR: US09 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") after fathers death year (" + father.getDeathday() + ") \n";
						}
						else if(Integer.parseInt(childBirthDaySects[1]) > Integer.parseInt(fatherDeathDaySects[1]))
						{
							out = out + "ERROR: US09 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") after fathers death year/month (" + father.getDeathday() + ") \n";
						}
						else if(Integer.parseInt(childBirthDaySects[2]) > Integer.parseInt(fatherDeathDaySects[2]))
						{
							out = out + "ERROR: US09 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") after fathers death year/month/day (" + father.getDeathday() + ") \n";
						}
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
	
	public String marriageAfterFourteen(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		for(Family fam : families.values())
		{
			Individual mother = null;
			Individual father = null;
			
			for(Individual ind : individuals.values())
			{
				if(ind.getID().equals(fam.getHusbandID()))
				{
					mother = ind;
				}
				if(ind.getID().equals(fam.getWifeID()))
				{
					father = ind;
				}
			}
			
			String[] marrDaySects = fam.getMarrDate().split("/");
			String[] motherBirthDaySects = mother.getBirthday().split("/");
			String[] fatherBirthDaySects = father.getBirthday().split("/");
			
			int motherAgeAtMarriage = Integer.parseInt(marrDaySects[0]) - Integer.parseInt(motherBirthDaySects[0]) - 1;
			int fatherAgeAtMarriage = Integer.parseInt(marrDaySects[0]) - Integer.parseInt(fatherBirthDaySects[0]) - 1;
			
			if(Integer.parseInt(marrDaySects[1]) > Integer.parseInt(motherBirthDaySects[1]))
			{
				if(Integer.parseInt(marrDaySects[2]) > Integer.parseInt(motherBirthDaySects[2]))
				{
					motherAgeAtMarriage++;
				}
			}
			
			if(Integer.parseInt(marrDaySects[1]) > Integer.parseInt(fatherBirthDaySects[1]))
			{
				if(Integer.parseInt(marrDaySects[2]) > Integer.parseInt(fatherBirthDaySects[2]))
				{
					fatherAgeAtMarriage++;
				}
			}
			
			if(motherAgeAtMarriage <= 14)
			{
				out = out + "ERROR: US10 conflict with Family " + fam.getID() + ", Mother got married at age " + motherAgeAtMarriage + " \n";
			}
			
			if(fatherAgeAtMarriage <= 14)
			{
				out = out + "ERROR: US10 conflict with Family " + fam.getID() + ", Father got married at age " + fatherAgeAtMarriage + " \n";
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }
	
    public long getAge(Individual ind)
    {
      String indBrithday = ind.getBirthday();
      int secondSlashIndex = indBrithday.indexOf("/", 5);
      int year = Integer.parseInt(indBrithday.substring(0,4));
      int month = Integer.parseInt(indBrithday.substring(5,secondSlashIndex));
      int day = Integer.parseInt(indBrithday.substring(secondSlashIndex+1));
      LocalDate start = LocalDate.of(year, month, day);
      LocalDate now = LocalDate.now();
      return ChronoUnit.YEARS.between(start,now);
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