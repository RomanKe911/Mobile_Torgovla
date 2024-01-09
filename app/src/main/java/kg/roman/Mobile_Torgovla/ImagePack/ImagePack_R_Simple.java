package kg.roman.Mobile_Torgovla.ImagePack;

/**
 * Created by user on 03.02.2016.
 */
public class ImagePack_R_Simple {

    public String Brands_Text;
    public String Brends_Image;
    public String Files_Size;
    public int Files_Count;
    public long Data_Update;
    public long Status_Update;
    private Boolean isSelected;


    public ImagePack_R_Simple(String Brends_Text, String Brends_Image,
                              String Files_Size, int Files_Count,
                              long Data_Update, long Status_Update,
                              Boolean isSelected) {
        this.Brands_Text = Brends_Text;
        this.Brends_Image = Brends_Image;
        this.Files_Size = Files_Size;
        this.Files_Count = Files_Count;
        this.Data_Update = Data_Update;
        this.Status_Update = Status_Update;
        this.isSelected=isSelected;

    }


    public String getBrends_Text() {
        return Brands_Text;
    }
    public void setBrends_Text(String Brends_Text) {
        this.Brands_Text = Brends_Text;
    }

    public String getBrends_Image() {
        return Brends_Image;
    }
    public void setBrends_Image(String Brends_Image) {
        this.Brends_Image = Brends_Image;
    }

    public String getFiles_Size() {
        return Files_Size;
    }
    public void setFiles_Size(String Files_Size) {
        this.Files_Size = Files_Size;
    }

    public int getFiles_Count() {
        return Files_Count;
    }
    public void setFiles_Count(int Files_Count) {
        this.Files_Count = Files_Count;
    }

    public long getData_Update() {
        return Data_Update;
    }
    public void setData_Update(long Data_Update) {
        this.Data_Update = Data_Update;
    }

    public long getStatus_Update() {
        return Status_Update;
    }
    public void setStatus_Update(long Status_Update) {
        this.Status_Update = Status_Update;
    }


    public boolean isSelected(){return  isSelected;}
    public void setSelected(boolean selected) {isSelected =selected;}

}
