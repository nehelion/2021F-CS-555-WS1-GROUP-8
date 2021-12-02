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

import javax.lang.model.type.NullType;

import java.nio.charset.StandardCharsets;

public class main_project
{	
	public static Map<String, Individual> individuals = new HashMap<String, Individual>();
	public static Map<String, Family> families = new HashMap<String, Family>();
	public static Utils utils = new Utils();
	public static String errorString = "\n\n";

	private static void storeIndivs(String[] lines) {
		boolean record = false;
		String birth = "";
		String sex = "";
		String name = "";
		String death = "";
		String id = "";
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("INDI") && !record) {
				record = true;
			} else if ((lines[i].contains("FAM") && record) || (lines[i].contains("INDI") && record)) {
				Individual indiv = new Individual(id, name, sex, birth, death);
				individuals.put(indiv.getID(), indiv);
			}

			if (lines[i].contains("FAM") && record) {
				record = false;
			}

			if (record) {
				if (lines[i].contains("NAME")) {
					String[] name_secs = lines[i].split(" ", 3);
					name = name_secs[2].replace("/", "");
				}

				if (lines[i].contains("BIRT")) {
					String[] date_secs = lines[i+1].split(" ", 3);
					String[] birth_secs = date_secs[2].split(" ");
					String birthday = birth_secs[2] + "/";
					birthday += utils.monthToInt(birth_secs[1]) + "/";
					birthday += birth_secs[0];
					birth = birthday;
				}

				if (lines[i].contains("INDI")) {
					String[] id_sec = lines[i].split(" ");
					id = id_sec[1];
				}

				if (lines[i].contains("SEX")) {
					String[] sex_sec = lines[i].split(" ");
					sex = sex_sec[2];
				}

				if (lines[i].contains("DEAT")) {
					String[] death_date_secs = lines[i+1].split(" ", 3);
					String[] death_secs = death_date_secs[2].split(" ");
					String deathday = death_secs[2] + "/";
					deathday += utils.monthToInt(death_secs[1]) + "/";
					deathday += death_secs[0];
					death = deathday;
				}
			}
			


		}
	}

	private static void storeFamilies(String[] lines) {
		boolean record = false;
		String id = "";
		String husband = "";
		String wife = "";
		List<String> children = new ArrayList<String>();
		String married = "";
		String divorced = "";
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("@ FAM") && !record) record = true;
			else if ((lines[i].contains("@ FAM") && record) || (i == lines.length - 1)) {
				
				
				List<String> storeChildren = new ArrayList<String>();
				for (String string : children) {
					storeChildren.add(string);
								}
				Family fam = new Family(id, husband, wife, storeChildren, married, divorced);
				Individual dad = individuals.get(husband);
				dad.addFams(id);
				Individual mom = individuals.get(wife);
				mom.addFams(id);
				families.put(fam.getID(), fam);	
				children.clear();		
			}
			if (record) {
				if (lines[i].contains("@ FAM")) {
					String[] id_secs = lines[i].split(" ");
					id = id_secs[1];
				}

				if (lines[i].contains("HUSB")) {
					String[] husb_secs = lines[i].split(" ");
					husband = husb_secs[2];
				}
	
				if (lines[i].contains("WIFE")) {
					String[] wife_secs = lines[i].split(" ");
					wife = wife_secs[2];
				}
	
				if (lines[i].contains("CHIL")) {
					String[] chil_sec = lines[i].split(" ");
					children.add(chil_sec[2]);
				}
	
				if (lines[i].contains("MARR")) {
					String[] marr_date_secs = lines[i+1].split(" ", 3);
					String[] marr_secs = marr_date_secs[2].split(" ");
					String marrDate = marr_secs[2] + "/";
					marrDate += utils.monthToInt(marr_secs[1]) + "/";
					marrDate += marr_secs[0];
					married = marrDate;
				}
	
				if (lines[i].contains("DIV")) {
					String[] div_date_secs = lines[i+1].split(" ", 3);
					String[] div_secs = div_date_secs[2].split(" ");
					String divDate = div_secs[2] + "/";
					divDate += utils.monthToInt(div_secs[1]) + "/";
					divDate += div_secs[0];
					divorced = divDate;
				}
				
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
		String strOut;
		strOut = utils.readFile(file);

		if (strOut == null) {
			System.out.println("File not found. Please try again with a valid path.");
			return;
		}

		String[] lines = strOut.split("\n");

		storeIndivs(lines);
		storeFamilies(lines);
		
		String US01 = utils.isDeathInFuture(individuals);
		if(!US01.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US01);
		}

		String US02 = utils.isDeathBeforeBirth(individuals);
		if(!US02.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US02);
		}
		
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
		
		String US08 = utils.birthBeforeParentMarriage(families,individuals);
		if(!US08.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US08);
		}
		
		String US09 = utils.birthBeforeParentDeath(families,individuals);
		if(!US09.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US09);
		}
	
		String US10 = utils.marriageAfterFourteen(families,individuals);
		if(!US10.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US10);
		}

		String US12 = utils.areParentsTooOld(families,individuals);
		if(!US12.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US12);
		}
		
		String US15 = utils.tooManyChildren(families,individuals);
		if(!US15.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US15);
		}
	
		String US16 = utils.maleLastName(families,individuals);
		if(!US16.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US16);
		}

		String US18 = utils.areSibilingsMarried(families,individuals);
		if(!US18.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US18);
		}
		
		String US19 = utils.noFirstCousinMarriage(families,individuals);
		if(!US19.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US19);
		}

		String US17 = utils.noMarriageToDecendents(families,individuals);
		if(!US17.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US17);
		}
		
		String US21 = utils.correctGenderRole(families,individuals);
		if(!US21.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US21);
		}

		String US22 = utils.uniqueFamById(families,individuals);
		if(!US22.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US22);
		}
		
		String US23 = utils.uniqueNamesandBirthdays(families,individuals);
		if(!US23.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US23);
		}

		String US24 = utils.uniqueFamsBySpouse(families,individuals);
		if(!US24.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US24);
		}

		String US11 = utils.isThereBigamy(families,individuals);
		if(!US11.equalsIgnoreCase("CORRECT"))
		{
			errorString = errorString.concat(US11);
		}

		printIndividuals();
		printFamilies();
		System.out.println(errorString);
	}
    public static void main(String[] args) throws Exception
	{
		Scanner myObj = new Scanner(System.in);
		System.out.print("Enter Path of GED file: ");
		
		String pathName = myObj.nextLine();				

		runGEDFile(pathName);
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

