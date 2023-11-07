package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Suncape {
    public String name;
    public String kol;
    public String cena;
    public String cena_nall;
    public String cena_kons;
    public String ostatki;
    public String image;
    public String kod_univ;
    public String strih;





    public ListAdapterSimple_Suncape(String kod_univ, String name, String kol,
                                     String cena, String cena_nall, String strih, String ostatki, String image) {
        this.kod_univ = kod_univ;
        this.name = name;
        this.kol = kol;
        this.strih = strih;
        this.cena = cena;
        this.cena_nall = cena_nall;
        this.cena_kons = cena_kons;
        this.image = image;
        this.ostatki = ostatki;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKol() {
        return kol;
    }

    public void setKol(String kol) {
        this.kol = kol;
    }

    public String getStrih() {
        return strih;
    }

    public void setStrih(String strih) {
        this.strih = strih;
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

    public String getCena_Nall() {
        return cena_nall;
    }
    public void setCena_Nall(String cena_nall) {
        this.cena_nall = cena_nall;
    }

    public String getCenaKons() {
        return cena_kons;
    }
    public void setCenaKons(String cena_kons) {
        this.cena_kons = cena_kons;
    }


    public String getOstatki() {
        return ostatki;
    }
    public void setOstatki(String ostatki) {
        this.ostatki= ostatki;
    }

    public String getKod_univ() {
        return kod_univ;
    }
    public void setKod_univ(String kod_univ) {
        this.kod_univ= kod_univ;
    }
}
