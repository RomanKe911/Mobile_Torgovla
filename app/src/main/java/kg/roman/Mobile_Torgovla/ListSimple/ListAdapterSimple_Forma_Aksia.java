package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Forma_Aksia {

    public String name;
    public String kod_uid;
    public String cena;
    public String cena_aks;
    public String ostatok;
    public String kol;
    public String image;
    public String skidka;
    public String sklad;
    public String sklad_uid;

    public ListAdapterSimple_Forma_Aksia(String name, String kod_uid, String cena, String cena_aks,
                                         String skidka, String ostatok, String kol, String image,
                                         String sklad, String sklad_uid) {
        this.name = name;
        this.kod_uid = kod_uid;
        this.cena = cena;
        this.cena_aks = cena_aks;
        this.ostatok = ostatok;
        this.kol = kol;
        this.image = image;
        this.skidka = skidka;
        this.sklad = sklad;
        this.sklad_uid = sklad_uid;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getKod_UID() {
        return kod_uid;
    }
    public void setKod_UID(String kod_uid) {
        this.kod_uid = kod_uid;
    }

    public String getCena() {
        return cena;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getSkidka() {
        return skidka;
    }
    public void setSkidka(String skidka) {
        this.skidka = skidka;
    }

    public String getCena_aks() {
        return cena_aks;
    }
    public void setCena_aks(String cena_aks) {
        this.cena_aks = cena_aks;
    }

    public String getOstatok() {
        return ostatok;
    }
    public void setOstatok(String ostatok) {
        this.ostatok = ostatok;
    }

    public String getKol() {
        return kol;
    }
    public void setKol(String kol) {
        this.kol = kol;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


    public String getSklad() {
        return sklad;
    }
    public void setSklad(String sklad) {
        this.sklad = sklad;
    }

    public String getSklad_uid() {
        return sklad_uid;
    }
    public void setSklad_uid(String sklad_uid) {
        this.sklad_uid = sklad_uid;
    }


}
