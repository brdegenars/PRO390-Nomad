package com.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 6/10/13
 * Time: 5:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CalendarCheck {

    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> eventLocations = new ArrayList<String>();


    public static ArrayList<String> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "dtstart", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String[] calendarNames = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        eventLocations.clear();

        for (int i = 0; i < calendarNames.length; i++) {

            if (!cursor.getString(3).equals("")){
                nameOfEvent.add(cursor.getString(1));
                eventLocations.add(cursor.getString(3));
            }
            calendarNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }
        return nameOfEvent;
    }

    public static String[] testCalendarEvent(){

        return new String[]{"Capstone Defense", "You're so hosed", "10701 S. RiverFront Parkway South Jordan, Utah 84095", "531 E 2700 S Salt Lake City, UT 84106"};
    }
}
