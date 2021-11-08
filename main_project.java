/*
 *  Agile Methods for Software Development - Project 2
 *  Author: Alexander Kulpin, Alex Johnson, Joe Basile
 *  Date: 09/27/2021
 */

import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner;

import javax.lang.model.type.NullType;

import java.nio.charset.StandardCharsets;

public class main_project
{	
	public static Map<String, Individual> individuals = new HashMap<String, Individual>();
	public static Map<String, Family> families = new HashMap<String, Family>();
	public static Utils utils = new Utils();
	public static String errorString = "";
	private static void storeIndivs(String[] lines) {
		for (int i = 0; i < lines.length; i++) {
			String deathday = null;

			String[] sections = lines[i].split(" ");
			if (sections.length > 2 && sections[2].equalsIgnoreCase("INDI")) {
				String[] name_secs = lines[i+1].split(" ", 3);
				String name = name_secs[2].replace("/", "");

				String[] sex_secs = lines[i+5].split(" ", 3);
				String sex = sex_secs[2];

				String[] date_secs = lines[i+7].split(" ", 3);
				String[] birth_secs = date_secs[2].split(" ");
				String birthday = birth_secs[2] + "/";
				birthday += utils.monthToInt(birth_secs[1]) + "/";
				birthday += birth_secs[0];
				if(lines[i+8].split(" ")[1].equalsIgnoreCase("DEAT"))
				{
					String[] death_date_secs = lines[i+9].split(" ", 3);
					String[] death_secs = death_date_secs[2].split(" ");
					deathday = death_secs[2] + "/";
					deathday += utils.monthToInt(death_secs[1]) + "/";
					deathday += death_secs[0];
				}
				

				Individual indiv = new Individual(sections[1], name, sex, birthday, deathday);
				individuals.put(indiv.getID(), indiv);
			}
		}
	}

	private static void storeFamilies(String[] lines) {
		boolean sameFam = false;
		for (int i = 0; i < lines.length; i++) {
			String[] sections = lines[i].split(" ");
			if (sections.length > 2 && sections[2].equalsIgnoreCase("FAM")) {
				String divDate = null;
				String marrDate = null;
				String[] husb_secs = lines[i+1].split(" ");
				String husb = husb_secs[2];

				String[] wife_secs = lines[i+2].split(" ");
				String wife = wife_secs[2];

				ArrayList<String> children = new ArrayList<>();
				int j = 3;
				while(lines[i+j].split(" ")[1].equalsIgnoreCase("CHIL")) {
					children.add(lines[i+j].split(" ")[2]);
					j++;
				}
				if(lines[i+j].split(" ")[1].equalsIgnoreCase("MARR"))
				{
					j++;
					String[] marr_date_secs = lines[i+j].split(" ", 3);
					String[] marr_secs = marr_date_secs[2].split(" ");
					marrDate = marr_secs[2] + "/";
					marrDate += utils.monthToInt(marr_secs[1]) + "/";
					marrDate += marr_secs[0];
					
					j++;
					j++;
				}

				if(lines[i+j].split(" ")[1].equalsIgnoreCase("DIV"))
				{
					j++;
					String[] div_date_secs = lines[i+j].split(" ", 3);
					String[] div_secs = div_date_secs[2].split(" ");
					divDate = div_secs[2] + "/";
					divDate += utils.monthToInt(div_secs[1]) + "/";
					divDate += div_secs[0];
					j++;
				}
				Family fam = new Family(sections[1], husb, wife, children, marrDate, divDate);
				Individual dad = individuals.get(husb);
				dad.addFams(sections[1]);
				Individual mom = individuals.get(wife);
				mom.addFams(sections[1]);
				
				families.put(fam.getID(), fam);
			}
		}
	}

	private static void printIndividuals() {
		System.out.print("\n****************************************\n                Individuals             \n****************************************\n");
		List<Individual> ar = new ArrayList<Individual>(individuals.values());
		for (int i = 0; i < ar.size(); i++) {
			Individual ind = ar.get(i);
			for(String id : ind.getFams())
			{
				System.out.print(id + " ");
			}
			System.out.println(ind.getID() + " | " + ind.getName());
		}
	}

