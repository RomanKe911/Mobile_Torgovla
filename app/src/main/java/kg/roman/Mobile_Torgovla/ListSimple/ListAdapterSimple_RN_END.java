package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_RN_END {


    public String name;
    public String kol;
    public String cena;
    public String cenaSK;
    public String summa;
    public String skidka;
    public String itogo;
    public String image;
    public String koduid;


    public ListAdapterSimple_RN_END(String name, String kol, String cena, String cenaSK, String summa, String skidka, String itogo, String image, String koduid) {
        this.name = name;
        this.kol = kol;
        this.cena = cena;
        this.cenaSK = cenaSK;
        this.summa = summa;
        this.skidka = skidka;
        this.itogo = itogo;
        this.image = image;
        this.koduid = koduid;
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

    public String getCena() {
        return cena;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getCenaSK() {
            return cenaSK;
    }
    public void setCenaSK(String cenaSK) {
        this.cenaSK = cenaSK;
    }

    public String getSumma() {
        return summa;
    }
    public void setSumma(String summa) {
        this.summa = summa;
    }

    public String getSkidka() {
        return skidka;
    }
    public void setSkidka(String skidka) {
        this.skidka = skidka;
    }

    public String getItogo() {
        return itogo;
    }
    public void setItogo(String itogo) {
        this.itogo = itogo;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


    public String getKoduid() {
        return koduid;
    }
    public void setKoduid(String koduid) {
        this.koduid = koduid;
    }

}
