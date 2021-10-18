/*
 *  Agile Methods for Software Development - Project 2
 *  Author: Alexander Kulpin
 *  Date: 09/27/2021
 */

import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner; 
import java.nio.charset.StandardCharsets;

public class main_project
{	
	public static ArrayList<Individual> individuals = new ArrayList<Individual>();
	public static ArrayList<Family> families = new ArrayList<Family>();

	private static String readFile(String filePath) 
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

    public static void main(String[] args) throws Exception
	{
		int c = 0;
		String valid = "N";
		
		Scanner myObj = new Scanner(System.in);
		System.out.print("Enter Path of GED file: ");
		
		String pathName = myObj.nextLine();
				
		String strOut = readFile(pathName);
		
		String[] lines = strOut.split("\r\n");
		
		for(int i = 0; i < lines.length; i++)
		{
			System.out.println("--> " + lines[i]); 
			
			for(int j = 0; j < lines[i].length(); j++)
			{
				char ch = lines[i].charAt(j);
				if(ch == ' ') { c++; } 
			}
			
			if(c > 1)
			{
				String[] sections = lines[i].split(" ", 3);
				
				if(sections[2].equals("INDI") || sections[2].equals("FAM"))
				{
					valid = "Y";
					System.out.println("<-- " + sections[0] + "|" + sections[2] + "|" + valid + "|" +  sections[1]);
					
					if(sections[2].equals("INDI"))
					{
						String[] sectionsName = lines[i + 1].split(" ", 3);
						String[] sectionsGender = lines[i + 5].split(" ", 3);
						String[] sectionsBirthday = lines[i + 7].split(" ", 3);
					
						Individual tempIndividual = new Individual(sections[1], sectionsName[2], sectionsGender[2], sectionsBirthday[2]);
						
						// US07: Death should be less than 150 years after birth for dead people, 
						//       and current date should be less than 150 years after birth for all living people
						if((Integer.parseInt((sectionsBirthday[2].split(" ", 3))[2])) + 150 < Calendar.getInstance().get(Calendar.YEAR))
						{
							if((lines[i + 8].split(" ", 3))[2].equals("Y"))
							{
								System.out.print("      ERROR: THIS DEATH WAS OVER 150 YEARS");
							}
							else
							{
								System.out.print("      ERROR: THIS PERSON CANT BE OVER 150 YEARS OLD");
							}
						}
						else if((Integer.parseInt((sectionsBirthday[2].split(" ", 3))[2])) + 150 == Calendar.getInstance().get(Calendar.YEAR))
						{
							if((monthToInt((sectionsBirthday[2].split(" ", 3))[1])) < Calendar.getInstance().get(Calendar.MONTH))
							{
								if((lines[i + 8].split(" ", 3))[2].equals("Y"))
								{
									System.out.print("      ERROR: THIS DEATH WAS OVER 150 YEARS");
								}
								else
								{
									System.out.print("      ERROR: THIS PERSON CANT BE OVER 150 YEARS OLD");
								}
							}
							else if((monthToInt((sectionsBirthday[2].split(" ", 3))[1])) == Calendar.getInstance().get(Calendar.MONTH))
							{
								if((Integer.parseInt((sectionsBirthday[2].split(" ", 3))[0])) <= Calendar.getInstance().get(Calendar.DATE))
								{
									if((lines[i + 8].split(" ", 3))[2].equals("Y"))
									{
										System.out.print("      ERROR: THIS DEATH WAS OVER 150 YEARS");
									}
									else
									{
										System.out.print("      ERROR: THIS PERSON CANT BE OVER 150 YEARS OLD");
									}
								}
							}
						}
					
						individuals.add(tempIndividual);
					}
					
					if(sections[2].equals("FAM"))
					{
						String[] sectionsHusbandsID = lines[i + 1].split(" ", 3);
						String[] sectionsWifesID = lines[i + 2].split(" ", 3);
						ArrayList<String> children = new ArrayList<String>();
						boolean continueLoop = true;
						int count = 0;
						while(continueLoop)
						{
							String[] isChild = lines[i + 3 + count].split(" ", 3);
							if(isChild[1].equals("CHIL"))
							{
								children.add(isChild[2]);
								count++;
							}
							else
							{
								continueLoop = false;
							}
						}
						
						Family tempFamily = new Family(sections[1], sectionsHusbandsID[2], sectionsWifesID[2], children);
						
						// US06: Divorce can only occur before death of both spouses.
						for(int j = 0; j < lines.length - i; j++) 
						{
							String[] isDiv = lines[i + j].split(" ", 3);
							if(isDiv[1].equals("DIV"))
							{
								String[] divDate = lines[i + j + 1].split(" ", 3);
								String[] divDateSec = divDate[2].split(" ", 3);
								String[] husbandDateSec = tempFamily.getHusbandBirthDay(sectionsHusbandsID[2]).split(" ", 3);
								String[] wifeDateSec = tempFamily.getWifeBirthDay(sectionsWifesID[2]).split(" ", 3);
								if(!isDateAhead(husbandDateSec, divDateSec))
								{
									System.out.print("      ERROR: ONE OR BOTH OF THE SPOUCES DIED BEFORE DIVORCE");
								}
								else if(!isDateAhead(wifeDateSec, divDateSec))
								{
									System.out.print("      ERROR: ONE OR BOTH OF THE SPOUCES DIED BEFORE DIVORCE");
								}
								break;
							}
							else if(isDiv[1].equals("MARR"))
							{
								break;
							}
							else if(isDiv[1].equals("TRLR"))
							{
								break;
							}
						}
					
						families.add(tempFamily);
					}
				}
				else
				{
					if(Tag.checkSupport(sections[1]))
					{
						valid = "Y";
					}
					System.out.println("<-- " + sections[0] + "|" + sections[1] + "|" + valid + "|" + sections[2]);
				}
			}
		
			else
			{
				String[] sections = lines[i].split(" ", 2);
				
				if(Tag.checkSupport(sections[1]))
				{
					valid = "Y";
				}
				
				System.out.println("<-- " + sections[0] + "|" + sections[1] + "|" + valid);
			}
			
			valid = "N";
			c = 0;
			
			
		}
		
		System.out.println("************************************");
		System.out.println("Individuals");
		
		for(int j = 0; j < individuals.size(); j++)
		{
			System.out.println(individuals.get(j).toTestString());
		}
		
		System.out.println("************************************");
		System.out.println("Families");
		
		for(int j = 0; j < families.size(); j++)
		{
			System.out.println(families.get(j).toTestString());
		}
	}
	
