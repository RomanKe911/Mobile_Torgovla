package kg.roman.Mobile_Torgovla.XML_Files;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class MTW_File_Const_ResourceParser {
    private ArrayList<MTW_File_Const> xml_const;

    public MTW_File_Const_ResourceParser(){
        xml_const = new ArrayList<>();
    }

    public ArrayList<MTW_File_Const> getXml_const(){
        return  xml_const;
    }

    public boolean parse(XmlPullParser xpp){
        boolean status = true;
        MTW_File_Const currentUser = null;
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
                            currentUser = new MTW_File_Const();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("PriceSync".equalsIgnoreCase(tagName)){ // Нужно смотреть из файла!!!!
                                xml_const.add(currentUser);
                                inEntry = false;
                            } else if("".equalsIgnoreCase(tagName)){
                                currentUser.setUid_user(textValue);
                            } else if("".equalsIgnoreCase(tagName)){
                                currentUser.setName(textValue);
                            }else if("".equalsIgnoreCase(tagName)){
                                currentUser.setPass(textValue);
                            }else if("".equalsIgnoreCase(tagName)){
                                currentUser.setRegion(textValue);
                            }else if("".equalsIgnoreCase(tagName)){
                                currentUser.setUid_region(textValue);
                            }else if("".equalsIgnoreCase(tagName)){
                                currentUser.setCena(textValue);
                            }else if("".equalsIgnoreCase(tagName)){
                                currentUser.setSklad(textValue);
                            }else if("".equalsIgnoreCase(tagName)){
                                currentUser.setKod_mobile(textValue);
                            }else if("".equalsIgnoreCase(tagName)){
                                currentUser.setType_real(textValue);
                            }else if("".equalsIgnoreCase(tagName)){
                                currentUser.setType_user(textValue);
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