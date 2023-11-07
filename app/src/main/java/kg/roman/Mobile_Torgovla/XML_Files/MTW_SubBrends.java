package kg.roman.Mobile_Torgovla.XML_Files;

public class MTW_SubBrends {
    private String name;
    private String kod;
    private String parents_kod;
    private String prefic;
    private String up;

    public String getName() {
        return name;
    }
    public String getKod() {
        return kod;
    }
    public String getParents_kod() {
        return parents_kod;
    }
    public String getPrefic() {
        return prefic;
    }
    public String getUp() {
        return up;
    }


    public void setName(String name) {
        this.name=name;
    }
    public void setKod(String kod) {
        this.kod = kod;
    }
    public void setParents_kod(String parents_kod) {
        this.parents_kod = parents_kod;
    }
    public void setPreficd(String prefic) {
        this.prefic = prefic;
    }
    public void setUp(String up) {
        this.up = up;
    }


    public String toString() {
        // return "uid:" + uid + " \n price:" + cena;
        return "";
    }
}