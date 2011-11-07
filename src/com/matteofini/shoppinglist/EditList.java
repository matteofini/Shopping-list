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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditList extends Activity {
	private LocationManager LM; 
	private Note mNote;
	
	private LocationListener loclis = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("\t\t"+provider+" has changed status to "+status);
		}
		@Override
		public void onProviderEnabled(String provider) {
			System.out.println("\t\t"+provider+" enabled");
		}
		@Override
		public void onProviderDisabled(String provider) {
			System.out.println("\t\t"+provider+" disabled");
		}
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("\t\t"+location.getLatitude()+" "+location.getLongitude());
			View root = getWindow().getDecorView();
			//ExpandableListView explist = (ExpandableListView) root.findViewById(R.id.attachments);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		LM = (LocationManager) getSystemService(LOCATION_SERVICE);
		mNote = new Note();
		
		final long rowid = getIntent().getExtras().getLong("id");
		ShoppingDb db = new ShoppingDb(EditList.this);
		db.open();
		final View ll = getLayoutInflater().inflate(R.layout.edit,null);
		//title
		mNote.setTitle(db.getListTitle(rowid).getString(0));
    	EditText title = (EditText) ll.findViewById(R.id.editlist_title);
    	title.setText(mNote.getTitle());
    	//text content
    	final Cursor c = db.getListContent(rowid);
    	if(c.getCount()!=0){
    		String str = db.getListContent(rowid).getString(0);
    		if(str!=null){
    			mNote.setText(Html.fromHtml(str));
    			LinearLayout content = (LinearLayout) ll.findViewById(R.id.editlist_content);
    			content.removeView(ll.findViewById(R.id.editlist_content_hint));
    			EditText textEdit = new EditText(EditList.this);
    			textEdit.setText(mNote.getText());
    			textEdit.setLines(6);
    			content.addView(textEdit);
    		}
    	}
    	c.close();
    	
    	View b_save = ll.findViewById(R.id.button_save);
    	View b_cancel = ll.findViewById(R.id.button_cancel);
    	
    	/*
    	ll.findViewById(R.id.bold).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int start = content.getSelectionStart();
				int end = content.getSelectionEnd();
				if(start==end){
					Toast.makeText(EditList.this, "Nessun testo selezionato", Toast.LENGTH_LONG).show();
				}
				else{
					int removed=0;
					StyleSpan[] spans = content.getText().getSpans(start, end, StyleSpan.class);
					if(spans.length!=0){
						for(int i=0;i<spans.length;i++){
							if(spans[i].getStyle()==Typeface.BOLD){
								content.getText().removeSpan(spans[i]);
								System.out.println("\t removed span start from "+content.getText().getSpanStart(spans[0]));
								removed++;
							}
						}
						if(removed==0){
							content.getText().setSpan(new StyleSpan(Typeface.BOLD), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
							System.out.println("\t attached span from "+start+" to "+end);
						}
					}
					else{
						content.getText().setSpan(new StyleSpan(Typeface.BOLD), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
						System.out.println("\t attached span from "+start+" to "+end);
					}
					System.out.println(Html.toHtml(content.getText()));
				}
			}
		});  	
    	
    	ll.findViewById(R.id.italic).setOnClickListener(new OnClickListener() {
    		@Override
			public void onClick(View v) {
				int start = content.getSelectionStart();
				int end = content.getSelectionEnd();
				if(start==end){
					Toast.makeText(EditList.this, "Nessun testo selezionato", Toast.LENGTH_LONG).show();
				}
				else{
					int removed=0;
					StyleSpan[] spans = content.getText().getSpans(start, end, StyleSpan.class);
					if(spans.length!=0){
						for(int i=0;i<spans.length;i++){
							if(spans[i].getStyle()==Typeface.ITALIC){
								content.getText().removeSpan(spans[i]);
								System.out.println("\t removed span start from "+content.getText().getSpanStart(spans[0]));
								removed++;
							}
						}
						if(removed==0){
							content.getText().setSpan(new StyleSpan(Typeface.ITALIC), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
							System.out.println("\t attached span from "+start+" to "+end);
						}
					}
					else{
						content.getText().setSpan(new StyleSpan(Typeface.ITALIC), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
						System.out.println("\t attached span from "+start+" to "+end);
					}
					System.out.println(Html.toHtml(content.getText()));
				}
			}
		});  
    	
    	ll.findViewById(R.id.underline).setOnClickListener(new OnClickListener() {
    		@Override
			public void onClick(View v) {
				int start = content.getSelectionStart();
				int end = content.getSelectionEnd();
				if(start==end){
					Toast.makeText(EditList.this, "Nessun testo selezionato", Toast.LENGTH_LONG).show();
				}
				else{
					UnderlineSpan[] spans = content.getText().getSpans(start, end, UnderlineSpan.class);
					if(spans.length!=0){
						for(int i=0;i<spans.length;i++){
							content.getText().removeSpan(spans[i]);
							System.out.println("\t removed span start from "+content.getText().getSpanStart(spans[0]));
						}
					}
					else{
						content.getText().setSpan(new UnderlineSpan(), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
						System.out.println("\t attached span from "+start+" to "+end);
					}
					System.out.println(Html.toHtml(content.getText()));
				}
			}
		});  
    	
    	ll.findViewById(R.id.strike).setOnClickListener(new OnClickListener() {
    		@Override
			public void onClick(View v) {
				int start = content.getSelectionStart();
				int end = content.getSelectionEnd();
				if(start==end){
					Toast.makeText(EditList.this, "Nessun testo selezionato", Toast.LENGTH_LONG).show();
				}
				else{
					StrikethroughSpan[] spans = content.getText().getSpans(start, end, StrikethroughSpan.class);
					if(spans.length!=0){
						for(int i=0;i<spans.length;i++){
							content.getText().removeSpan(spans[i]);
							System.out.println("\t removed span start from "+content.getText().getSpanStart(spans[0]));
						}
					}
					else{
						content.getText().setSpan(new StrikethroughSpan(), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
						System.out.println("\t attached span from "+start+" to "+end);
					}
					System.out.println(Html.toHtml(content.getText()));
				}
			}
		});  
    	/*
    	ll.findViewById(R.id.link).setOnClickListener(new OnClickListener() {
    		@Override
			public void onClick(View v) {
				final int start = content.getSelectionStart();
				final int end = content.getSelectionEnd();
					URLSpan[] spans = content.getText().getSpans(start, end, URLSpan.class);
					if(spans.length!=0){
						for(int i=0;i<spans.length;i++){
							content.getText().removeSpan(spans[i]);
							System.out.println("\t removed span start from "+content.getText().getSpanStart(spans[0]));
						}
					}
					else{
						final View dialog_layout = getLayoutInflater().inflate(R.layout.dialog_link, null);
						AlertDialog dialog = new AlertDialog.Builder(EditList.this)
					    .setTitle("Inserisci l'indirizzo del link")
					    .setView(dialog_layout)
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int whichButton) {
					            EditText edit = (EditText) dialog_layout.findViewById(R.id.edit_link);
					            setLink(edit.getText(), start, end);				            
					        }

							private void setLink(Editable url, int start, int end) {
								View ll = getLayoutInflater().inflate(R.layout.edit,null);
								URLSpan urlspan = new URLSpan(url.toString());
								if(start==end){
									content.getText().insert(content.getSelectionEnd(), Html.fromHtml("<a href='"+url+"'>"+url+"</a>"));
								}
								else
									content.getText().setSpan(urlspan, content.getSelectionStart(), content.getSelectionEnd(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
								System.out.println("\t attached span from "+start+" to "+end);
								
							}
					    })
					    .setNegativeButton("Cancel", null).create();
						dialog.show();
					}
					System.out.println(Html.toHtml(content.getText()));
				}
		});  
    	
    	ll.findViewById(R.id.photo).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i, 300);
			}
		});

    	ll.findViewById(R.id.gps).setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			//List<String> providers = LM.getAllProviders();
    			if(!LM.isProviderEnabled("network") && !LM.isProviderEnabled("gps")){
    				Toast.makeText(EditList.this, "Abilita una origine dati (rete o gps) per 'La mia posizione' nelle impostazioni di sistema", 2).show();
    			}
    			else{
    				if(LM.isProviderEnabled("network")){
    					ConnectivityManager CM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    					NetworkInfo info = CM.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    					if(!info.isConnected()){
    						Toast.makeText(EditList.this, "Non sei connesso a nessuna rete WIFI. Il telefono può essere fuori portata.", 2).show();
    						//LM.getLastKnownLocation("network");	//TODO
    					}
						else
    						LM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, loclis);
    				}
    				else
    					LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loclis);
    			}
			}
    	});
    	*/
    	b_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout content = (LinearLayout) getWindow().getDecorView().findViewById(R.id.editlist_content);
				EditText edit = (EditText) content.findViewById(R.id.edit_textcontent);
				if(edit!=null){
					Bundle b = new Bundle();
					b.putString("content", Html.toHtml(edit.getText()));
					b.putLong("id", rowid);
					showDialog(0, b);
				}
			}
		});
    	b_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    	b_save.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_save), Toast.LENGTH_LONG).show();
				return true;
			}
		});
    	b_cancel.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.help_cancel), Toast.LENGTH_LONG).show();
				return true;
			}
		});
    	setContentView(ll);
    	ll.requestFocus();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.option_edit, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getOrder()) {
		case 1:
			LinearLayout ll = (LinearLayout) getWindow().getDecorView().findViewById(R.id.editlist_content);
			if(ll.findViewById(R.id.editlist_content_hint)!=null){
				ll.removeView(ll.findViewById(R.id.editlist_content_hint));
				EditText edit = (EditText) getLayoutInflater().inflate(R.layout.edit_textcontent, null);
				ll.addView(edit);
				edit.requestFocusFromTouch();
				edit.requestFocus();
			}
			else{
				ll.getChildAt(0).requestFocus();
			}
			break;

		default:
			break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==300 && data!=null){
			Uri uri = data.getData();
			ImageView img = new ImageView(EditList.this);
			img.setImageURI(uri);
			//LinearLayout ll = (LinearLayout) getWindow().findViewById(R.id.attachments);
			//ll.addView(img); 
		}
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
