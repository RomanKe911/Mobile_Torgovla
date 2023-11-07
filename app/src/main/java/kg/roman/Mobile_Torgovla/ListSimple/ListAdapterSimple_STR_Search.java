package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_STR_Search {
    public String brends;
    public String p_group;
    public String kod;
    public String name;
    public String kolbox;
    public String cena;
    public String image;
    public String strih;
    public String koduniv;
    public String koduid;
    public String name_image;

    public ListAdapterSimple_STR_Search(String brends, String p_group, String kod, String name,
                                        String kolbox, String cena, String image, String strih,
                                        String koduniv, String koduid, String name_image) {

        this.brends = brends;
        this.p_group = p_group;
        this.kod = kod;
        this.name = name;
        this.kolbox = kolbox;
        this.cena = cena;
        this.image = image;
        this.strih = strih;
        this.koduniv = koduniv;
        this.koduid = koduid;
        this.name_image = name_image;
    }

    public ListAdapterSimple_STR_Search() {

    }

    public String getBrends() {
        return brends;
    }
    public void setBrends(String brends) {
        this.brends = brends;
    }

    public String getP_group() {
        return p_group;
    }
    public void setP_group(String p_group) {
        this.p_group = p_group;
    }

    public String getKod() {
        return kod;
    }
    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getKolbox() {
        return kolbox;
    }
    public void setKolbox(String kolbox) {
        this.kolbox = kolbox;
    }

    public String getCena() {
        return cena;
    }
    public void setCena(String cena) {this.cena = cena;}

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getStrih() {
        return strih;
    }
    public void setStrih(String strih) {
        this.strih = strih;
    }

    public String getKoduniv() {
        return koduniv;
    }
    public void setKoduniv(String koduniv) {
        this.koduniv = koduniv;
    }

    public String getKoduid() {
        return koduid;
    }
    public void setKoduid(String koduid) {
        this.koduid = koduid;
    }

    public String getName_iamge() {
        return name_image;
    }
    public void setName_image(String name_image) {
        this.name_image = name_image;
    }
}
