import java.io.*;

public class Individual
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