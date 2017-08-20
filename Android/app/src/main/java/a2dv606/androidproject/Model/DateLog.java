package a2dv606.androidproject.Model;

import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Abeer on 3/22/2017.
 */

public class DateLog  {


    private Long ID;
    private  int waterNeed;
    private int waterDrunk;
    private String date ;


    public String getDate(){return date;}

    public  void setDate(String date) {
        this.date = date;
    }

    public int getWaterDrunk() {return waterDrunk;}

    public void setWaterDrunk(int waterDrunk) {
        this.waterDrunk = waterDrunk;
    }

    public int getWaterNeed() {return waterNeed;}

    public Double getWaterInLiter(int water){
    double w= (double) water;
            return w/1000.0;
    }
    public int getWaterInMLiter(int water){
        return water*1000;
    }
    public void setWaterNeed(int waterNeed) {
        this.waterNeed = waterNeed;
    }



    public void setID(Long ID) {
        this.ID =  ID;
    }
    public Long getID() {
        return ID;
    }
}
