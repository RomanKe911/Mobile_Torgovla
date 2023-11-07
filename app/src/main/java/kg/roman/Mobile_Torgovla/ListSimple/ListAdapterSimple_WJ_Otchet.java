package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_WJ_Otchet {

    public String data;
    public String agents;
    public String brends;
    public String name;
    public String kol_vo;
    public String cena;
    public String summa;


    public ListAdapterSimple_WJ_Otchet(String brends, String kol_vo, String summa, String cena) {
        this.data = data;
        this.agents = agents;
        this.brends = brends;
        this.name = name;
        this.cena = cena;
        this.kol_vo = kol_vo;
        this.summa = summa;

    }


    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getAgents() {
        return agents;
    }
    public void setAgents(String agents) {
        this.agents = agents;
    }

    public String getBrends() {
        return brends;
    }
    public void setBrends(String brends) {
        this.brends = brends;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getKol_vo() {
        return kol_vo;
    }
    public void setKol_vo(String kol_vo) {
        this.kol_vo = kol_vo;
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


}
