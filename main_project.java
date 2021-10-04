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
import java.nio.charset.StandardCharsets;

public class main_project
{	
    public static void main(String[] args) throws Exception
	{
		int c = 0;
		String valid = "N";
		
		Path path = Paths.get("Kulpin_Family.ged");
		
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
}

