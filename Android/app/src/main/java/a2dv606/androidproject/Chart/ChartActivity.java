package a2dv606.androidproject.Chart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.MainWindow.DateHandler;
import a2dv606.androidproject.Model.DateLog;
import a2dv606.androidproject.Model.TimeLog;
import a2dv606.androidproject.R;


public class ChartActivity extends AppCompatActivity {

    BarChart dayBarChart;
    BarChart weekBarChart;
    BarChart mothBarChart;
    BarChart yearBarChart;
    List<TimeLog> dayValues;
    List<DateLog> weekValues;
    List<DateLog> monthValues;
    List<DateLog> yearValues;
    DrinkDataSource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Charts");

        db = new DrinkDataSource(this);
        db.open();
        dayValues = db.getDrinkByDay();
        weekValues = db.getDrinkByWeek();
        monthValues = db.getDrinkByMonth();
        yearValues = db.getDrinkByYear();

        TextView drinkInDay=(TextView) findViewById(R.id.textView);
        TextView drinkInWeek=(TextView) findViewById(R.id.textView2);
        TextView drinkInMonth=(TextView) findViewById(R.id.textView3);
        TextView drinkInYear=(TextView) findViewById(R.id.textView4);

        drinkInDay.setText("Total drink for today :"+DateHandler.dayFormat(db.getCurrentDay()));
        drinkInWeek.setText("Total drink for last week");
        drinkInMonth.setText("Total drink from "+ DateHandler.dAndmFormat(db.getMonth())+"-"+DateHandler.dAndmFormat(db.getCurrentDay()));
        drinkInYear.setText("Total drink from "+DateHandler.monthFormat(db.getYear())+"-"+DateHandler.mAndYFormat(db.getCurrentDay()));





        dayBarChart = (BarChart) findViewById(R.id.day_chart);
        BarDataSet barDataSet = new BarDataSet(getYAxisValues(), "The consumed water in ml");
        BarData barData = new BarData(getXAxisValues(), barDataSet);
        dayBarChart.setData(barData);
        dayBarChart.setDescription("");

        weekBarChart = (BarChart) findViewById(R.id.week_chart);
        BarDataSet weekBarDataSet = new BarDataSet(getYAxisWeekValues(), "The consumed water in ml");
        BarData weekBarData = new BarData(getXAxisWeekValues(), weekBarDataSet);
        weekBarChart.setData(weekBarData);
        weekBarChart.setDescription("");

        mothBarChart = (BarChart) findViewById(R.id.month_chart);
        BarDataSet mBarDataSet = new BarDataSet(getYAxisMonthValues(), "The consumed water in ml");
        BarData mBarData = new BarData(getXAxisMonthValues(), mBarDataSet);
        mothBarChart.setData(mBarData);
        mothBarChart.setDescription("");

        yearBarChart = (BarChart) findViewById(R.id.year_chart);
        BarDataSet yBarDataSet = new BarDataSet(getYAxisYearValues(),"The consumed water in ml");
        BarData yBarData = new BarData(getXAxisYearValues(), yBarDataSet);
        yearBarChart.setData(yBarData);
        yearBarChart.setDescription("");
        yearBarChart.getXAxis().setDrawGridLines(false);
        yBarDataSet.setDrawValues(false);
        yearBarChart.getAxisRight().setDrawLimitLinesBehindData(false);
        yearBarChart.getAxisRight().setDrawAxisLine(false);
        yearBarChart.getAxisRight().setDrawAxisLine(false);
        yearBarChart.getAxisRight().setDrawGridLines(false);


        yearBarChart.getAxisLeft().setDrawAxisLine(false);
        yearBarChart.getAxisLeft().setDrawLimitLinesBehindData(false);
        yearBarChart.getAxisLeft().setDrawAxisLine(false);
        yearBarChart.getAxisLeft().setDrawGridLines(false);

        mothBarChart.getXAxis().setDrawGridLines(false);
        mBarDataSet.setDrawValues(false);
        mothBarChart.getAxisRight().setDrawAxisLine(false);
        mothBarChart.getAxisRight().setDrawLimitLinesBehindData(false);
        mothBarChart.getAxisRight().setDrawAxisLine(false);
        mothBarChart.getAxisRight().setDrawGridLines(false);

