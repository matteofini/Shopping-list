package com.matteofini.shoppinglist;

import com.matteofini.shoppinglist.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditList extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final long rowid = getIntent().getExtras().getLong("id");
		ShoppingDb db = new ShoppingDb(EditList.this);
		db.open();
		
		final View ll = getLayoutInflater().inflate(R.layout.editcontent,null);
    	TextView title = (TextView) ll.findViewById(R.id.editlist_title);
    	title.setText(db.getListTitle(rowid).getString(0));
    	
    	TextView content = (TextView) ll.findViewById(R.id.editlist_content);
    	final Cursor c = db.getListContent(rowid);
    	if(c.getCount()!=0)
    		content.setText(db.getListContent(rowid).getString(0));
    	c.close();
    	
    	View b_save = ll.findViewById(R.id.button_save);
    	View b_reset = ll.findViewById(R.id.button_cancel);
    	
    	b_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edit = (EditText) ll.findViewById(R.id.editlist_content);
				Bundle b = new Bundle();
				b.putString("content", edit.getText().toString());
				b.putLong("id", rowid);
				showDialog(0, b);
			}
		});
    	b_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edit = (EditText) ll.findViewById(R.id.editlist_content);
				edit.setText("");
			}
		});
    	
    	b_save.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_save), Toast.LENGTH_LONG).show();
				return true;
			}
		});
    	b_reset.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_cancel), Toast.LENGTH_LONG).show();
				return true;
			}
		});
    	setContentView(ll);
    	
	}
	
	@Override
	protected Dialog onCreateDialog(int id, final Bundle args) {
		if(id==0){
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			
			adb.setTitle(getResources().getString(R.string.ask_save))
				.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ShoppingDb db = new ShoppingDb(EditList.this);
						db.open();
						db.addListContent(args.getLong("id"), args.getString("content"));
						Intent i = new Intent();
						i.putExtra("id", args.getLong("id"));
						i.setComponent(new ComponentName("com.matteofini.shoppinglist", "com.matteofini.shoppinglist.ShowList"));
						db.close();
						startActivity(i);
						finish();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismissDialog(0);
					}
				})
				.setCancelable(true);
			AlertDialog ad = adb.create();
			return ad;
		}
		return null;
	}

}
