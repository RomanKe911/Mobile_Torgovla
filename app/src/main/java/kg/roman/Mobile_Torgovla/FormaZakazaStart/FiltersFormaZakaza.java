package kg.roman.Mobile_Torgovla.FormaZakazaStart;


import android.content.Context;

import kg.roman.Mobile_Torgovla.MT_FTP.CalendarThis;
import kg.roman.Mobile_Torgovla.MT_FTP.PreferencesWrite;

interface InterfaceFilters {
    String filterDate();

    String filterClient();

    String filterAdmin();

    String filterAll();

    String filterNull();

    String filterGroup();
}


public class FiltersFormaZakaza implements InterfaceFilters {
    Context thisContext;
    PreferencesWrite preferencesWrite;
    String agentAdmin, agentUID, clientUID, DateStart, DateEnd, DateThis;
    CalendarThis calendarThis;

    FiltersFormaZakaza(Context context) {
        this.thisContext = context;
        preferencesWrite = new PreferencesWrite(context);
        calendarThis = new CalendarThis();
        agentAdmin = preferencesWrite.PEREM_ANDROID_ID_ADMIN;
        agentUID = preferencesWrite.Setting_AG_UID;
        clientUID = preferencesWrite.Setting_Filters_Clients_UID;
        DateStart = preferencesWrite.Setting_Filters_DataStart;
        DateEnd = preferencesWrite.Setting_Filters_DataEND;
        DateThis = calendarThis.getThis_DateFormatSqlDB;
    }


    @Override
    public String filterGroup() {
        String query;
        //// Данные без фильтров
        query = "SELECT agent_name, agent_uid, k_agn_uid, k_agn_name, k_agn_adress, data, SUM(itogo) AS 'itogo' " +
                "FROM base_RN_All " +
                "WHERE agent_uid = '" + agentUID + "'" +
                "AND data BETWEEN '" + DateThis + "' AND '" + DateThis + "'" +
                "GROUP BY k_agn_uid " +
                "ORDER BY k_agn_name;";

        //// Фильриция по Клиенту
        if (preferencesWrite.Setting_FiltersSelectClients)
            query = "SELECT agent_name, agent_uid, k_agn_uid, k_agn_name, k_agn_adress, data, SUM(itogo) AS 'itogo' " +
                    "FROM base_RN_All " +
                    "WHERE agent_uid = '" + agentUID + "'" +
                    "AND k_agn_uid = '" + clientUID + "'" +
                    "AND data BETWEEN '" + DateThis + "' AND '" + DateThis + "'" +
                    "GROUP BY k_agn_uid ;";


        //// Фильтрация по дате
        if (preferencesWrite.Setting_FiltersSelectDate)
            query = "SELECT agent_name, agent_uid, k_agn_uid, k_agn_name, k_agn_adress, data, SUM(itogo) AS 'itogo' " +
                    "FROM base_RN_All " +
                    "WHERE agent_uid = '" + agentUID + "'" +
                    "AND data BETWEEN '" + DateStart + "' AND '" + DateEnd + "'" +
                    "GROUP BY k_agn_uid " +
                    "ORDER BY k_agn_name;";

        //// Фильтрицмя по дате и клиентам
        if (preferencesWrite.Setting_FiltersSelectDate && preferencesWrite.Setting_FiltersSelectClients)
            query = "SELECT agent_name, agent_uid, k_agn_uid, k_agn_name, k_agn_adress, data, SUM(itogo) AS 'itogo' " +
                    "FROM base_RN_All " +
                    "WHERE agent_uid = '" + agentUID + "'" +
                    "AND k_agn_uid = '" + clientUID + "'" +
                    "AND data BETWEEN '" + DateStart + "' AND '" + DateEnd + "'" +
                    "GROUP BY k_agn_uid;";

        return query;
    }


    //// Данные без фильтров
    @Override
    public String filterNull() {
        return "SELECT * FROM base_RN_All\n" +
                "WHERE agent_uid = '" + agentUID + "' " +
                "AND base_RN_All.data BETWEEN '" + DateThis + "' " + "AND '" + DateThis + "'";
    }

    //// Фильтрация по дате
    @Override
    public String filterDate() {
        return "SELECT * FROM base_RN_All\n" +
                "WHERE agent_uid = '" + agentUID + "'" +
                "AND base_RN_All.data BETWEEN '" + DateStart + "' AND '" + DateEnd + "'";
    }

    //// Фильриция по Клиенту
    @Override
    public String filterClient() {
        return "SELECT * FROM base_RN_All\n" +
                "WHERE agent_uid = '" + agentUID + "'" +
                "AND k_agn_uid = '" + clientUID + "' " +
                "AND base_RN_All.data BETWEEN '" + DateThis + "' AND '" + DateThis + "'";
    }

    //// Фильтрицмя по дате и клиентам
    @Override
    public String filterAll() {
        return "SELECT * FROM base_RN_All\n" +
                "WHERE agent_uid = '" + agentUID + "'" +
                "AND k_agn_uid = '" + clientUID + "' " +
                "AND base_RN_All.data BETWEEN '" + DateStart + "' AND '" + DateEnd + "'";
    }

    //// Фильтры для админа
    @Override
    public String filterAdmin() {
        String query;
        //// Данные без фильтров
        query = "SELECT * FROM base_RN_All WHERE data BETWEEN '" + DateThis + "' AND '" + DateThis + "';";
        //// Фильриция по Клиенту
        if (preferencesWrite.Setting_FiltersSelectClients)
            query = "SELECT * FROM base_RN_All\n" +
                    "WHERE k_agn_uid = '" + clientUID + "' " +
                    "AND data BETWEEN '" + DateThis + "' AND '" + DateThis + "'";
        //// Фильтрация по дате
        if (preferencesWrite.Setting_FiltersSelectDate)
            query = "SELECT * FROM base_RN_All\n" +
                    "WHERE data BETWEEN '" + DateStart + "' AND '" + DateEnd + "'";
        //// Фильтрицмя по дате и клиентам
        if (preferencesWrite.Setting_FiltersSelectDate && preferencesWrite.Setting_FiltersSelectClients)
            query = "SELECT * FROM base_RN_All\n" +
                    "WHERE k_agn_uid = '" + clientUID + "' " +
                    "AND data BETWEEN '" + DateStart + "' AND '" + DateEnd + "'";
        return query;
    }

}
