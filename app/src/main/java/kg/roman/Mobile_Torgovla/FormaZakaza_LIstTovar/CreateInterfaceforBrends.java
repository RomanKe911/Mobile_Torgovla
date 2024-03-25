package kg.roman.Mobile_Torgovla.FormaZakaza_LIstTovar;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import kg.roman.Mobile_Torgovla.R;

interface TestInterface {
    void firmaTradeGof();

    void firmaSunbell();

    void firmaFamaly();

    void firmaLavr();
}

public class CreateInterfaceforBrends implements TestInterface {

    Context context;
    String brend;
    View navHeader;
    Toolbar toolbar;
    TextView textView_header1, textView_header2;
    ImageView imageView_header;
    LinearLayout linearLayout;
    Window window;


    CreateInterfaceforBrends(Context context, String brends, View navView, Toolbar toolbar, Window window) {
        this.context = context;
        this.brend = brends;
        this.navHeader = navView;
        this.toolbar = toolbar;
        this.window = window;

        linearLayout = navHeader.findViewById(R.id.lineary_header);
        imageView_header = navHeader.findViewById(R.id.nav_header_nomenclature);
        textView_header1 = navHeader.findViewById(R.id.tvw_nav_headerTitle);
        textView_header2 = navHeader.findViewById(R.id.tvw_nav_headerText);
        textView_header2 = navHeader.findViewById(R.id.tvw_nav_headerText);
        TextView tvw_headerPref = navHeader.findViewById(R.id.tvw_nav_headerPref);
        textView_header1.setTextSize(12);
        textView_header2.setTextSize(10);
        tvw_headerPref.setVisibility(View.GONE);
    }

