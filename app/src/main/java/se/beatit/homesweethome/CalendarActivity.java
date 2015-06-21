package se.beatit.homesweethome;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;

import se.beatit.homesweethome.intent.GraphActivityIntent;
import se.beatit.homesweethome.time.TimeSpan;


public class CalendarActivity extends FragmentActivity {

    private CaldroidFragment caldroidFragment;

    private Date fromDate;
    private Date toDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);

        caldroidFragment = new CaldroidFragment();

        if (savedInstanceState != null) {
            // Activity is created after rotation
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        } else {
            // Activity is created from fresh
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        caldroidFragment.setCaldroidListener(new CalendarListener());

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calroidcalendar, caldroidFragment);
        t.commit();

        caldroidFragment.setSelectedDates(fromDate, toDate);
        caldroidFragment.refreshView();

        //resSelector.setPadding(0, caldroidFragment.getView().getHeight(),0,0);

    }

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        fromDate = cal.getTime();

        // Max date is today
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        toDate = cal.getTime();

        /*
        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.blue,
                    fromDate);
            caldroidFragment.setBackgroundResourceForDate(R.color.green,
                    toDate);
            caldroidFragment.setTextColorForDate(R.color.white, fromDate);
            caldroidFragment.setTextColorForDate(R.color.white, toDate);
        }
        */
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void showGraphButtonClicked(View view) {
        TimeSpan timeSpan = new TimeSpan(fromDate, toDate, "D");
        GraphActivityIntent graphActivityIntent = new GraphActivityIntent(this, timeSpan);
        graphActivityIntent.startActivity();
    }

    final class CalendarListener extends CaldroidListener {

        @Override
        public void onSelectDate(Date date, View view) {
            if(date.before(fromDate)) {
                fromDate = date;
            } else {
                toDate = date;
            }



            //caldroidFragment.setBackgroundResourceForDate(R.color.green, fromDate);
            //caldroidFragment.setTextColorForDate(R.color.white, fromDate);
            caldroidFragment.setSelectedDates(fromDate, toDate);
            caldroidFragment.refreshView();

        }

        @Override
        public void onChangeMonth(int month, int year) {

        }

        @Override
        public void onLongClickDate(Date date, View view) {
            /*
            System.out.println("on long click date pressed "+ date);
            toDate = date;
            caldroidFragment.setBackgroundResourceForDate(R.color.green, toDate);
            caldroidFragment.setTextColorForDate(R.color.white, toDate);
            caldroidFragment.setSelectedDates(fromDate, toDate);
            caldroidFragment.refreshView();
            */
        }

        @Override
        public void onCaldroidViewCreated() {

        }
    }

}
