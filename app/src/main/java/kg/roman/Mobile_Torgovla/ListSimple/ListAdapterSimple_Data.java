package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Data {

    public String name_up;
    public String data_up;
    public String image;

    public ListAdapterSimple_Data(String name_up, String data_up) {
        this.name_up = name_up;
        this.data_up = data_up;
        this.image = image;

    }


    public String getName_up() {
        return name_up;
    }
    public void setName_up(String name_up) {
        this.name_up = name_up;
    }

    public String getData_up() {
        return data_up;
    }
    public void setData_up(String data_up) {
        this.data_up = data_up;
    }


    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}
