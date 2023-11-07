package kg.roman.Mobile_Torgovla.ArrayList;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_List_Zakaz_Content {
    public String _id;
    public String kod;
    public String tochaka;
    public String summa;
    public String skidka;
    public String itogo;

    public ListAdapterSimple_List_Zakaz_Content(String kod, String tochaka,
                                                String summa, String skidka, String itogo) {
        this._id = _id;
        this.kod = kod;
        this.tochaka = tochaka;
        this.summa = summa;
        this.skidka = skidka;
        this.itogo = itogo;

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getKod() {
        return kod;
    }

    public void setImage(String kod) {
        this.kod = kod;
    }

    public String getTochaka() {
        return tochaka;
    }

    public void setTochaka(String kod) {
        this.tochaka = tochaka;;
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
        this.itogo = itogo;;
    }

}
