package kg.roman.Mobile_Torgovla.FormaZakazaNew.ui.FormaZakaza_End;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModel_End extends ViewModel {

    private final MutableLiveData<String> mText;

    public ViewModel_End() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}