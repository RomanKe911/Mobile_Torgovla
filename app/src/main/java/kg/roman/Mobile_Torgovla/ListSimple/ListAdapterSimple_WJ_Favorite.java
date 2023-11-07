package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_WJ_Favorite {
    public String name;
    public String image;
    public String cena;
    public String cena_skidka;
    public String ostatki;
    public String kol_box;
    public String kod_univ;
    public String strih;

    public ListAdapterSimple_WJ_Favorite(String name, String image, String cena, String cena_skidka,
                                         String ostatki, String kol_box, String kod_univ, String strih) {

        this.name = name;
        this.image = image;
        this.cena = cena;
        this.cena_skidka = cena_skidka;
        this.ostatki = ostatki;
        this.kol_box = kol_box;
        this.kod_univ = kod_univ;
        this.strih = strih;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getCena() {
        return cena;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getCena_skidka() {
        return cena_skidka;
    }
    public void setCena_skidka(String cena_skidka) {
        this.cena_skidka = cena_skidka;
    }

    public String getOstatki() {
        return ostatki;
    }
    public void setOstatki(String ostatki) {
        this.ostatki= ostatki;
    }

    public String getKol_box() {
        return kol_box;
    }
    public void setKol_box(String kol_box) {
        this.kol_box = kol_box;
    }

    public String getKod_univ() {
        return kod_univ;
    }
    public void setKod_univ(String kod_univ) {
        this.kod_univ= kod_univ;
    }

    public String getStrih() {
        return strih;
    }
    public void setStrih(String strih) {
        this.strih = strih;
    }

}
