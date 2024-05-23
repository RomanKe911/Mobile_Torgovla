package kg.roman.Mobile_Torgovla.MT_MyClassSetting;

import android.util.Log;
import android.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

public class CalendarThis {
    public String getThis_DateFormatDisplay, getThis_DateFormatSqlDB, getThis_DateFormatXML, getThis_DateFormatCountTovar,
            getThis_DateFormatVrema, getThis_DateFormatAllSqlDB, getThis_DateFormatAllDisplay;
    public int Year, Month, Day;
    public long getThis_DateFormatAllSqlDBLong;
    private String loge_TAG = "ClassCalendar";
    public CalendarThis() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormatDisplay = new SimpleDateFormat("dd:MM:yyyy");
        SimpleDateFormat dateFormatSqlDB = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormatXML = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateFormatTovarCount = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormatVrema = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormatAllDisplay = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        getThis_DateFormatDisplay = dateFormatDisplay.format(calendar.getTime());                   /// формат даты: dd:MM:yyyy - дата для отображения в рабочей зоне(дисплей)
        getThis_DateFormatAllDisplay = dateFormatAllDisplay.format(calendar.getTime());                   /// формат даты: dd:MM:yyyy HH:mm:ss - дата для отображения в рабочей зоне(дисплей)
        getThis_DateFormatSqlDB = dateFormatSqlDB.format(calendar.getTime());                       /// формат даты: yyyy-MM-dd - дата для отображения сортировки (база данных)
        getThis_DateFormatXML = dateFormatXML.format(calendar.getTime());                           /// формат даты: dd.MM.yyyy - дата для сохранения в формате XML
        getThis_DateFormatCountTovar = dateFormatTovarCount.format(calendar.getTime());             /// формат даты: dd/MM/yyyy - дата для сохранения в формате XML
        getThis_DateFormatVrema = dateFormatVrema.format(calendar.getTime());                       /// формат даты: HH:mm:ss - формат времени
        getThis_DateFormatAllSqlDB = dateFormatAll.format(calendar.getTime());                      /// формат даты: yyyy-MM-dd HH:mm:ss полный формат для SQLite
        getThis_DateFormatAllSqlDBLong = (calendar.getTime().getTime());                      /// формат даты: yyyy-MM-dd HH:mm:ss полный формат для SQLite

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

/*        Log.e(loge_TAG, "Format Display: " + getThis_DateFormatDisplay);
        Log.e(loge_TAG, "Format DB: " + getThis_DateFormatSqlDB);
        Log.e(loge_TAG, "Format XML: " + getThis_DateFormatXML);
        Log.e(loge_TAG, "Format Vrema: " + getThis_DateFormatVrema);*/
    }

    //////  Получение имени дня недели
    public Pair<String, String> CalendarDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        String CalendarDayOfWeek = "";
        String CalendarDayOfWeekText = "";
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY: {
                CalendarDayOfWeekText = "Понедельник";
                CalendarDayOfWeek = "01_WDay";
            }
            break;
            case Calendar.TUESDAY: {
                CalendarDayOfWeekText = "Вторник";
                CalendarDayOfWeek = "02_WDay";
            }
            break;
            case Calendar.WEDNESDAY: {
                CalendarDayOfWeekText = "Среда";
                CalendarDayOfWeek = "03_WDay";
            }
            break;
            case Calendar.THURSDAY: {
                CalendarDayOfWeekText = "Четверг";
                CalendarDayOfWeek = "04_WDay";
            }
            break;
            case Calendar.FRIDAY: {
                CalendarDayOfWeekText = "Пятница";
                CalendarDayOfWeek = "05_WDay";
            }
            break;
            case Calendar.SATURDAY: {
                CalendarDayOfWeekText = "Суббота";
                CalendarDayOfWeek = "06_WDay";
            }
            break;
            case Calendar.SUNDAY: {
                CalendarDayOfWeekText = "Воскресенье";
                CalendarDayOfWeek = "07_WDay";
            }
            break;
            default:
                break;
        }
        Log.e("ClassCalendar", "DayWeek: " + CalendarDayOfWeekText);
        return new Pair<>(CalendarDayOfWeek, CalendarDayOfWeekText);
    }

    //////  Получение строки для ActionBar рабочего стола
    public String CalendarofDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String messege = "";
        for (int am = 0; am < 6; am++) if (hour == am) messege = "Доброй ночи!";
        for (int am = 6; am <= 11; am++) if (hour == am) messege = "Доброе утро!";
        for (int am = 12; am <= 16; am++) if (hour == am) messege = "Добрый день!";
        for (int am = 17; am <= 20; am++) if (hour == am) messege = "Добрый вечер!";
        for (int am = 21; am < 24; am++) if (hour == am) messege = "Доброй ночи!";
        return messege;
    }

    public String getDateToNextDeveloper()
    {
        Calendar c = Calendar.getInstance();
        AtomicInteger mYear = new AtomicInteger();
        AtomicInteger mMonth = new AtomicInteger();
        AtomicInteger mDay = new AtomicInteger();
        mYear.set(c.get(Calendar.YEAR));
        mMonth.set(c.get(Calendar.MONTH));
        mDay.set(c.get(Calendar.DAY_OF_MONTH) + 1);
        c.set(mYear.get(), mMonth.get(), mDay.get());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String data_start = dateFormat.format(c.getTime());
        return data_start;
    }




    //// Конвертация даты файлы из String в Long(дата и время) (WORK)
    public Long getFullTimeStringToLong(String timeString) {
        // Формат строки 09:01:2024 09:37:21
        Calendar calendar = new GregorianCalendar();
        long returnLong;
        try {
            String firstString = timeString.substring(0, timeString.indexOf(" "));
            String secondString = timeString.substring(timeString.lastIndexOf(" "));
            int day = Integer.parseInt(firstString.substring(0, firstString.indexOf(":")).trim());
            int month = Integer.parseInt(firstString.substring(firstString.indexOf(":") + 1, firstString.lastIndexOf(":")).trim()) - 1;
            int year = Integer.parseInt(firstString.substring(firstString.lastIndexOf(":") + 1).trim());
            int hour = Integer.parseInt(secondString.substring(0, secondString.indexOf(":")).trim());
            int minute = Integer.parseInt(secondString.substring(secondString.indexOf(":") + 1, secondString.lastIndexOf(":")).trim());
            int second = Integer.parseInt(secondString.substring(secondString.lastIndexOf(":") + 1).trim());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, second);
            calendar.set(Calendar.MILLISECOND, 0);
            //  Log.e("TimeZoneData: ", "Long: " + calendar.getTimeInMillis() + " Da " + calendar.getTime());
            returnLong = calendar.getTimeInMillis();
        } catch (Exception e) {
            Log.e(loge_TAG, "Ошибка выполнения конвертации даты в формат long");
            returnLong = 0L;
        }
        return returnLong;
    }

}



