package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Ros_RN {

    public String rn;
    public String data;
    public String vrema;
    public String summa;

    public ListAdapterSimple_Ros_RN(String rn, String data, String vrema, String summa) {
        this.rn = rn;
        this.data = data;
        this.vrema = vrema;
        this.summa = summa;
    }


    public String getRN() {
        return rn;
    }
    public void setRN(String rn) {
        this.rn = rn;
    }


    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getVrema() {
        return vrema;
    }
    public void setVrema(String vrema) {
        this.vrema = vrema;
    }

    public String getSumma() {
        return summa;
    }
    public void setSumma(String summa) {
        this.summa = summa;
    }

}
