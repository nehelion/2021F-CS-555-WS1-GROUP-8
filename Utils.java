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

	// US01
    public String isDeathInFuture(Map<String, Individual> individuals) throws Exception 
	{
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
          }
       }
       if (out.length() == 0) out.concat("Correct");

       return out;
    }

	// US02
    public String isDeathBeforeBirth(Map<String, Individual> individuals) throws Exception 
	{
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
				out += "ERROR: US02 conflict with " + indiv.getID() + "\n";
			}
			if (out.length() == 0) out.concat("Correct");
		}
		return out;
    }
	
	// US03
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
	
	// US04
	public String marriageBeforeDevorce(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		for(Family fam : families.values())
		{
			if(fam.getDivDate() != null)
			{
				String[] marrDaySects = fam.getMarrDate().split("/");
				String[] divDaySects = fam.getDivDate().split("/");

            if (divDaySects.length == 1) {
               continue;
            }
				
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
	
	// US05
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
            if (marrDate.equals("")) continue;
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
                  out = out.concat("ERROR: US05 conflict with ").concat(wife_id).concat(".\n");
               }
            }

            if(husb_death != null)
            {
               //Date wDeath = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH)
               Date hDeath = tf.parse(husb_death);
               
               if(hDeath.before(mDate))
               {
                  out = out.concat("ERROR: US05 conflict with ").concat(husb_id).concat(".\n");
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

	// US06
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
            if (divDate.equals("")) {
               continue;
            }
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
                  out = out.concat("ERROR: US06 conflict with ").concat(wife_id).concat(".\n");
               }
            }

            if(husb_death != null)
            {
               //Date wDeath = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH)
               Date hDeath = tf.parse(husb_death);
               
               if(hDeath.before(dDate))
               {
                  out = out.concat("ERROR: US06 conflict with ").concat(husb_id).concat(".\n");
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
	
	// US07
	public String overOneFifty(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		for(Individual ind : individuals.values())
		{
			String[] indBirthDaySects = ind.getBirthday().split("/");
			
			if(ind.getDeathday() != null)
			{
				String[] indDeathDaySects = ind.getDeathday().split("/");
				
				if(Integer.parseInt(indBirthDaySects[0]) + 150 < Integer.parseInt(indDeathDaySects[0]))
				{
					out = out + "ERROR: US07 conflict with Individual " + ind.getID() + ", was born (" + ind.getBirthday() + ") and was over 150 years when died (" + ind.getDeathday() + ") \n";
				}
				else if(Integer.parseInt(indBirthDaySects[1]) < Integer.parseInt(indDeathDaySects[1]))
				{
					out = out + "ERROR: US07 conflict with Individual " + ind.getID() + ", was born (" + ind.getBirthday() + ") and was over 150 years when died (" + ind.getDeathday() + ") \n";
				}
				else if(Integer.parseInt(indBirthDaySects[2]) < Integer.parseInt(indDeathDaySects[2]))
				{
					out = out + "ERROR: US07 conflict with Individual " + ind.getID() + ", was born (" + ind.getBirthday() + ") and was over 150 years when died (" + ind.getDeathday() + ") \n";
				}
			}
			else
			{
				if(Integer.parseInt(indBirthDaySects[0]) + 150 < Calendar.getInstance().get(Calendar.YEAR))
				{
					out = out + "ERROR: US07 conflict with Individual " + ind.getID() + ", was born (" + ind.getBirthday() + ") and is over 150 years today \n";
				}
				else if(Integer.parseInt(indBirthDaySects[1]) < Calendar.getInstance().get(Calendar.MONTH))
				{
					out = out + "ERROR: US07 conflict with Individual " + ind.getID() + ", was born (" + ind.getBirthday() + ") and is over 150 years today \n";
				}
				else if(Integer.parseInt(indBirthDaySects[2]) < Calendar.getInstance().get(Calendar.DATE))
				{
					out = out + "ERROR: US07 conflict with Individual " + ind.getID() + ", was born (" + ind.getBirthday() + ") and is over 150 years today \n";
				}
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }
	
	// US08
	public String birthBeforeParentMarriage(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
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
						father = ind;
					}
					if(ind.getID().equals(fam.getWifeID()))
					{
						mother = ind;
					}
				}
				
				String[] childBirthDaySects = child.getBirthday().split("/");
				String[] marriageDaySects = fam.getMarrDate().split("/");
					
				if(Integer.parseInt(childBirthDaySects[0]) > Integer.parseInt(marriageDaySects[0]))
				{
					out = out + "ERROR: US08 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") before marriage year (" + fam.getMarrDate() + ") \n";
				}
				else if(Integer.parseInt(childBirthDaySects[1]) > Integer.parseInt(marriageDaySects[1]))
				{
					out = out + "ERROR: US08 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") before marriage year/month (" + fam.getMarrDate() + ") \n";
				}
				else if(Integer.parseInt(childBirthDaySects[2]) > Integer.parseInt(marriageDaySects[2]))
				{
					out = out + "ERROR: US08 conflict with Family " + fam.getID() + ", Child " + child.getName() + " was born (" + child.getBirthday() + ") before marriage year/month/day (" + fam.getMarrDate() + ") \n";
				}
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }
	
	// US09
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
						father = ind;
					}
					if(ind.getID().equals(fam.getWifeID()))
					{
						mother = ind;
					}
				}
				
				if((mother.getDeathday() != null || father.getDeathday() != null))
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
	
	// US10
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
					father = ind;
				}
				if(ind.getID().equals(fam.getWifeID()))
				{
					mother = ind;
				}
			}
			
			String[] marrDaySects = fam.getMarrDate().split("/");
         if (fam.getMarrDate().equals("")) continue;
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
	
    // US11
    public String isThereBigamy(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      for(Family fam : families.values())
      {
         String wife_id = fam.getWifeID();
			String husb_id = fam.getHusbandID();
			Individual temp_wife = individuals.get(wife_id);
			Individual temp_husb = individuals.get(husb_id);
         String divDate = fam.getDivDate();
         String marDate = fam.getMarrDate();
         String fID = fam.getID();
         SimpleDateFormat tf = new SimpleDateFormat("yyyy/MM/dd");
         Date mDate = tf.parse(marDate);
         Date dDate = null;
         if(!divDate.equalsIgnoreCase(""))
         {
            dDate = tf.parse(divDate);
         }

         for(Family fam2 : families.values())
         {
            if(!fID.equalsIgnoreCase(fam2.getID()))
            {
               String wife_id2 = fam2.getWifeID();
			      String husb_id2 = fam2.getHusbandID();
			      Individual temp_wife2 = individuals.get(wife_id2);
			      Individual temp_husb2 = individuals.get(husb_id2);
               String divDate2 = fam2.getDivDate();
               String marDate2 = fam2.getMarrDate();
               String fID2 = fam2.getID();
               Date mDate2 = tf.parse(marDate2);
               Date dDate2 = null;
               int spot = 0;
               if(wife_id.equalsIgnoreCase(wife_id2)){
                  spot = 1;
               }
               else if(husb_id.equalsIgnoreCase(husb_id2))
               {
                  spot = 2;
               }
               else
               {
                  spot = 0;
               }

               if(!divDate2.equalsIgnoreCase(""))
               {
                  dDate2 = tf.parse(divDate2);
               }

               if(spot != 0){

                  
                  if(dDate == null && dDate2 != null)
                  {
                     if(mDate.before(dDate2))
                     {
                        if(spot == 1)
                        {
                           out += "ERROR US11: There is bigamy with spouse " + wife_id + " " + temp_wife2.getName() + " in families " + fID + " and " + fID2 +".\n";
                        }
                        else{
                           out += "ERROR US11: There is bigamy with spouse " + husb_id + " " + temp_husb2.getName() + " in families " + fID + " and " + fID2 +".\n";

                        }
                     }  
                  }
                  else if(dDate != null && dDate2 == null)
                  {
                     if(mDate2.before(dDate))
                     {
                        if(spot == 1)
                        {
                           out += "ERROR US11: There is bigamy with spouse " + wife_id + " " + temp_wife2.getName() + " in families " + fID + " and " + fID2 +".\n";
                        }
                        else{
                           out += "ERROR US11: There is bigamy with spouse " + husb_id + " " + temp_husb2.getName() + " in families " + fID + " and " + fID2 +".\n";

                        }
                     }
                  }
                  else
                  {
                     if(spot == 1)
                     {
                        out += "ERROR US11: There is bigamy with spouse " + wife_id + " " + temp_wife2.getName() + " in families " + fID + " and " + fID2 +".\n";
                     }
                     else{
                        out += "ERROR US11: There is bigamy with spouse " + husb_id + " " + temp_husb2.getName() + " in families " + fID + " and " + fID2 +".\n";

                     }
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

	// US12
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
	
	// US13
	// If the difference between the youngest and oldest is more than 25 years. Mark as anomoly
	public String siblingSpacing(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
	{
		String out = "";
		for (Family fam : families.values()) {
			List<String> children = fam.getChildren();
			if (children.size() < 2) {
				continue;
			}
			ArrayList<Integer> children_ages = new ArrayList<>();
			for (String id : children) {
				Individual child = individuals.get(id);
				String birth = child.getBirthday();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH);
				Date birthdate = formatter.parse(birth);
				Instant instant = birthdate.toInstant();
				ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
				LocalDate givenDate = zone.toLocalDate();
				Period period = Period.between(givenDate, LocalDate.now());
				int dif = period.getYears();
				children_ages.add(dif);
			}
			Collections.sort(children_ages);
			if ((children_ages.get(0) - children_ages.get(children_ages.size() - 1)) > 25) {
				out = out.concat("ERROR: US13 conflict with siblings in family " + fam.getID() + ", has children with a age difference of more then 25 years.\n");
			}
		}

		if (out.length() == 0) {
			out = "Correct";
		}

		return out;
	}
	
	
	// US14
	public String multipleBirths(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
	{
		String out = "";
		Map<String, Integer> vals = new HashMap<>();
		for (Individual indiv : individuals.values())
		{
			ArrayList<String> fam_id = indiv.getFams();
			if(fam_id.size() == 0) {
				continue;
			}
			Family fam = families.get(fam_id.get(0));
			String mom = fam.getWifeID();
			if (vals.containsKey(mom)) {
				int t = vals.get(mom);
				vals.replace(mom, t + 1);
			} else {
				vals.put(mom, 1);
			}
		}

		boolean more = false;
		for (String i : vals.keySet()) {
			if (vals.get(i) > 5) {
				out = out.concat("ERROR: US14 conflict with mother " + i + ", has more than 5 children.\n");
			}
		}

		if (out.length() == 0) {
			out = "Correct";
		}

		return out;
	}
	
	// US15
	public String tooManyChildren(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		for(Family fam : families.values())
		{
			if(fam.getChildren().size() > 15)
			{
				out = out.concat("ERROR: US15 conflict with Family " + fam.getID() + ", has more than 15 children.\n");
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }
	
	// US16
	public String maleLastName(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		for(Family fam : families.values())
		{
			if(fam.getDivDate() != null)
			{
				Individual mother = null;
				Individual father = null;
				
				for(Individual ind : individuals.values())
				{
					if(ind.getID().equals(fam.getHusbandID()))
					{
						father = ind;
					}
					if(ind.getID().equals(fam.getWifeID()))
					{
						mother = ind;
					}
				}
				
				String[] fatherSplit = father.getName().split(" ");
				String[] motherSplit = mother.getName().split(" ");
				
				if(!motherSplit[1].equals(fatherSplit[1]))
				{
					out = out + "ERROR: US16 conflict with Mother in Family " + fam.getID() + ".  " + father.getName() + " and " + mother.getName() + " don't have the same last name. \n";
				}
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }
	
	// US17
	public String noMarriageToDecendents(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      for(Family fam : families.values())
      {

         if(!fam.getChildren().isEmpty())
         {
         
            Individual mom = individuals.get(fam.getWifeID());
            Individual dad = individuals.get(fam.getHusbandID());
            List<Individual> children = new ArrayList<>();
            for (String child : fam.getChildren()) {
               children.add(individuals.get(child));
            }
            for (Individual c : children) {

               for (String id : c.getFams()) {

                  if(families.get(id).getHusbandID() != null && families.get(id).getWifeID() != null)
                  {
                     if(mom.getID().equals(families.get(id).getWifeID()))
                     {
                        out += "ERROR: US17 conflict with Family " + id + " " + mom.getID() + " is married to decendent " + c.getID() + ".\n";
                     }
                     if(dad.getID().equals(families.get(id).getHusbandID()))
                     {
                        out += "ERROR: US17 conflict with Family " + id + " " + dad.getID() + " is married to decendent " + c.getID() + ".\n";
                     }
                     // if(!families.get(id).getChildren().isEmpty())
                     // {
                     //    for (Individual individual : children) {
                           
                     //    }
                     // }
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
	
	// US18
    public String areSibilingsMarried(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      for(Family fam : families.values())
      {
         String famId = fam.getID();
         if(fam.getChildren().size() > 1)
         {
            List<String> children = fam.getChildren();
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
	
	// US19
	public String noFirstCousinMarriage(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		List<String> grandChildren = new ArrayList<String>();
		
		for(Family fam : families.values())
		{
			if(fam.getChildren().size() > 1)
			{
				List<String> children = fam.getChildren();
				
				for(Family fam_second : families.values())
				{
					if(children.contains(fam_second.getHusbandID()) || children.contains(fam_second.getWifeID()))
					{
						for(String chil : fam_second.getChildren())
						{
							grandChildren.add(chil);
						}
					}
				}
			}
        }    
        for(Family tempFam : families.values())
        {
            if(grandChildren.contains(tempFam.getHusbandID()) && grandChildren.contains(tempFam.getWifeID()))
            {
                out = out + "ERROR: US19 conflict with Family " + tempFam.getID() + ".  " 
						  + individuals.get(tempFam.getWifeID()).getName() + tempFam.getWifeID() + " and " 
						  + individuals.get(tempFam.getHusbandID()).getName() + tempFam.getHusbandID() + " are first cousins and married. \n";
            }
        }
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }	
	
	// US20
	public String noUncleAuntMarryingNephew(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		List<String> uncleAunts = new ArrayList<String>();
		
		for(Family fam : families.values())
		{
			Individual mother = null;
			Individual father = null;
			List<String> children = fam.getChildren();
			
			for(Individual ind : individuals.values())
			{
				if(ind.getID().equals(fam.getHusbandID()))
				{
					father = ind;
				}
				if(ind.getID().equals(fam.getWifeID()))
				{
					mother = ind;
				}
			}
			
			for(Family fam_second : families.values())
			{
				if(fam_second.getChildren().contains(father.getID()) || fam_second.getChildren().contains(mother.getID()))
				{
					for(String chil : fam_second.getChildren())
					{
						uncleAunts.add(chil);
					}
				}
			}
			
			for(Family tempFam : families.values())
			{
				if((uncleAunts.contains(tempFam.getHusbandID()) && children.contains(tempFam.getWifeID())) || 
				   (uncleAunts.contains(tempFam.getWifeID()) && children.contains(tempFam.getHusbandID())))
				{
					out = out + "ERROR: US20 conflict with Family " + tempFam.getID() + ".  " 
							+ individuals.get(tempFam.getWifeID()).getName() + tempFam.getWifeID() + " and " 
							+ individuals.get(tempFam.getHusbandID()).getName() + tempFam.getHusbandID() + " are Uncle/Aunt and Neice/Nephew and married. \n";
				}
			}
		}	
			
       
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }	

	// US21
	public String correctGenderRole(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
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
					father = ind;
				}
				if(ind.getID().equals(fam.getWifeID()))
				{
					mother = ind;
				}
			}
			
			if(!mother.getGender().equals("F"))
			{
				out = out + "ERROR: US21 conflict with Family " + fam.getID() + ", Mother not Female \n";
			}
			
			System.out.println(father.getGender());
			
			if(!father.getGender().equals("M"))
			{
				out = out + "ERROR: US21 conflict with Family " + fam.getID() + ", Father not Male \n";
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }
	
	// US22
	public String uniqueFamById(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
		String out = "";
		int count = 0;
		Family[] family_list = new Family[families.size()];
		
		for(Family fam : families.values())
		{
			family_list[count] = fam;
			count++;
		}
		for(int i = 0; i < family_list.length; i++)
		{
			for(int j = i + 1; j < family_list.length; j++)
			{
				if(family_list[i].getID().equals(family_list[j].getID()))
				{
					out = out + "ERROR: US22 conflict with Family " + family_list[i].getID() + ", not unique \n";
				}
			}
		}
		if(out.length() == 0)
		{
			out.concat("Correct");
		}
		return out;
    }

	// US23
    public String uniqueNamesandBirthdays(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out = "";
      for (Individual indi : individuals.values()) {
         String id = indi.getID();
         String birthday = indi.getBirthday();
         String name = indi.getName();
         for (Individual person2 : individuals.values()) {
            if(!id.equals(person2.getID()))
            {
               if(birthday.equals(person2.getBirthday()) && name.equals(person2.getName()))
               {
                  out += "ERROR: US23 conflict with Person " + id +  " has same name and birthday as person " + person2.getID() + ".\n";
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

	// US24
    public String uniqueFamsBySpouse(Map<String, Family> families, Map<String, Individual> individuals) throws Exception
    {
      String out ="";
      for (Family fam_Main : families.values()) {
         String husb_name = individuals.get(fam_Main.getHusbandID()).getName();
         String wife_name = individuals.get(fam_Main.getWifeID()).getName();
         String mDate = fam_Main.getMarrDate();
         for (Family fam2 : families.values()) {
            String hName2 = individuals.get(fam2.getHusbandID()).getName();
            String wName2 = individuals.get(fam2.getWifeID()).getName();
            String mDate2 = fam2.getMarrDate();
            if(!fam2.getID().equalsIgnoreCase(fam_Main.getID()))
            {
               if(hName2.equalsIgnoreCase(husb_name) && mDate.equalsIgnoreCase(mDate2))
               {
                  out += "ERRROR WITH US24: Families with IDs " + fam2.getID() + " and " + fam_Main.getID() + " have same spouse and marriage date being " + husb_name + " on " + mDate + ".\n";
               }
               if(wName2.equalsIgnoreCase(wife_name) && mDate.equalsIgnoreCase(mDate2))
               {
                  out += "ERRROR WITH US24: Families with IDs " + fam2.getID() + " and " + fam_Main.getID() + " have same spouse and marriage date being " + wife_name + " on " + mDate + ".\n";
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
            return null;
        }
        return contentBuilder.toString();
    }
}