/*        String this_rn_vrema, this_rn_year, this_rn_month, this_rn_day;
        DateFormat df_data = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_vrema = new SimpleDateFormat("HH:mm:ss");
        DateFormat df_year = new SimpleDateFormat("yyyy");
        DateFormat df_month = new SimpleDateFormat("MM");
        DateFormat df_day = new SimpleDateFormat("dd");

        this_rn_data = df_data.format(Calendar.getInstance().getTime());
        this_rn_vrema = df_vrema.format(Calendar.getInstance().getTime());
        this_rn_year = df_year.format(Calendar.getInstance().getTime());
        this_rn_month = df_month.format(Calendar.getInstance().getTime());
        this_rn_day = df_day.format(Calendar.getInstance().getTime());





        String dateString_NOW = dateFormatDDMMYYYY.format(calendar.getTime());
        String dateString_WORK = dateFormatYYYYMMDD.format(calendar.getTime());
        this_data_now = dateString_NOW + " " + this_rn_vrema;
        Log.e("ClassCalendar", "!DataStart:" + dateString_NOW);

        SimpleDateFormat dateFormat_display = new SimpleDateFormat("dd-MM-yyyy");  // формат для экрана даты
        SimpleDateFormat dateFormat_filter = new SimpleDateFormat("yyyy-MM-dd");  // формат для сортировки даты
        String Data_Display_now = dateFormat_display.format(calendar.getTime());
        String Data_filter_now = dateFormat_filter.format(calendar.getTime());

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        String dateDataFormat = dateText + " " + timeText;

        this_data_now = Data_Display_now;
*/

/*        getThis_DateFormatDDMMYYYY3 = dateFormatDDMMYYYY3.format(calendar.getTime());               /// формат даты: dd-MM-yyyy
        getThis_DateFormatYYYYMMDDHHMMSS = dateFormatYYYYMMDDHHMMSS.format(calendar.getTime());     /// формат даты: yyyy-MM-dd HH:mm:ss
                SimpleDateFormat dateFormatDDMMYYYY3 = new SimpleDateFormat("dd-MM-yyyy");


        SimpleDateFormat dateFormatYYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
