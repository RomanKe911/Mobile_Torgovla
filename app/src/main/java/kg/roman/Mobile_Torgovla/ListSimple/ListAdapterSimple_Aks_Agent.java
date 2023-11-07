package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Aks_Agent {

    public String agent;
    public String usl;
    public String par_uslov;


    public ListAdapterSimple_Aks_Agent(String agent, String usl, String par_uslov) {
        this.agent = agent;
        this.usl = usl;
    }


    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getUsl() {
        return usl;
    }

    public void setUsl(String usl) {
        this.usl = usl;
    }

    public String getPar_uslov() {
        return par_uslov;
    }

    public void setPar_uslov(String par_uslov) {
        this.par_uslov = par_uslov;
    }


}
