package kg.roman.Mobile_Torgovla.XML_Files;

public class MTW_Customers {
    private String uid_k_agent;
    private String k_agent;
    private String adress;
    private String uid_agent;
    private String readuid;
    private String roadname;

    public String getUid_k_agent() {
        return uid_k_agent;
    }

    public String getK_agent() {
        return k_agent;
    }

    public String getAdress() {
        return adress;
    }

    public String getUid_agent() {
        return uid_agent;
    }

    public String getReaduid() {
        return readuid;
    }

    public String getRoadname() {
        return roadname;
    }


    public void setUid_k_agent(String uid_k_agent) {
        this.uid_k_agent = uid_k_agent;
    }

    public void setK_agent(String k_agent) {
        this.k_agent = k_agent;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setUid_agent(String uid_agent) {
        this.uid_agent = uid_agent;
    }

    public void setReaduid(String readuid) {
        this.readuid = readuid;
    }

    public void setRoadname(String roadname) {
        this.roadname = roadname;
    }

    public String toString() {
        // return "uid:" + uid + " \n price:" + cena;
        return "";
    }
}