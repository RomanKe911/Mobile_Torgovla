package kg.roman.Mobile_Torgovla.FTP;

/**
 * Created by user on 03.02.2016.
 */
public class ListAdapterSimple_Pref_Ftp {

    public String server;
    public String port;
    public String login;
    public String password;
    public String put;





    public ListAdapterSimple_Pref_Ftp(String server, String port, String login, String password, String put) {
        this.server = server;
        this.port = port;
        this.login = login;
        this.password = password;
        this.put = put;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }


    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPut() {
        return put;
    }

    public void setPut(String put) {
        this.put = put;
    }






}
