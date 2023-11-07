package kg.roman.Mobile_Torgovla.XML_Files;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_Price_ResourceParser {
    private ArrayList<MTW_Price> price;

    public MTW_Price_ResourceParser(){
        price = new ArrayList<>();
    }

    public ArrayList<MTW_Price> getPrice(){
        return  price;
    }

    public boolean parse(XmlPullParser xpp){
        boolean status = true;
        MTW_Price currentUser = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("PriceSync".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentUser = new MTW_Price();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("PriceSync".equalsIgnoreCase(tagName)){
                                price.add(currentUser);
                                inEntry = false;
                            } else if("NomenclatureUID".equalsIgnoreCase(tagName)){
                                currentUser.setUid(textValue);
                            } else if("Price".equalsIgnoreCase(tagName)){
                                currentUser.setCena(textValue);
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