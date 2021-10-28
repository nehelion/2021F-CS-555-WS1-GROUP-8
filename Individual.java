import java.io.*;
import java.util.ArrayList;

public class Individual
{
    private String ID;
    private String name;
    private String gender;
    private String birthday;
    private String deathday;
    private ArrayList<String> famIds = new ArrayList<>();
    
    public Individual(String ID, String name, String gender, String birthday, String deathday)
    {
        this.ID = ID;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.deathday = deathday;
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

    String getDeathday()
    {
        return this.deathday;
    }

    void addFams(String id)
    {
        famIds.add(id);
    }

    ArrayList<String> getFams()
    {
        return this.famIds;
    }
    
    
    String toTestString()
    {
        return "ID: " + this.ID + " Name: " + this.name; // + " Gender: " + this.gender + " Birthday: " + this.birthday;
    }
}