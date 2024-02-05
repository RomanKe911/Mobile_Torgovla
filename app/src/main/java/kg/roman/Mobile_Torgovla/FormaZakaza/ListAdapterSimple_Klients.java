package kg.roman.Mobile_Torgovla.FormaZakaza;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Klients {
    public String uid;
    public String name;
    public String adress;
    public String userUID;
    public String roadUID;
    public String roadName;
    public Integer image;
    public Integer debet;


    public ListAdapterSimple_Klients(String name, String uid, String adress, Integer image, String userUID) {
        this.name = name;
        this.uid = uid;
        this.adress = adress;
        this.userUID = userUID;
        this.roadUID = roadUID;
        this.roadName = roadName;
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUID() {
        return this.uid;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }


    public String getAdress() {
        return this.adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }


    public String getUserUID() {
        return this.userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }


    public String getRoadUID() {
        return this.roadUID;
    }

    public void setRoadUID(String roadUID) {
        this.roadUID = roadUID;
    }


    public String getRoadName() {
        return this.roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public Integer getImage() {
        return this.image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }


}
