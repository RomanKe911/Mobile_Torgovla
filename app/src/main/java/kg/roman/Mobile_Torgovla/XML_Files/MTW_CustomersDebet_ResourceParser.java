package kg.roman.Mobile_Torgovla.XML_Files;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_CustomersDebet_ResourceParser {
    private ArrayList<MTW_CustomersDebet> debet;

    public MTW_CustomersDebet_ResourceParser() {
        debet = new ArrayList<>();
    }

    public ArrayList<MTW_CustomersDebet> getCustomersDebet() {
        return debet;
    }

    public boolean parse(XmlPullParser xpp) {
        boolean status = true;
        MTW_CustomersDebet currentUser = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        textValue = "";
                        if ("CustomerSync".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentUser = new MTW_CustomersDebet();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("CustomerSync".equalsIgnoreCase(tagName)) {
                                //Double.parseDouble("sum_debet".replaceAll(",", ".")) > 0
                                debet.add(currentUser);
                                inEntry = false;
                                textValue = "";
                            } else if ("Agent".equalsIgnoreCase(tagName)) {
                                currentUser.setAgent(textValue);
                            } else if ("uid_agent".equalsIgnoreCase(tagName)) {
                                currentUser.setUid_agent(textValue);
                            } else if ("Customer".equalsIgnoreCase(tagName)) {
                                currentUser.setCustomer(textValue);
                            } else if ("uid_customer".equalsIgnoreCase(tagName)) {
                                currentUser.setUid_customer(textValue);
                            } else if ("sum_debet".equalsIgnoreCase(tagName)) {
                                currentUser.setSum_debet(textValue);
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