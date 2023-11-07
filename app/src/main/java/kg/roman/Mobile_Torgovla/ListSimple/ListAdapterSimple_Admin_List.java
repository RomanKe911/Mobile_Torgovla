package kg.roman.Mobile_Torgovla.ListSimple;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Admin_List {

    public String name;
    public String kod;
    public Boolean brends_bool;
    private boolean checked = false;
    private boolean checkedALL = false;
    private String itemText = "";

    public ListAdapterSimple_Admin_List(String name, String kod, Boolean brends_bool) {
        this.name = name;
        this.kod = kod;
        this.brends_bool = brends_bool;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getKod() {
        return kod;
    }
    public void setKod(String kod) {
        this.kod=kod;
    }

    public Boolean getBrends_bool() {
        return brends_bool;
    }
    public void setBrends_bool(Boolean brends_bool) {
        this.brends_bool=brends_bool;
    }

    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }



    public boolean isCheckedALL() {
        return checkedALL;
    }
    public void setCheckedALL(boolean checkedALL) {
        this.checkedALL = checkedALL;
    }



    public void setCheckedClear(boolean checked) {
        this.checked = checked;
    }
    public String getItemText() {
        return itemText;
    }

}
