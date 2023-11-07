package kg.roman.Mobile_Torgovla.FormaZakaza;

public class SelectContrAgent {
    private String client_name; // Торговыя точка
    private String client_uid;  // uid-точки
    private String client_adress;  // адрес торговой точки
    private String client_credit;  // долги точки
    private int client_image; // ресурс картинки для точки

    public SelectContrAgent(String client_name, String client_uid, String client_adress, String client_credit, int client_image) {

        this.client_name = client_name;
        this.client_uid = client_uid;
        this.client_adress = client_adress;
        this.client_credit = client_credit;
        this.client_image = client_image;
    }


    public String getClientName() {
        return this.client_name;
    }

    public void setClientName(String client_name) {
        this.client_name = client_name;
    }


    public String getClientUID() {
        return this.client_uid;
    }

    public void setClientUID(String client_uid) {
        this.client_uid = client_uid;
    }


    public String getClientAdress() {
        return this.client_adress;
    }

    public void setClientAdress(String client_adress) { this.client_adress = client_adress; }

    public String getClientCredit() {
        return this.client_credit;
    }

    public void setClientCredit(String client_credit) {
        this.client_credit = client_credit;
    }


    public int getClientImage() {
        return this.client_image;
    }

    public void setClientImage(int client_image) {
        this.client_image = client_image;
    }

}
