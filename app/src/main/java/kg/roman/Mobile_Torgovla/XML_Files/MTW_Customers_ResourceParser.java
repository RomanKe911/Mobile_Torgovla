package kg.roman.Mobile_Torgovla.XML_Files;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_Customers_ResourceParser {
    private ArrayList<MTW_Customers> customers;

    public MTW_Customers_ResourceParser(){
        customers = new ArrayList<>();
    }

    public ArrayList<MTW_Customers> getCustomers(){
        return customers;
    }

    public boolean parse(XmlPullParser xpp){
        boolean status = true;
        MTW_Customers currentUser = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("CustomerSync".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentUser = new MTW_Customers();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("CustomerSync".equalsIgnoreCase(tagName)){
                                customers.add(currentUser);
                                inEntry = false;
                            } else if("UID".equalsIgnoreCase(tagName)){
                                currentUser.setUid_k_agent(textValue);
                            } else if("Name".equalsIgnoreCase(tagName)){
                                currentUser.setK_agent(textValue);
                            }else if("Adress".equalsIgnoreCase(tagName)){
                                currentUser.setAdress(textValue);
                            }else if("UserUID".equalsIgnoreCase(tagName)){
                                currentUser.setUid_agent(textValue);
                            }else if("RoadUID".equalsIgnoreCase(tagName)){
                                currentUser.setReaduid(textValue);
                            }else if("RoadName".equalsIgnoreCase(tagName)){
                                currentUser.setRoadname(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        }
        catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return  status;
    }
}