    //// Интерфейс для фирмы TradeGof
    @Override
    public void firmaTradeGof() {
        if (brend.equals("Bella")) {
            //  toolbar.setBackgroundColor(Color.alpha(R.color.bella_colorPrimary));
            toolbar.setBackgroundColor(Color.parseColor("#5f5b8d"));
            imageView_header.setImageResource(R.drawable.logo_bella);
            textView_header1.setText(R.string.header_bella_title_1);
            textView_header2.setText(R.string.header_bella_title_2);
            linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_bella));
        }
    }

    //// Интерфейс для фирмы Sunbell
    @Override
    public void firmaSunbell() {
        switch (brend) {
            case "Cussons": {
                toolbar.setBackgroundColor(Color.parseColor("#91a2b6"));
                imageView_header.setImageResource(R.mipmap.logo_pzcussons);
                textView_header1.setText(R.string.header_cussons_title_1);
                textView_header2.setText(R.string.header_cussons_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_cussons));
            }
            break;
            case "Halk hijyenik urunler": {
                toolbar.setBackgroundColor(Color.parseColor("#26C6DA"));
                imageView_header.setImageResource(R.mipmap.logo_halk);
                textView_header1.setText(R.string.header_halk_title_1);
                textView_header2.setText(R.string.header_halk_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_halk));
            }
            break;
            case "Lacalut": {
                toolbar.setBackgroundColor(Color.parseColor("#e40209"));
                imageView_header.setImageResource(R.mipmap.logo_lacalute);
                textView_header1.setText(R.string.header_lacalute_title_1);
                textView_header2.setText(R.string.header_lacalute_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_lacalute));
            }
            break;
            case "Ontex tuketim urunleri": {
                toolbar.setBackgroundColor(Color.parseColor("#259EFF"));
                imageView_header.setImageResource(R.mipmap.logo_ontex);
                textView_header1.setText(R.string.header_ontex_title_1);
                textView_header2.setText(R.string.header_ontex_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_ontex));
            }
            break;
            case "Pcc": {
                toolbar.setBackgroundColor(Color.parseColor("#fd6004"));
                imageView_header.setImageResource(R.mipmap.logo_flo);
                textView_header1.setText(R.string.header_pcc_title_1);
                textView_header2.setText(R.string.header_pcc_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_pcc));
            }
            break;

            case "Plushe": {
                toolbar.setBackgroundColor(Color.parseColor("#803088"));
                imageView_header.setImageResource(R.mipmap.ic_plushe);
                textView_header1.setText(R.string.header_plushe_1);
                textView_header2.setText(R.string.header_plushe_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_plushe));
            }
            break;
            case "Samarela": {
                toolbar.setBackgroundColor(Color.parseColor("#05aea0"));
                imageView_header.setImageResource(R.mipmap.logo_samarela);
                textView_header1.setText(R.string.header_samarela_title_1);
                textView_header2.setText(R.string.header_samarela_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_samarela));
            }
            break;

            case "Ооо \"биг\"": {
                toolbar.setBackgroundColor(Color.parseColor("#FF7043"));
                imageView_header.setImageResource(R.mipmap.logo_big);
                textView_header1.setText(R.string.header_big_title_1);
                textView_header2.setText(R.string.header_big_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_big));
            }
            break;

            case "Ооо \"коттон клаб\"": {
                toolbar.setBackgroundColor(Color.parseColor("#1976D2"));
                imageView_header.setImageResource(R.mipmap.logo_cotton);
                textView_header1.setText(R.string.header_cottonclub_title_1);
                textView_header2.setText(R.string.header_cottonclub_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_cotton));
            }
            break;

            case "Ооо \"наука, техника, медицина\"": {
                toolbar.setBackgroundColor(Color.parseColor("#1976D2"));
                imageView_header.setImageResource(R.mipmap.logo_ntm);
                textView_header1.setText(R.string.header_nauka_title_1);
                textView_header2.setText(R.string.header_nauka_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_ntm));
            }
            break;

            case "Драмерс": {
                toolbar.setBackgroundColor(Color.parseColor("#194c95"));
                imageView_header.setImageResource(R.mipmap.logo_brait);
                textView_header1.setText(R.string.header_dramers_title_1);
                textView_header2.setText(R.string.header_dramers_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_dramers));
            }
            break;
            case "Первое решение": {
                toolbar.setBackgroundColor(Color.parseColor("#CDB885"));
                imageView_header.setImageResource(R.mipmap.logo_agafia);
                textView_header1.setText(R.string.header_one_work);
                textView_header2.setText(R.string.header_one_work_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_agadia));
            }
            break;

            case "Пкф сонца": {
                toolbar.setBackgroundColor(Color.parseColor("#18bfd7"));
                imageView_header.setImageResource(R.mipmap.logo_soncy);
                textView_header1.setText(R.string.header_pkf_title_1);
                textView_header2.setText(R.string.header_pkf_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_soncy));
            }
            break;
            case "Свобода": {
                toolbar.setBackgroundColor(Color.parseColor("#03a045"));
                imageView_header.setImageResource(R.mipmap.logo_svoboda);
                textView_header1.setText(R.string.header_svoboda_title_1);
                textView_header2.setText(R.string.header_svoboda_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_svoboda));
            }
            break;
        }
    }

    @Override
    public void firmaFamaly() {
        switch (brend) {
            case "Viero (тиссью груп)": {
                toolbar.setBackgroundColor(Color.parseColor("#26A69A"));
                imageView_header.setImageResource(R.drawable.icon_menu_viero_2);
                textView_header1.setText(R.string.header_viero_title_1);
                textView_header2.setText(R.string.header_viero_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_viero));
            }
            break;
            case "Yokosun": {
                toolbar.setBackgroundColor(Color.parseColor("#42A5F5"));
                imageView_header.setImageResource(R.drawable.icon_menu_yokosun);
                textView_header1.setText(R.string.header_yokosun_title_1);
                textView_header2.setText(R.string.header_yokosun_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_yokosun));
            }
            break;

            case "President": {
                toolbar.setBackgroundColor(Color.parseColor("#818181"));
                imageView_header.setImageResource(R.drawable.icon_menu_president);
                textView_header1.setText(R.string.header_president_title_1);
                textView_header2.setText(R.string.header_president_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_president));
            }
            break;

            case "Свитмилк": {
                // Создание оболочки для бренда Свитмилк

            }
            break;

            case "Tori poko": {
                // Создание оболочки для бренда Tori poko
            }
            break;

            case "Ооо \"малби фудс\"": {
                // Создание оболочки для бренда малби фудс
            }
            break;
        }
    }

    @Override
    public void firmaLavr() {
        switch (brend) {
            case "Ооо \"пк \"уфа пак\"": {
                toolbar.setBackgroundColor(Color.parseColor("#FF0000"));
                imageView_header.setImageResource(R.mipmap.logo_ufa);
                textView_header1.setText(R.string.header_ufapack_title_1);
                textView_header2.setText(R.string.header_ufapack_title_2);
                linearLayout.setBackground(getDrawable(context, R.drawable.side_nav_bar_ufapack));
            }
            break;
            case "": {

            }
            break;

        }
    }

}
