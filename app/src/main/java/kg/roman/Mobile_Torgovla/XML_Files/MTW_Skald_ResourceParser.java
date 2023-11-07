package kg.roman.Mobile_Torgovla.XML_Files;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_Skald_ResourceParser {
    private ArrayList<MTW_Sklad> sklads;

    public MTW_Skald_ResourceParser() {
        sklads = new ArrayList<>();
    }

    public ArrayList<MTW_Sklad> getSklads() {
        return sklads;
    }

    public boolean parse(XmlPullParser xpp) {
        boolean status = true;
        MTW_Sklad currentUser = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("WarehouseSync".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentUser = new MTW_Sklad();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("WarehouseSync".equalsIgnoreCase(tagName)) {
                                sklads.add(currentUser);
                                inEntry = false;
                            } else if ("UID".equalsIgnoreCase(tagName)) {
                                currentUser.setSklad_uid(textValue);
                            } else if ("Name".equalsIgnoreCase(tagName)) {
                                currentUser.setSklad_name(textValue);
                            }

                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}