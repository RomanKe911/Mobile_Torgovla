package kg.roman.Mobile_Torgovla.XML_Files;

public class User {
    private String name;
    private String uid;


    public String getName(){
        return name;
    }
    public String getUID(){
        return uid;
    }





    public void setName(String name){
        this.name = name;
    }
    public void setUID(String uid){
        this.uid = uid;
    }
    public String toString(){
        return  "User: " + name + " - " + uid;
    }
}