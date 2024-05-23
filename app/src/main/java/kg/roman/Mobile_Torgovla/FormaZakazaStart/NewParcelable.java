package kg.roman.Mobile_Torgovla.FormaZakazaStart;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NewParcelable implements Parcelable {

    private String clientName, clientUID, clientAdress;
    private String kodRN, sale, skladUID;


    public NewParcelable(String w_clientName, String w_clientUID, String w_clientAdress, String w_kodRN, String w_Sale, String w_SkladUID)
    {
        clientName = w_clientName;
        clientUID = w_clientUID;
        clientAdress = w_clientAdress;
        kodRN = w_kodRN;
        sale = w_Sale;
        skladUID = w_SkladUID;
    }
    public NewParcelable(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        clientName = data[0];
        clientUID = data[1];
        clientAdress = data[2];
        kodRN = data[3];
        sale = data[4];
        skladUID = data[5];
    }

    public String getClientName() {
        return clientName;
    }
    public String getClientUID() {
        return clientUID;
    }
    public String getClientAdress() {
        return clientAdress;
    }
    public String getKodRN() {
        return kodRN;
    }
    public String getSale() {return sale; }
    public String getSkladUID() {return skladUID; }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStringArray(new String[] { clientName, clientUID, clientAdress, kodRN });
    }
    public static final Parcelable.Creator<NewParcelable> CREATOR = new Parcelable.Creator<NewParcelable>() {

        @Override
        public NewParcelable createFromParcel(Parcel source) {
            return new NewParcelable(source);
        }

        @Override
        public NewParcelable[] newArray(int size) {
            return new NewParcelable[size];
        }
    };

}