        mothBarChart.getAxisLeft().setDrawAxisLine(false);
        mothBarChart.getAxisLeft().setDrawLimitLinesBehindData(false);
        mothBarChart.getAxisLeft().setDrawAxisLine(false);
        mothBarChart.getAxisLeft().setDrawGridLines(false);


        weekBarChart.getXAxis().setDrawGridLines(false);
        weekBarDataSet.setDrawValues(false);
        weekBarChart.getAxisRight().setDrawAxisLine(false);
        weekBarChart.getAxisRight().setDrawLimitLinesBehindData(false);
        weekBarChart.getAxisRight().setDrawAxisLine(false);
        weekBarChart.getAxisRight().setDrawGridLines(false);


        weekBarChart.getAxisLeft().setDrawAxisLine(false);
        weekBarChart.getAxisLeft().setDrawLimitLinesBehindData(false);
        weekBarChart.getAxisLeft().setDrawAxisLine(false);
        weekBarChart.getAxisLeft().setDrawGridLines(false);

        dayBarChart.getXAxis().setDrawGridLines(false);
        barDataSet.setDrawValues(false);
        dayBarChart.getAxisRight().setDrawAxisLine(false);
        dayBarChart.getAxisRight().setDrawLimitLinesBehindData(false);
        dayBarChart.getAxisRight().setDrawAxisLine(false);
        dayBarChart.getAxisRight().setDrawGridLines(false);


        dayBarChart.getAxisLeft().setDrawAxisLine(false);
        dayBarChart.getAxisLeft().setDrawLimitLinesBehindData(false);
        dayBarChart.getAxisLeft().setDrawAxisLine(false);
        dayBarChart.getAxisLeft().setDrawGridLines(false);





        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Today");
        spec.setContent(R.id.linearLayout);
        spec.setIndicator("Today");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("week");
        spec.setContent(R.id.linearLayout2);
        spec.setIndicator(" week");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("month");
        spec.setContent(R.id.linearLayout3);
        spec.setIndicator("month");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("year");
        spec.setContent(R.id.linearLayout4);
        spec.setIndicator("year");
        host.addTab(spec);
    }

    private List<BarEntry> getYAxisYearValues() {
        ArrayList<BarEntry> yAxis = new ArrayList<>();
        for (int i = 0; i < yearValues.size(); i++) {
            BarEntry v1e1 = new BarEntry(yearValues.get(i).getWaterDrunk(), i);
            yAxis.add(v1e1);

        }
        return yAxis;
    }

    private List<String> getXAxisYearValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i = 0; i < yearValues.size(); i++) {
            xAxis.add(DateHandler.monthFormat(yearValues.get(i).getDate()));
        }
        return xAxis;
    }

    private List<String> getXAxisMonthValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i = 0; i < monthValues.size(); i++) {
            xAxis.add(DateHandler.dayFormat(monthValues.get(i).getDate()));
        }
        return xAxis;
    }

    private List<BarEntry> getYAxisMonthValues() {
        ArrayList<BarEntry> yAxis = new ArrayList<>();
        for (int i = 0; i < monthValues.size(); i++) {
            BarEntry v1e1 = new BarEntry(monthValues.get(i).getWaterDrunk(), i);
            yAxis.add(v1e1);
        }
        return yAxis;
    }

    private List<String> getXAxisWeekValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i = 0; i < weekValues.size(); i++) {
            xAxis.add(DateHandler.dayFormat(weekValues.get(i).getDate()));
        }
        return xAxis;
    }

    private List<BarEntry> getYAxisWeekValues() {
        ArrayList<BarEntry> yAxis = new ArrayList<>();
        for (int i = 0; i < weekValues.size(); i++) {
            BarEntry v1e1 = new BarEntry(weekValues.get(i).getWaterDrunk(), i);
            yAxis.add(v1e1);

        }
        return yAxis;
    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i = 0; i < dayValues.size(); i++) {
            xAxis.add(dayValues.get(i).getTime().toString());

        }
        return xAxis;
    }

    private ArrayList<BarEntry> getYAxisValues() {
        ArrayList<BarEntry> yAxis = new ArrayList<>();
        for (int i = 0; i < dayValues.size(); i++) {
            BarEntry v1e1 = new BarEntry(dayValues.get(i).getAmount(), i);
            yAxis.add(v1e1);
        }
        return yAxis;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}




