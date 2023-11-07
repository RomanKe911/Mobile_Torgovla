package kg.roman.Mobile_Torgovla.XML_Files;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_Nomenclatures_ResourceParser {
    private ArrayList<MTW_Nomenclatures> tovar;

    public MTW_Nomenclatures_ResourceParser() {
        tovar = new ArrayList<>();
    }

    public ArrayList<MTW_Nomenclatures> getTovar() {
        return tovar;
    }

    public boolean parse(XmlPullParser xpp) {
        boolean status = true;
        MTW_Nomenclatures currentUser = null;
        boolean inEntry = false;
        String textValue = "";
        Integer k = 0;

        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("Nomenclature".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentUser = new MTW_Nomenclatures();
                            //   Log.e("SQL", "True " + xpp.getName() + " " + xpp.getAttributeCount() + "" + xpp.getDepth());
                            //  Log.e("SQL", "True " + xpp.getText());
                        } //else Log.e("SQL", "False " + xpp.getName());
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();

                        /*if("Nomenclature".equalsIgnoreCase(tagName)){
                            Log.e("SQL", "True"+textValue);
                        }else Log.e("SQL", "False"+textValue);*/

                        break;
                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("Nomenclature".equalsIgnoreCase(tagName)) {
                                tovar.add(currentUser);
                                inEntry = false;
                            } else if ("kod_univ".equalsIgnoreCase(tagName)) {
                                currentUser.setKod_univ(textValue);
                            } else if ("koduid".equalsIgnoreCase(tagName)) {
                                currentUser.setKoduid(textValue);
                            } else if ("kod".equalsIgnoreCase(tagName)) {
                                currentUser.setKod(textValue);
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                currentUser.setName(textValue);
                            } else if ("brends".equalsIgnoreCase(tagName)) {
                                currentUser.setBrends(textValue);
                            } else if ("p_group".equalsIgnoreCase(tagName)) {
                                currentUser.setP_group(textValue);
                            } else if ("kol_box".equalsIgnoreCase(tagName)) {
                                currentUser.setKolbox(textValue);
                            } else if ("cena".equalsIgnoreCase(tagName)) {
                                currentUser.setCena(textValue);
                            } else if ("strih".equalsIgnoreCase(tagName)) {
                                currentUser.setStrih(textValue);
                            }

                        }
                        break;
                    default:

                }
                eventType = xpp.next();
                // k=0;
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}







                            /*if("Nomenclature".equalsIgnoreCase(tagName)){
                                tovar.add(currentUser);
                                inEntry = false;
                            } else if("brends".equalsIgnoreCase(tagName)){
                                currentUser.setBrends(textValue);
                            } else if("p_group".equalsIgnoreCase(tagName)){
                                currentUser.setP_group(textValue);
                            } else if("kod".equalsIgnoreCase(tagName)){
                                currentUser.setKod(textValue);
                            }else if("name".equalsIgnoreCase(tagName)){
                                currentUser.setName(textValue);
                            }else if("cena".equalsIgnoreCase(tagName)){
                                currentUser.setCena(textValue);
                            }else if("strih".equalsIgnoreCase(tagName)){
                                currentUser.setStrih(textValue);
                            }else if("kod_univ".equalsIgnoreCase(tagName)){
                                currentUser.setKod_univ(textValue);
                            }else if("koduid".equalsIgnoreCase(tagName)){
                                currentUser.setKoduid(textValue);
                            }else if("kol_box".equalsIgnoreCase(tagName)){
                                currentUser.setKolbox(textValue);
                            }else if("/".equalsIgnoreCase(tagName)){
                                currentUser.setCena("Пусто");
                            }else if(" ".equalsIgnoreCase(tagName)){
                                currentUser.setCena("Пусто");
                            }*/