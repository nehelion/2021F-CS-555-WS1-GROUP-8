import java.io.*;
import java.util.*;

public class Family
{
    private String ID;
    private String husbandID;
    private String wifeID;
    private ArrayList<String> children;
    private String marrDate;
    private String divDate;
    
    public Family(String ID, String husbandID, String wifeID, ArrayList<String> children, String marrDate, String divDate)
    {
        this.ID = ID;
        this.husbandID = husbandID;
        this.wifeID = wifeID;
        this.children = children;
        this.marrDate = marrDate;
        this.divDate = divDate;
    }
    
    String getID()
    {
        return this.ID;
    }
    
    String getHusbandID()
    {
        return this.husbandID;
    }
    
    String getMarrDate()
    {
        return this.marrDate;
    }
    String getDivDate()
    {
        return this.divDate;
    }
    // String getHusbandName()
    // {
    //     for(int j = 0; j < individuals.size(); j++)
    //     {
    //         if(this.husbandID.equals(individuals.get(j).getID()))
    //         {
    //             return individuals.get(j).getName();
    //         }
    //     }
    //     return "ERROR";
    // }
    
    // String getHusbandBirthDay(String inputHusbandID)
    // {
    //     for(int j = 0; j < individuals.size(); j++)
    //     {
    //         if(inputHusbandID.equals(individuals.get(j).getID()))
    //         {
    //             return individuals.get(j).getBirthday();
    //         }
    //     }
    //     return "ERROR";
    // }
    
    String getWifeID()
    {
        return this.wifeID;
    }
    
    // String getWifeName()
    // {
    //     for(int j = 0; j < individuals.size(); j++)
    //     {
    //         if(this.wifeID.equals(individuals.get(j).getID()))
    //         {
    //             return individuals.get(j).getName();
    //         }
    //     }
    //     return "ERROR";
    // }
    
    // String getWifeBirthDay(String inputWifeID)
    // {
    //     for(int j = 0; j < individuals.size(); j++)
    //     {
    //         if(inputWifeID.equals(individuals.get(j).getID()))
    //         {
    //             return individuals.get(j).getBirthday();
    //         }
    //     }
    //     return "ERROR";
    // }
    
    ArrayList<String> getChildren()
    {
        return this.children;
    }
    
    // String toTestString()
    // {
    //     return "ID: " + this.ID + " Husband ID: " + this.husbandID + " Husband Name: " + getHusbandName() + " Wife ID: " + this.wifeID + " Wife Name: " + getWifeName(); //+ "-" + this.children;
    // }
}