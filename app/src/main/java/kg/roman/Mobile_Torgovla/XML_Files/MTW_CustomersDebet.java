package kg.roman.Mobile_Torgovla.XML_Files;

public class MTW_CustomersDebet {
    private String Agent;
    private String uid_agent;
    private String Customer;
    private String uid_customer;
    private String sum_debet;

    public String getAgent() {
        return Agent;
    }

    public String getUid_agent() {
        return uid_agent;
    }

    public String getCustomer() {
        return Customer;
    }

    public String getUid_customer() {
        return uid_customer;
    }

    public String getSum_debet() {
        return sum_debet;
    }



    public void setAgent(String Agent) {
        this.Agent = Agent;
    }

    public void setUid_agent(String uid_agent) {
        this.uid_agent= uid_agent;
    }

    public void setCustomer(String Customer) {
        this.Customer = Customer;
    }

    public void setUid_customer(String uid_customer) {
        this.uid_customer = uid_customer;
    }

    public void setSum_debet(String sum_debet) {
        this.sum_debet = sum_debet;
    }

    public String toString() {
        // return "uid:" + uid + " \n price:" + cena;
        return "";
    }
}