	enum Tag 
	{
		INDI("iid"),
		NAME("string"),
		SEX("M/F"),
		BIRT("none"),
		DEAT("none"),
		FAMC("fid"),
		FAMS("fid"),
		FAM("fid"),
		MARR("none"),
		HUSB("iid"),
		WIFE("iid"),
		CHIL("iid"),
		DIV("none"),
		DATE("day/month/year"),
		HEAD("none"),
		TRLR("none"),
		NOTE("string");
		
		private static final Tag[] copyOfValues = values();
		
		public final String label;

		private Tag(String label) 
		{
			this.label = label;
		}
    
		public static boolean checkSupport(String name) {
			for (Tag value : copyOfValues) {
				if (value.name().equals(name)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static class Individual
	{
		private String ID;
		private String name;
		private String gender;
		private String birthday;
		
		public Individual(String ID, String name, String gender, String birthday)
		{
			this.ID = ID;
			this.name = name;
			this.gender = gender;
			this.birthday = birthday;
		}
		
		String getID()
		{
			return this.ID;
		}
		
		String getName()
		{
			return this.name;
		}
		
		String getGender()
		{
			return this.gender;
		}
		
		String getBirthday()
		{
			return this.birthday;
		}
		
		String toTestString()
		{
			return "ID: " + this.ID + " Name: " + this.name; // + " Gender: " + this.gender + " Birthday: " + this.birthday;
		}
	}
	
	public static class Family
	{
		private String ID;
		private String husbandID;
		private String wifeID;
		private ArrayList<String> children;
		
		public Family(String ID, String husbandID, String wifeID, ArrayList<String> children)
		{
			this.ID = ID;
			this.husbandID = husbandID;
			this.wifeID = wifeID;
			this.children = children;
		}
		
		String getID()
		{
			return this.ID;
		}
		
		String getHusbandID()
		{
			return this.husbandID;
		}
		
		String getHusbandName()
		{
			for(int j = 0; j < individuals.size(); j++)
			{
				if(this.husbandID.equals(individuals.get(j).getID()))
				{
					return individuals.get(j).getName();
				}
			}
			return "ERROR";
		}
		
		String getHusbandBirthDay(String inputHusbandID)
		{
			for(int j = 0; j < individuals.size(); j++)
			{
				if(inputHusbandID.equals(individuals.get(j).getID()))
				{
					return individuals.get(j).getBirthday();
				}
			}
			return "ERROR";
		}
		
		String getWifeID()
		{
			return this.wifeID;
		}
		
		String getWifeName()
		{
			for(int j = 0; j < individuals.size(); j++)
			{
				if(this.wifeID.equals(individuals.get(j).getID()))
				{
					return individuals.get(j).getName();
				}
			}
			return "ERROR";
		}
		
		String getWifeBirthDay(String inputWifeID)
		{
			for(int j = 0; j < individuals.size(); j++)
			{
				if(inputWifeID.equals(individuals.get(j).getID()))
				{
					return individuals.get(j).getBirthday();
				}
			}
			return "ERROR";
		}
		
		ArrayList<String> getChildren()
		{
			return this.children;
		}
		
		String toTestString()
		{
			return "ID: " + this.ID + " Husband ID: " + this.husbandID + " Husband Name: " + getHusbandName() + " Wife ID: " + this.wifeID + " Wife Name: " + getWifeName(); //+ "-" + this.children;
		}
	}
	
	// Test if dateToBeFirst is in fact a date that comes before the dateToBeAfter
	public static boolean isDateAhead(String[] dateToBeFirst, String[] dateToBeAfter)
	{
		if(Integer.parseInt(dateToBeFirst[2]) <= Integer.parseInt(dateToBeAfter[2]))
		{
			if(monthToInt(dateToBeFirst[1]) <= monthToInt(dateToBeAfter[1]))
			{
				if(Integer.parseInt(dateToBeFirst[0]) < Integer.parseInt(dateToBeAfter[0]))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static int monthToInt(String month)
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
}

