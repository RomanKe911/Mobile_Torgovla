package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_WJ_Otchet_EveryDay {

    public String kod_rn;
    public String k_agn_name;
    public String k_agn_uid;
    public String k_agn_adress;
    public String summa;
    public String skidka;
    public String itogo;
    public String credit;

    public ListAdapterSimple_WJ_Otchet_EveryDay(String kod_rn, String k_agn_name, String k_agn_uid,
                                                String k_agn_adress, String summa, String skidka, String itogo, String credit) {
        this.kod_rn = kod_rn;
        this.k_agn_name= k_agn_name;
        this.k_agn_uid = k_agn_uid;
        this.k_agn_adress = k_agn_adress;
        this.summa = summa;
        this.skidka = skidka;
        this.itogo = itogo;
        this.credit = credit;

    }


    public String getKod_rn() {
        return kod_rn;
    }
    public void setKod_rn(String kod_rn) {
        this.kod_rn = kod_rn;
    }

    public String getK_agn_name() {
        return k_agn_name;
    }
    public void setK_agn_name(String k_agn_name) {
        this.k_agn_name = k_agn_name;
    }

    public String getK_agn_uid() {
        return k_agn_uid;
    }
    public void setK_agn_uid(String k_agn_uid) {
        this.k_agn_uid = k_agn_uid;
    }

    public String getK_agn_adress() {
        return k_agn_adress;
    }
    public void setK_agn_adress(String k_agn_adress) {
        this.k_agn_adress = k_agn_adress;
    }

    public String getSkidka() {
        return skidka;
    }
    public void setSkidka(String skidka) {
        this.skidka = skidka;
    }

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


    public String getCredit() {
        return credit;
    }
    public void setCredit(String credit) {
        this.credit = credit;
    }


}
