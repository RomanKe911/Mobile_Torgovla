package kg.roman.Mobile_Torgovla.XML_Files;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_Users_ResourceParser {
    private ArrayList<MTW_Users> users;

    public MTW_Users_ResourceParser(){
        users = new ArrayList<>();
    }

    public ArrayList<MTW_Users> getUsers(){
        return  users;
    }

    public boolean parse(XmlPullParser xpp){
        boolean status = true;
        MTW_Users currentUser = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("User".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentUser = new MTW_Users();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("User".equalsIgnoreCase(tagName)){
                                users.add(currentUser);
                                inEntry = false;
                            } else if("UID".equalsIgnoreCase(tagName)){
                                currentUser.setUid_name(textValue);
                            } else if("Name".equalsIgnoreCase(tagName)){
                                currentUser.setName(textValue);
                            }else if("region".equalsIgnoreCase(tagName)){
                                currentUser.setUid_region(textValue);
                            }else if("cena".equalsIgnoreCase(tagName)){
                                currentUser.setCena(textValue);
                            }else if("uid_sklad".equalsIgnoreCase(tagName)){
                                currentUser.setUid_sklad(textValue);
                            }else if("sklad".equalsIgnoreCase(tagName)){
                                currentUser.setSklad(textValue);
                            }else if("kod_mobile".equalsIgnoreCase(tagName)){
                                currentUser.setKod_mobile(textValue);
                            }else if("type_real".equalsIgnoreCase(tagName)){
                                currentUser.setType_real(textValue);
                            }else if("type_user".equalsIgnoreCase(tagName)){
                                currentUser.setType_user(textValue);
                            }else if("name_region".equalsIgnoreCase(tagName)){
                                currentUser.setPutAg(textValue);
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