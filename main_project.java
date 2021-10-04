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

    public static void main(String[] args) throws Exception
	{
		int c = 0;
		String valid = "N";
		
		Scanner myObj = new Scanner(System.in);
		System.out.print("Enter Path of GED file: ");
		
		String pathName = myObj.nextLine();
		
		Path path = Paths.get(pathName);
		
		String strOut = Files.readString(path, StandardCharsets.ISO_8859_1);
		
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
		
		ArrayList<String> getChildren()
		{
			return this.children;
		}
		
		String toTestString()
		{
			return "ID: " + this.ID + " Husband ID: " + this.husbandID + " Husband Name: " + getHusbandName() + " Wife ID: " + this.wifeID + " Wife Name: " + getWifeName(); //+ "-" + this.children;
		}
	}
}

