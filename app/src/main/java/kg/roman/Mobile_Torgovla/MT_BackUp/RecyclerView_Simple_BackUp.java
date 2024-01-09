package kg.roman.Mobile_Torgovla.MT_BackUp;

/**
 * Created by user on 03.02.2016.
 */
public class RecyclerView_Simple_BackUp {

    public String BackUp_AgentName;
    public String BackUp_AgentRegion;
    public String BackUp_RNDB_Size;
    public String BackUp_CONSTDB_Size;
    public String BackUp_BASEDB_Size;


    public RecyclerView_Simple_BackUp(String BackUp_AgentName, String BackUp_AgentRegion,
                                      String BackUp_RNDB_Size, String BackUp_CONSTDB_Size,
                                      String BackUp_BASEDB_Size) {
        this.BackUp_AgentName = BackUp_AgentName;
        this.BackUp_AgentRegion = BackUp_AgentRegion;
        this.BackUp_RNDB_Size = BackUp_RNDB_Size;
        this.BackUp_CONSTDB_Size = BackUp_CONSTDB_Size;
        this.BackUp_BASEDB_Size = BackUp_BASEDB_Size;
    }


    public String getBackUp_AgentName() {
        return BackUp_AgentName;
    }

    public void setBackUp_AgentName(String BackUp_AgentName) {
        this.BackUp_AgentName = BackUp_AgentName;
    }

    public String getBackUp_AgentRegion() {
        return BackUp_AgentRegion;
    }

    public void setBackUp_AgentRegion(String BackUp_AgentRegion) {
        this.BackUp_AgentRegion = BackUp_AgentRegion;
    }

    public String getBackUp_RNDB_Size() {
        return BackUp_RNDB_Size;
    }

    public void setBackUp_RNDB_Size(String BackUp_RNDB_Size) {
        this.BackUp_RNDB_Size = BackUp_RNDB_Size;
    }

    public String getBackUp_CONSTDB_Size() {
        return BackUp_CONSTDB_Size;
    }

    public void setBackUp_CONSTDB_Size(String BackUp_CONSTDB_Size) {
        this.BackUp_CONSTDB_Size = BackUp_CONSTDB_Size;
    }

    public String getBackUp_BASEDB_Size() {
        return BackUp_BASEDB_Size;
    }

    public void setBackUp_BASEDB_Size(String BackUp_BASEDB_Size) {
        this.BackUp_BASEDB_Size = BackUp_BASEDB_Size;
    }

}
