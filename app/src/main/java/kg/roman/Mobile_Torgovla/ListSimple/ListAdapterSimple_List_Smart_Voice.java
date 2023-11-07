package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_List_Smart_Voice {

    public String name;
    public String uid;
    public String univ;
    public String kolbox;
    public String ostatok;
    public String cena;
    public String image;

    public ListAdapterSimple_List_Smart_Voice(String name, String uid, String univ, String kolbox, String ostatok, String cena, String image) {
        this.name = name;
        this.uid = uid;
        this.univ = univ;
        this.kolbox = kolbox;
        this.ostatok = ostatok;
        this.cena = cena;
        this.image = image;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUniv() {
        return univ;
    }
    public void setUniv(String univ) {
        this.univ = univ;
    }

    public String getKolbox() {
        return kolbox;
    }
    public void setKolbox(String kolbox) {
        this.kolbox = kolbox;
    }

    public String getOstatok() {
        return ostatok;
    }
    public void setOstatok(String ostatok) {
        this.ostatok = ostatok;
    }

    public String getCena() {
        return cena;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}
