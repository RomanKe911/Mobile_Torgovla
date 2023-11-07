package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Debet {

    public String k_agetn;
    public String uid_k_agent;
    public String debet;
    public String agent;

    public ListAdapterSimple_Debet(String k_agetn, String uid_k_agent, String debet, String agent) {
        this.k_agetn = k_agetn;
        this.uid_k_agent = uid_k_agent;
        this.debet = debet;
        this.agent = agent;
    }


    public String getK_agetn() {
        return k_agetn;
    }

    public void setK_agetn(String k_agetn) {
        this.k_agetn = k_agetn;
    }

    public String getUid_k_agent() {
        return uid_k_agent;
    }

    public void setUid_k_agent(String uid_k_agent) {
        this.uid_k_agent = uid_k_agent;
    }

    public String getDebet() {
        return debet;
    }

    public void setDebet(String debet) {
        this.debet = debet;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

}
