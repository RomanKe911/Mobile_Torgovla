package kg.roman.Mobile_Torgovla.ListSimple;

public class ListAdapterSimple_WJ_Scan {


    String name;
    String kod;
    String cena;
    String strih;
    String ostatok;
    String image;


    public ListAdapterSimple_WJ_Scan(String name, String kod, String cena, String ostatok, String strih, String image) {
        this.name=name;
        this.kod=kod;
        this.cena=cena;
        this.strih=strih;
        this.image=image;
        this.ostatok=ostatok;

    }


    public String getName() {
        return name;
    }

    public String getKod() {
        return kod;
    }

    public String getCena() {
        return cena;
    }

    public String getStrih() {
        return strih;
    }

    public String getOstatok() {
        return ostatok;
    }

    public String getImage() {
        return image;
    }

}