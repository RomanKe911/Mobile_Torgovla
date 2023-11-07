package kg.roman.Mobile_Torgovla.XML_Files;

public class MTW_Nomenclatures {
    private String brends;
    private String p_group;
    private String kod;
    private String name;
    private String cena;
    private String kolbox;
    private String strih;
    private String kod_univ;
    private String koduid;


    private String count;
    private String image;
    private String work;

    public String getBrends() {
        return brends;
    }
    public String getP_group() {
        return p_group;
    }
    public String getKod() {
        return kod;
    }
    public String getName() {
        return name;
    }
    public String getKolbox() {
        return kolbox;
    }
    public String getCena() {
        return cena;
    }
    public String getStrih() {
        return strih;
    }
    public String getKod_univ() {
        return kod_univ;
    }
    public String getKoduid() {
        return koduid;
    }


    public String getCount() {
        return count;
    }
    public String getImage() {
        return image;
    }
    public String getWork() {
        return work;
    }

    public void setBrends(String brends) {
        this.brends = brends;
    }
    public void setP_group(String p_group) {
        this.p_group = p_group;
    }
    public void setKod(String kod) {
        this.kod = kod;
    }
    public void setName(String name)  {
        this.name = name;
    }
    public void setKolbox(String kolbox) {this.kolbox = kolbox;}
    public void setCena(String cena) {this.cena = cena;}
    public void setStrih(String strih) {this.strih = strih;}
    public void setKod_univ(String kod_univ) {this.kod_univ = kod_univ;}
    public void setKoduid(String koduid) {this.koduid = koduid;}



    public void setCount(String count) {this.count = count;}
    public void setImage(String image) {this.image = image;}
    public void setWork(String work) {this.work = work;}


    public String toString() {
       // return "uid:" + uid + " \n price:" + cena;
        return "";
    }
}