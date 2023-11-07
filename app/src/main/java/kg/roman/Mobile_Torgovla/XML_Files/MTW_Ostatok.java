package kg.roman.Mobile_Torgovla.XML_Files;

public class MTW_Ostatok {
    private String data;
    private String sklad_uid;
    private String name_uid;
    private String name;
    private String count;

    public String getData() {
        return data;
    }
    public String getSklad_uid() {
        return sklad_uid;
    }
    public String getName_uid() {
        return name_uid;
    }
    public String getName() {
        return name;
    }
    public String getCount() {
        return count;
    }


    public void setData(String data) {
        this.data = data;
    }
    public void setSklad_uid(String sklad_uid) {
        this.sklad_uid = sklad_uid;
    }
    public void setName_uid(String name_uid) {
        this.name_uid = name_uid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCount(String count) {
        this.count = count;
    }


    public String toString() {
       // return "uid:" + uid + " \n price:" + cena;
        return "";
    }
}