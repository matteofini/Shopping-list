/**
 *  Program: Shopping List
 *  Author: Matteo Fini <mf.calimero@gmail.com>
 *  Year: 2011
 *  
 *	This file is part of "Shopping List".
 *	"Liturgia delle Ore" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  "Shopping List" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>. 
 */
package com.matteofini.shoppinglist;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ShowList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		final long rowid = i.getExtras().getLong("id");
		//Toast.makeText(getApplicationContext(), ""+rowid, Toast.LENGTH_SHORT).show();
		final ShoppingDb db = new ShoppingDb(ShowList.this);
        db.open();
        final Cursor c = db.getListContent(rowid);
        if(c.getString(0)==null){	
        	Intent i1 = new Intent();
        	i1.putExtra("id", rowid);
        	i1.setComponent(new ComponentName("com.matteofini.shoppinglist", "com.matteofini.shoppinglist.EditList"));
        	c.close();
        	db.close();
        	startActivity(i1);
        	finish();      	
        	//Toast.makeText(getApplicationContext(), shoppingDB.getListTitle(rowid).getString(0), Toast.LENGTH_SHORT).show();
        }
        else{
        	final View rl = getLayoutInflater().inflate(R.layout.showlist, null);
        	TextView tv_title = (TextView) rl.findViewById(R.id.showlist_title);
        	TextView tv_date = (TextView) rl.findViewById(R.id.showlist_date);
        	TextView tv_content = (TextView) rl.findViewById(R.id.showlist_content);
        	tv_title.setText(db.getListTitle(rowid).getString(0));
        	CharSequence str = DateFormat.format("MM/dd/yy h:mmaa", db.getListDate(rowid).getLong(0));
        	tv_date.setText(str);
        	tv_content.setText(Html.fromHtml(c.getString(0)));
//        	tv_content.setText(Html.fromHtml("<a href='http://www.google.com' title='gogole link'>link</a><b>bold</b><i>italic</i><strike>stroke</strike><t>tttttt</t>"));
//			tv_content.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        	setContentView(rl);
        	
        	View b_cal = rl.findViewById(R.id.button_calendar);
        	b_cal.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle arg = new Bundle();
					arg.putLong("id", rowid);
					showDialog(0, arg);
				}
			});
        	b_cal.setOnLongClickListener(new OnLongClickListener() {
    			@Override
    			public boolean onLongClick(View v) {
    				Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_calendar), Toast.LENGTH_LONG).show();
    				return true;
    			}
    		});
        	findViewById(R.id.button_edit2).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setComponent(new ComponentName(ShowList.this, EditList.class));
					i.putExtra("id", rowid);
					startActivity(i);
					finish();
				}
			});
        	/*
        	rl.findViewById(R.id.button_zoomin).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TextView tv = (TextView) rl.findViewById(R.id.showlist_content);
					tv.setTextSize(tv.getTextSize()+2);
				}
			});
        	rl.findViewById(R.id.button_zoomout).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					TextView tv = (TextView) rl.findViewById(R.id.showlist_content);
					tv.setTextSize(tv.getTextSize()-2);
				}
			});
			*/
        	c.close();
        	db.close();
        	
        }
	}
	
	private static DatePickerDialog.OnDateSetListener setdate_callback;
	private static TimePickerDialog.OnTimeSetListener settime_callback;
	
	
	@Override
	protected Dialog onCreateDialog(int id, final Bundle args) {
		if(id==0){
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			AlertDialog ad = adb.create();
			View v = getLayoutInflater().inflate(R.layout.timedatedialog, null);
			ad.setView(v);
			ad.setTitle("Imposta data e ora dell'evento");
			
			final TextView date = (TextView) v.findViewById(R.id.label_setdate);
			final TextView time = (TextView) v.findViewById(R.id.label_settime);
			final Calendar now = Calendar.getInstance();
			date.setText(DateFormat.format("MM/dd/yyyy", now.getTime()));
			time.setText(DateFormat.format("h:mmaa", now.getTime()));
			
			View b_setdate = v.findViewById(R.id.button_setdate);
			View b_settime = v.findViewById(R.id.button_settime);
			View b_ok = v.findViewById(R.id.button_setdatetimeOK);
			
			setdate_callback = new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					now.set(year, monthOfYear, dayOfMonth);
					date.setText(DateFormat.format("dd/MM/yyyy", now.getTime()));
				}
			};
			
			settime_callback = new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					now.set(Calendar.HOUR_OF_DAY, hourOfDay);
					now.set(Calendar.MINUTE, minute);
					time.setText(DateFormat.format("h:mmaa", now.getTime()));
				}
			};
			
			b_setdate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showDialog(1);
				}
			});
			b_settime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showDialog(2);
				}
			});
			b_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.putExtra("beginTime", now.getTimeInMillis());
					ShoppingDb db = new ShoppingDb(getApplicationContext());
					db.open();
					long id = args.getLong("id");
					i.putExtra("title", db.getListTitle(id).getString(0));
					i.putExtra("description", db.getListContent(id).getString(0));
					i.setComponent(new ComponentName("com.android.calendar", "com.android.calendar.EditEvent"));
					startActivity(i);	
				}
			});
			return ad;
		}
		else if(id==1){
			Calendar c = Calendar.getInstance();
			DatePickerDialog dp = new DatePickerDialog(this, setdate_callback, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			return dp;
		}
		else if(id==2){
			Calendar c = Calendar.getInstance();
			TimePickerDialog tp = new TimePickerDialog(this, settime_callback, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
			return tp;
		}
		return null;
	}
}
