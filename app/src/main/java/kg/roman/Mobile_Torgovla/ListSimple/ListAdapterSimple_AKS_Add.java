package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_AKS_Add {

    public String brend;
    public String name_aks;
    public String catigory;

    public ListAdapterSimple_AKS_Add(String brend, String name_aks, String catigory) {
        this.brend = brend;
        this.name_aks = name_aks;
        this.catigory = catigory;
    }


    public String getBrend() {
        return brend;
    }

    public void setBrend(String name) {
        this.brend = brend;
    }


    public String getName_aks() {
        return name_aks;
    }

    public void setName_aks(String name_aks) {
        this.name_aks = name_aks;
    }


    public String getCatigory() {
        return catigory;
    }

    public void setCatigory(String catigory) {
        this.catigory = catigory;
    }

}
