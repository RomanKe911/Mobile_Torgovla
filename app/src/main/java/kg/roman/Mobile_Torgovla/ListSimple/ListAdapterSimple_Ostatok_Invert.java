package kg.roman.Mobile_Torgovla.ListSimple;

import android.widget.Toast;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Ostatok_Invert {
    public String name;
    public String cena;
    public String kolbox;
    public String ostatok;
    public String ostatok_f;
    public String kolbox_delete;
    public String razn;
    public String strih;
    public String kod_uid;
    public String image;
    public String image2;

    public ListAdapterSimple_Ostatok_Invert(String name, String kod_uid, String cena, String ostatok, String ostatok_f,
                                            String kolbox, String kolbox_delete, String razn, String strih, String image) {
        this.name = name;
        this.kod_uid = kod_uid;
        this.cena = cena;
        this.ostatok = ostatok;
        this.ostatok_f = ostatok_f;
        this.kolbox = kolbox;
        this.kolbox_delete = kolbox_delete;
        this.razn = razn;
        this.strih = strih;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public String getKod_uid() {
        return kod_uid;
    }
    public String getCena() {
        return cena;
    }
    public String getOstatok() {
        return ostatok;
    }
    public String getOstatok_f() {
        return ostatok_f;
    }
    public String getKolbox() {
        return kolbox;
    }
    public String getKolbox_Delete() {
        return kolbox_delete;
    }
    public String getRazn() {
        return razn;
    }
    public String getStrih() {
        return strih;
    }
    public String getImage() {
        return image;
    }




    public void setName(String name) {
        this.name = name;
    }
    public void setKod_uid(String kod_uid) {
        this.kod_uid = kod_uid;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }
    public void setOstatok(String ostatok) {
        this.ostatok = ostatok;
    }
    public void setOstatok_f(String ostatok_f) {
        this.ostatok_f = ostatok_f;
    }
    public void setKolbox(String kolbox) {
        this.kolbox = kolbox;
    }
    public void setKolbox_delete(String kolbox_delete) {
        this.kolbox_delete = kolbox_delete;
    }
    public void setRazn(String razn) {
        this.razn = razn;
    }
    public void setStrih(String strih) {
        this.strih = strih;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
