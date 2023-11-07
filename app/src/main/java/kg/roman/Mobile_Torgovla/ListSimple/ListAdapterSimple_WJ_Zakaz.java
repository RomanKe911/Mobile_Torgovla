package kg.roman.Mobile_Torgovla.ListSimple;

public class ListAdapterSimple_WJ_Zakaz {

    String id;
    String name;
    String kod;
    String kol;
    String cena;
    String cena_sk;
    String summa;
    String skidka;
    String itogo;
    public String image;

    public ListAdapterSimple_WJ_Zakaz(String name, String kod, String kol, String cena, String cena_sk, String summa, String skidka, String itogo, String image) {
        this.id=id;
        this.name=name;
        this.kod=kod;
        this.kol=kol;
        this.cena=cena;
        this.cena_sk=cena_sk;
        this.summa=summa;
        this.skidka=skidka;
        this.itogo=itogo;
        this.image = image;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKod() {
        return kod;
    }

    public String getKol() {
        return kol;
    }

    public String getSumma() {
        return summa;
    }

    public String getCena() {
        return cena;
    }

    public String getCena_Sk() {
        return cena_sk;
    }

    public String getSkidka() {
        return skidka;
    }

    public String getItogo() {
        return itogo;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}