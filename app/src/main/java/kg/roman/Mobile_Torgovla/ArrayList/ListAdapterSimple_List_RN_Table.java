package kg.roman.Mobile_Torgovla.ArrayList;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_List_RN_Table {
    public String kodrn;
    public String k_agent;
    public String k_agentUID;
    public String vrema;
    public String data;
    public String summa;
    public String itogo;
    public String adress;

    public String debet;
    public String skidka;
    public String status;
    public String sklad;
    public String skladUID;

    public ListAdapterSimple_List_RN_Table(String kodrn, String k_agent, String k_agentUID,
                                           String vrema, String data, String summa, String itogo,
                                           String adress, String skidka, String debet, String status,
                                           String sklad, String skladUID) {
        this.kodrn = kodrn;
        this.k_agent = k_agent;
        this.k_agentUID = k_agentUID;
        this.vrema = vrema;
        this.data = data;
        this.itogo = itogo;
        this.summa = summa;
        this.adress = adress;
        this.debet = debet;
        this.status = status;
        this.skidka = skidka;
        this.sklad = sklad;
        this.skladUID = skladUID;
    }

    public String getKodrn() {
        return kodrn;
    }

    public void setKodrn(String kodrn) {
        this.kodrn = kodrn;
    }

    public String getK_agent() {
        return k_agent;
    }

    public void setK_agent(String k_agent) {
        this.k_agent = k_agent;
    }

    public String getK_agentUID() {
        return k_agentUID;
    }

    public void setK_agentUID(String k_agentUID) {
        this.k_agentUID = k_agentUID;
    }

    public String getVrema() {
        return vrema;
    }

    public void setVrema(String vrema) {   this.vrema = vrema; }

    public String getData() {
        return data;
    }

    public void setData(String data) {   this.data = data; }

    public String getSumma() {
        return summa;
    }

    public void setSumma(String summa) {
        this.summa = summa;
    }



    public String getItogo() {
        return itogo;
    }

    public void setItogo(String itogo) {
        this.itogo = itogo;
    }


    public String getAdress() {
        return adress;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }




    public String getDebet() {
        return debet;
    }
    public void setDebet(String debet) {
        this.debet = debet;
    }

    public String getSkidka() {
        return skidka;
    }
    public void setSkidka(String skidka) {
        this.skidka = skidka;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getSklad() {
        return sklad;
    }
    public void setSklad(String sklad) {
        this.sklad = sklad;
    }

    public String getSkladUID() {
        return skladUID;
    }
    public void setSkladUID(String skladUID) {
        this.skladUID = skladUID;
    }


}
