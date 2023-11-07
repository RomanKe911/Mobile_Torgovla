package kg.roman.Mobile_Torgovla.XML_Files;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_SubBrends_ResourceParser {
    private ArrayList<MTW_SubBrends> subBrends;

    public MTW_SubBrends_ResourceParser(){
        subBrends = new ArrayList<>();
    }

    public ArrayList<MTW_SubBrends> getSubBrends(){
        return subBrends;
    }

    public boolean parse(XmlPullParser xpp){
        boolean status = true;
        MTW_SubBrends currentUser = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("BrendsSync".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentUser = new MTW_SubBrends();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("BrendsSync".equalsIgnoreCase(tagName)){
                                subBrends.add(currentUser);
                                inEntry = false;
                            } else if("name".equalsIgnoreCase(tagName)){
                                currentUser.setName(textValue);
                            } else if("code".equalsIgnoreCase(tagName)){
                                currentUser.setKod(textValue);
                            }else if("perentcode".equalsIgnoreCase(tagName)){
                                currentUser.setParents_kod(textValue);
                            }else if("prefix".equalsIgnoreCase(tagName)){
                                currentUser.setPreficd(textValue);
                            }else if("up".equalsIgnoreCase(tagName)){
                                currentUser.setUp(textValue);
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