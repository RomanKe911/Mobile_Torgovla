package kg.roman.Mobile_Torgovla.ArrayList;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Login {
    public String name;
    public String image;
    public String name_type;

    public ListAdapterSimple_Login(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public ListAdapterSimple_Login(String name, String image, String name_type) {
        this.name = name;
        this.name_type = name_type;
        this.image = image;

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName_type() {
        return name_type;
    }
    public void setName_type(String name_type) {
        this.name_type = name_type;
    }


    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


}
