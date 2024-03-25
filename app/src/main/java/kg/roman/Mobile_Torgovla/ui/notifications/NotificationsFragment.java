package kg.roman.Mobile_Torgovla.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import kg.roman.Mobile_Torgovla.FormaZakazaNew.ui.FormaZakaza_End.ViewModel_End;
import kg.roman.Mobile_Torgovla.R;

public class NotificationsFragment extends Fragment {

    private ViewModel_End notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ViewModel_End.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
/*        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}