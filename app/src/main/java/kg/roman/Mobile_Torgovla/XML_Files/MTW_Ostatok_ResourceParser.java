package kg.roman.Mobile_Torgovla.XML_Files;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_Ostatok_ResourceParser {
    private ArrayList<MTW_Ostatok> ostatok;

    public MTW_Ostatok_ResourceParser(){
        ostatok = new ArrayList<>();
    }

    public ArrayList<MTW_Ostatok> getOstatok(){
        return  ostatok;
    }

    public boolean parse(XmlPullParser xpp){
        boolean status = true;
        MTW_Ostatok currentUser = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("ResidueGoodsSync".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentUser = new MTW_Ostatok();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("ResidueGoodsSync".equalsIgnoreCase(tagName)){
                                ostatok.add(currentUser);
                                inEntry = false;
                            } else if("Date".equalsIgnoreCase(tagName)){
                                currentUser.setData(textValue);
                            } else if("SkladUID".equalsIgnoreCase(tagName)){
                                currentUser.setSklad_uid(textValue);
                            }else if("NomenclatureUID".equalsIgnoreCase(tagName)){
                                currentUser.setName_uid(textValue);
                            }else if("Name".equalsIgnoreCase(tagName)){
                                currentUser.setName(textValue);
                            }else if("Count".equalsIgnoreCase(tagName)){
                                currentUser.setCount(textValue);
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