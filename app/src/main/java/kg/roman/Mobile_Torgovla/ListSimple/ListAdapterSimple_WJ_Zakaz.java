package kg.roman.Mobile_Torgovla.ListSimple;

public class ListAdapterSimple_WJ_Zakaz {

    String kodUID;
    String kodUniv;
    String name;
    String kod;
    String count;
    String cena;
    String cena_sk;
    String summa;
    String skidka;
    String itogo;
    public String image;

    public ListAdapterSimple_WJ_Zakaz(String name, String kodUID, String kodUniv, String count, String cena, String cena_sk, String summa, String skidka, String itogo, String image) {
        this.kodUID=kodUID;
        this.kodUniv=kodUniv;
        this.name=name;
        this.kod=kod;
        this.count=count;
        this.cena=cena;
        this.cena_sk=cena_sk;
        this.summa=summa;
        this.skidka=skidka;
        this.itogo=itogo;
        this.image = image;

    }

    public String getUID() {
        return kodUID;
    }
    public void setUID(String kodUID) {
        this.kodUID = kodUID;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }


    public String getKodUniv() {return kodUniv; }





    public String getKod() {
        return kod;
    }



    public String getSumma() {
        return summa;
    }
    public void setSumma(String summa) {
        this.summa = summa;
    }


    public String getCena() {
        return cena;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }


    public String getCena_Sk() {
        return cena_sk;
    }
    public void setCena_Sk(String cena_sk) {this.cena_sk = cena_sk; }



    public String getSkidka() {
        return skidka;
    }
    public void setSkidka(String skidka) {this.skidka = skidka; }


    public String getItogo() {
        return itogo;
    }

    public void setItogo(String itogo) {this.itogo = itogo; }






    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}