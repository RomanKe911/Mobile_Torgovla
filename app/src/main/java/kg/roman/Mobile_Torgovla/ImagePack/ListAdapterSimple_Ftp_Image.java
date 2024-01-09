package kg.roman.Mobile_Torgovla.ImagePack;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Ftp_Image {

    public String Brands_Text;
    public String Brends_Image;
    public String Files_Size;
    public String Files_Count;
    public String Data_Update;
    public Boolean Status_Update;


    public ListAdapterSimple_Ftp_Image(String Brends_Text, String Brends_Image,
                                       String Files_Size, String Files_Count,
                                       String Data_Update, Boolean Status_Update) {
        this.Brands_Text = Brends_Text;
        this.Brends_Image = Brends_Image;
        this.Files_Size = Files_Size;
        this.Files_Count = Files_Count;
        this.Data_Update = Data_Update;
        this.Status_Update = Status_Update;
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

    public String getFiles_Count() {
        return Files_Count;
    }
    public void setFiles_Count(String Files_Count) {
        this.Files_Count = Files_Count;
    }

    public String getData_Update() {
        return Data_Update;
    }
    public void setData_Update(String Data_Update) {
        this.Data_Update = Data_Update;
    }

    public Boolean getStatus_Update() {
        return Status_Update;
    }
    public void setStatus_Update(Boolean Status_Update) {
        this.Status_Update = Status_Update;
    }

}
