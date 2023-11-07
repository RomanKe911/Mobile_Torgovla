package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Otchet_Ostatok {
    public String name;
    public String brend;
    public String group;
    public String sklad;
    public String cena;
    public String count;
    public String kolbox;
    public String kolbox_delete;
    public String image;


    public ListAdapterSimple_Otchet_Ostatok(String name, String brend, String group, String sklad,
                                            String cena, String count, String kolbox, String kolbox_delete,
                                            String image) {
        this.name = name;
        this.brend = brend;
        this.group = group;
        this.sklad = sklad;
        this.cena = cena;
        this.count = count;
        this.kolbox = kolbox;
        this.kolbox_delete = kolbox_delete;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public String getBrend() {
        return brend;
    }
    public String getGroup() {
        return group;
    }
    public String getSklad() {
        return sklad;
    }
    public String getCena() {
        return cena;
    }
    public String getCount() {
        return count;
    }
    public String getKolbox() {
        return kolbox;
    }
    public String getKolbox_Delete() {
        return kolbox_delete;
    }
    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setBrend(String brend) {
        this.brend = brend;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public void setSklad(String sklad) {
        this.image = sklad;
    }
    public void setCena(String cena) {
        this.cena = cena;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public void setKolbox(String kolbox) {
        this.kolbox = kolbox;
    }
    public void setKolbox_delete(String kolbox_delete) {
        this.kolbox_delete = kolbox_delete;
    }
    public void setImage(String image) {
        this.image = image;
    }



}
