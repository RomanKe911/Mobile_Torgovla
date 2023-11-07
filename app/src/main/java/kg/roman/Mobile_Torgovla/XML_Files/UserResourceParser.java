package kg.roman.Mobile_Torgovla.XML_Files;

import org.xmlpull.v1.XmlPullParser;
import java.util.ArrayList;


public class UserResourceParser {
    private ArrayList<User> users;

    public UserResourceParser(){
        users = new ArrayList<>();
    }

    public ArrayList<User> getUsers(){
        return  users;
    }

    public boolean parse(XmlPullParser xpp){
        boolean status = true;
        User currentUser = null;
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
                            currentUser = new User();
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
                            } else if("name".equalsIgnoreCase(tagName)){
                                currentUser.setName(textValue);
                            } else if("uid".equalsIgnoreCase(tagName)){
                                currentUser.setUID(textValue);
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