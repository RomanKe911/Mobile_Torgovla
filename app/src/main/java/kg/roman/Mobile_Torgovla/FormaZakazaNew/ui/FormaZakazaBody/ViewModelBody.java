package kg.roman.Mobile_Torgovla.FormaZakazaNew.ui.FormaZakazaBody;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelBody extends ViewModel {

    private final MutableLiveData<String> mText;

    public ViewModelBody() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}