package com.matteofini.shoppinglist;

import com.matteofini.shoppinglist.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingList extends ListActivity {
	
	private static final int ADD_TITLE_DIALOG = 0;
	private Cursor mCursor;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        View b = findViewById(R.id.button_addlist);
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(ADD_TITLE_DIALOG);
			}
		});
        b.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_add_button), Toast.LENGTH_LONG).show();
				return true;
			}
		});
        
        ShoppingDb shoppingDB = new ShoppingDb(ShoppingList.this);
        shoppingDB.open();
        mCursor = shoppingDB.getShoppingList();
        startManagingCursor(mCursor);
        //System.out.println("\t count: "+c.getCount());
        SimpleCursorAdapter sca = new MyAdapter(this, R.layout.mainlist_item, mCursor, new String[]{"title", "date", "summary"}, new int[]{R.id.item_title, R.id.item_date, R.id.item_summary});
        setListAdapter(sca);   
    } 

    
    public class MyAdapter extends SimpleCursorAdapter{

    	protected int count;
    	
    	public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
    		super(context, layout, c, from, to);
    		count=0;
    	}
    	
    	@Override
    	public View getView(final int position, View convertView, ViewGroup parent) {
     		View v = getLayoutInflater().inflate(R.layout.mainlist_item, null);
    		View b_view = v.findViewById(R.id.button_view);
    		View b_edit = v.findViewById(R.id.button_edit);
    		View b_del = v.findViewById(R.id.button_del);
    		
    		final Cursor c = getCursor();
    		c.moveToPosition(position);
    		
    		TextView title = (TextView) v.findViewById(R.id.item_title);
    		title.setText(c.getString(c.getColumnIndex("title")));
    		
    		TextView date = (TextView) v.findViewById(R.id.item_date);
    		int mills = c.getInt(c.getColumnIndex("date"));
    		
    		CharSequence str = DateFormat.format("MM/dd/yy h:mmaa", mills);
    		//date.setText(d.getDay()+"/"+d.getMonth()+"/"+d.getYear()+" "+d.getHours()+":"+d.getMinutes());
    		date.setText(str);
    		
    		String c_summ = c.getString(c.getColumnIndex("summary"));
	    	if(c_summ!=null){
    			TextView summary = (TextView) v.findViewById(R.id.item_summary);
	    		String[] summ_rows = c_summ.split("\n");
	    		summary.setText(summ_rows[0].substring(0, summ_rows[0].length())+" ...");
	    	}
    		OnClickListener view_ocl = new OnClickListener() {
				long id = c.getLong(0);
    			@Override
				public void onClick(View v) {
					view(id);
				}
			};
			b_view.setOnClickListener(view_ocl);
			OnClickListener edit_ocl = new OnClickListener() {
				long id = c.getLong(0);
    			@Override
				public void onClick(View v) {
					edit(id);
				}
			};
			b_edit.setOnClickListener(edit_ocl);
			OnClickListener del_ocl = new OnClickListener() {
				long id = c.getLong(0);
    			@Override
				public void onClick(View v) {
					delete(id);
				}
			};
    		b_del.setOnClickListener(del_ocl);
    		
    		b_view.setOnLongClickListener(new OnLongClickListener() {	
				@Override
				public boolean onLongClick(View v) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_view), Toast.LENGTH_LONG).show();
					return true;
				}
			});
    		b_edit.setOnLongClickListener(new OnLongClickListener() {	
				@Override
				public boolean onLongClick(View v) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_edit), Toast.LENGTH_LONG).show();
					return true;
				}
			});
    		b_del.setOnLongClickListener(new OnLongClickListener() {	
				@Override
				public boolean onLongClick(View v) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_delete), Toast.LENGTH_LONG).show();
					return true;
				}
			});
    		
    		if(position%2==0)
    			v.setBackgroundColor(Color.DKGRAY);
    		else
    			v.setBackgroundColor(Color.GRAY);
    		return v;
    	}
    	
    	private void view(long id){
    		Intent i = new Intent();
			i.setComponent(new ComponentName(ShoppingList.this, ShowList.class));
			i.putExtra("id", id);
			startActivity(i);
    	}
    	
    	private void edit(long id){
    		Intent i = new Intent();
			i.setComponent(new ComponentName(ShoppingList.this, EditList.class));
			i.putExtra("id", id);
			startActivity(i);
    	}
    	
    	private void delete(long id){
    		ShoppingDb shoppingDB = new ShoppingDb(ShoppingList.this);
	        shoppingDB.open();
	        int res = shoppingDB.deleteList(id);
	        Toast.makeText(getApplicationContext(), getResources().getString(R.string.list_deleted), Toast.LENGTH_LONG).show();
	        shoppingDB.close();
	        mCursor.requery();
	        setListAdapter(new MyAdapter(ShoppingList.this, R.layout.mainlist_item, mCursor, new String[]{"title", "date", "summary"}, new int[]{R.id.item_title, R.id.item_date, R.id.item_summary}));
    	}
    	
    }

    
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog d = null;
    	if(id==0){
    		final View ll = getLayoutInflater().inflate(R.layout.dialog_addtitle, null);
			AlertDialog.Builder dialb = new AlertDialog.Builder(ShoppingList.this);
			dialb.setView(ll);
			dialb.setIcon(R.drawable.alert_dialog_icon);
			dialb.setTitle(getResources().getString(R.string.dialog_addtitle_title));
			dialb.setCancelable(true);
			dialb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			dialb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					EditText edit = (EditText) ll.findViewById(R.id.edit_addtitle);
					String str = edit.getText().toString();
					if(!str.equals("")){
						ShoppingDb db = new ShoppingDb(getApplicationContext());
						db.open();
						long id = db.addList(str);
						db.close();
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.list_created), Toast.LENGTH_LONG).show();
						mCursor.requery();
						setListAdapter(new MyAdapter(ShoppingList.this, R.layout.mainlist_item, mCursor, new String[]{"title", "date", "summary"}, new int[]{R.id.item_title, R.id.item_date, R.id.item_summary}));
						Intent i = new Intent();
						i.putExtra("id", id);
						i.setComponent(new ComponentName("com.matteofini.shoppinglist", "com.matteofini.shoppinglist.EditList"));
						startActivity(i);
					}
					else{
						edit.requestFocus();
						Toast.makeText(getApplicationContext(), "inserisci un titolo non vuoto", Toast.LENGTH_SHORT).show();
					}
				}
			});
			d = dialb.create();
    	}
		return d;
    }
}