	private static void printFamilies() {
		System.out.print("\n****************************************\n                 Families             \n****************************************\n");
		List<Family> ar = new ArrayList<Family>(families.values());
		for (int i = 0; i < ar.size(); i++) {
			Family fam = ar.get(i);
			String wife = individuals.get(fam.getWifeID()).getName();
			String husb = individuals.get(fam.getHusbandID()).getName();
			System.out.println(fam.getID() + " | " + husb + " | " + wife + " | " + fam.getChildren().toString());
		}
	}

	public static void runGEDFile(String file) throws Exception
	{
		int c = 0;
		String valid = "N";
		

		
		//String pathName = myObj.nextLine();				
		String pathName = file;
		String strOut = utils.readFile(pathName);
		String[] lines = strOut.split("\n");

		storeIndivs(lines);
		storeFamilies(lines);
		
		String US03 = utils.bornBeforeDead(individuals);
		if(!US03.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US03);
		}
		
		String US04 = utils.marriageBeforeDevorce(families,individuals);
		if(!US04.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US04);
		}

		String US05 = utils.isMarriageBeforeDeath(families,individuals);
		if(!US05.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US05);
		}

		String US06 = utils.isDivorceBeforeDeath(families,individuals);
		if(!US06.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US06);
		}

		String US12 = utils.areParentsTooOld(families,individuals);
		if(!US12.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US12);
		}

		String US18 = utils.areSibilingsMarried(families,individuals);
		if(!US18.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US18);
		}
		


		printIndividuals();
		printFamilies();
		System.out.println(errorString);
	}
    public static void main(String[] args) throws Exception
	{
		int c = 0;
		String valid = "N";
		
		Scanner myObj = new Scanner(System.in);
		System.out.print("Enter Path of GED file: ");
		
		String pathName = myObj.nextLine();				
		//String pathName = "resources/Kulpin_Family.ged";
		String strOut = utils.readFile(pathName);
		String[] lines = strOut.split("\n");

		storeIndivs(lines);
		storeFamilies(lines);

		String US05 = utils.isMarriageBeforeDeath(families,individuals);
		if(!US05.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US05);
		}

		String US06 = utils.isDivorceBeforeDeath(families,individuals);
		if(!US06.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US06);
		}

		String US12 = utils.areParentsTooOld(families,individuals);
		if(!US12.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US12);
		}
		

		printIndividuals();
		printFamilies();
		System.out.println(errorString);
		errorString = null;
		individuals.clear();
		families.clear();
		/*for(int i = 0; i < lines.length; i++)
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
								// String[] husbandDateSec = tempFamily.getHusbandBirthDay(sectionsHusbandsID[2]).split(" ", 3);
								// String[] wifeDateSec = tempFamily.getWifeBirthDay(sectionsWifesID[2]).split(" ", 3);
								// if(!isDateAhead(husbandDateSec, divDateSec))
								// {
								// 	System.out.print("      ERROR: ONE OR BOTH OF THE SPOUCES DIED BEFORE DIVORCE");
								// }
								// else if(!isDateAhead(wifeDateSec, divDateSec))
								// {
								// 	System.out.print("      ERROR: ONE OR BOTH OF THE SPOUCES DIED BEFORE DIVORCE");
								// }
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
			// System.out.println(individuals.get(j).toTestString());
		}
		
		System.out.println("************************************");
		System.out.println("Families");
		
		for(int j = 0; j < families.size(); j++)
		{
			// System.out.println(families.get(j).toTestString());
		}*/
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
	

	
	// Test if dateToBeFirst is in fact a date that comes before the dateToBeAfter
	// public static boolean isDateAhead(String[] dateToBeFirst, String[] dateToBeAfter)
	// {
	// 	if(Integer.parseInt(dateToBeFirst[2]) <= Integer.parseInt(dateToBeAfter[2]))
	// 	{
	// 		if(monthToInt(dateToBeFirst[1]) <= monthToInt(dateToBeAfter[1]))
	// 		{
	// 			if(Integer.parseInt(dateToBeFirst[0]) < Integer.parseInt(dateToBeAfter[0]))
	// 			{
	// 				return false;
	// 			}
	// 		}
	// 	}
		
	// 	return true;
	// }
}

