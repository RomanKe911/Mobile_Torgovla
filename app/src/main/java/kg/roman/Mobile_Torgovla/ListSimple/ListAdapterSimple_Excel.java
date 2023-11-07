package kg.roman.Mobile_Torgovla.ListSimple;

public class ListAdapterSimple_Excel
{
    public Double cena;
    public String kod;
    public Integer kol;
    public String name;

    public ListAdapterSimple_Excel(String paramString1, String paramString2, Double paramDouble, Integer paramInteger)
    {
        this.kod = paramString1;
        this.name = paramString2;
        this.cena = paramDouble;
        this.kol = paramInteger;
    }

    public Double getCena()
    {
        return this.cena;
    }

    public String getKod()
    {
        return this.kod;
    }

    public Integer getKol()
    {
        return this.kol;
    }

    public String getName()
    {
        return this.name;
    }

    public void setCena(Double paramDouble)
    {
        this.cena = paramDouble;
    }

    public void setKod(String paramString)
    {
        this.kod = paramString;
    }

    public void setKol(Integer paramInteger)
    {
        this.kol = paramInteger;
    }

    public void setName(String paramString)
    {
        this.name = paramString;
    }
}