package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Spinner_Filter {

    public String name;
    public String adress;
    public String uid;

    public ListAdapterSimple_Spinner_Filter(String name, String adress, String uid) {
        this.name = name;
        this.adress= adress;
        this.uid= uid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
