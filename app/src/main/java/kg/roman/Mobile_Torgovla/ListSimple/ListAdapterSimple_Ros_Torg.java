package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Ros_Torg {

    public String image;
    public String name;
    public String cena;
    public String kol_vo;
    public String kod_univ;
    public String strih;
    public String ostatok;

    public ListAdapterSimple_Ros_Torg(String image, String name, String cena, String kol_vo, String kod_univ, String strih, String ostatok) {
        this.image = image;
        this.name = name;
        this.cena = cena;
        this.kol_vo = kol_vo;
        this.kod_univ= kod_univ;
        this.strih= strih;
        this.ostatok= ostatok;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCena() {
        return cena;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getKol_vo() {
        return kol_vo;
    }
    public void setKol_vo(String kol_vo) {
        this.kol_vo = kol_vo;
    }

    public String getKod_univ() {
        return kod_univ;
    }
    public void setKod_univ(String kod_univ) {
        this.kod_univ = kod_univ;
    }

    public String getStrih() {
        return strih;
    }
    public void setStrih(String strih) {
        this.strih = strih;
    }

    public String getOstatok() {
        return ostatok;
    }
    public void setOstatok(String ostatok) {
        this.ostatok = ostatok;
    }




}
