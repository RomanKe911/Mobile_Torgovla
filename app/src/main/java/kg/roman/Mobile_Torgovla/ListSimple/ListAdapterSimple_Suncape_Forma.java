package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Suncape_Forma {
    public String name;
    public String kol;
    public String cena;
    public String cena_nall;
    public String cena_kons;
    public String ostatki;
    public String image;
    public String kod_univ;
    public String strih;
    public String kod_uid;
    public String uid_sklad;
    public String name_sklad;





    public ListAdapterSimple_Suncape_Forma(String kod_uid, String kod_univ, String name, String kol,
                                           String cena, String cena_nall,
                                           String strih, String ostatki, String image,
                                           String name_sklad, String uid_sklad) {
        this.kod_uid = kod_uid;
        this.kod_univ = kod_univ;
        this.name = name;
        this.kol = kol;
        this.strih = strih;
        this.cena = cena;
        this.cena_nall = cena_nall;
      //  this.cena_kons = cena_kons;
        this.image = image;
        this.ostatki = ostatki;
        this.uid_sklad = uid_sklad;
        this.name_sklad = name_sklad;

    }
    public String getKod_uid() {
        return kod_uid;
    }

    public void setKod_uid(String kod_uid) {
        this.kod_uid = kod_uid;
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


    public String getUid_sklad() {
        return uid_sklad;
    }
    public void setUid_sklad(String uid_sklad) {
        this.uid_sklad= uid_sklad;
    }

    public String getName_sklad() {
        return name_sklad;
    }
    public void setName_sklad(String name_sklad) {
        this.name_sklad= name_sklad;
    }
}
