package com.matteofini.shoppinglist;

import java.awt.font.TextAttribute;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.Html.TagHandler;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EditList extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		final long rowid = getIntent().getExtras().getLong("id");
		ShoppingDb db = new ShoppingDb(EditList.this);
		db.open();
		
		final View ll = getLayoutInflater().inflate(R.layout.edit,null);
    	EditText title = (EditText) ll.findViewById(R.id.editlist_title);
    	title.setText(db.getListTitle(rowid).getString(0));
    	
    	final EditText content = (EditText) ll.findViewById(R.id.editlist_content);
    	final Cursor c = db.getListContent(rowid);
    	if(c.getCount()!=0){
    		String str = db.getListContent(rowid).getString(0);
    		if(str!=null){
    			Spanned spanned = Html.fromHtml(str);
    			content.setText(spanned);
    		}
    		else
    			content.setText(str);
    		//content.setText(db.getListContent(rowid).getString(0));
    	}
    	c.close();
    	
    	View b_save = ll.findViewById(R.id.button_save);
    	View b_cancel = ll.findViewById(R.id.button_cancel);
    	Button b_strike = (Button) ll.findViewById(R.id.strike);
    	b_strike.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    	
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
    	
    	ll.findViewById(R.id.link).setOnClickListener(new OnClickListener() {
    		@Override
			public void onClick(View v) {
				final int start = content.getSelectionStart();
				final int end = content.getSelectionEnd();
				if(start==end){
					Toast.makeText(EditList.this, "Nessun testo selezionato", Toast.LENGTH_LONG).show();
				}
				else{
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
								content.setMovementMethod(LinkMovementMethod.getInstance());
						    	//EditText content = (EditText) ll.findViewById(R.id.editlist_content);
								content.getText().setSpan(urlspan, content.getSelectionStart(), content.getSelectionEnd(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
								System.out.println("\t attached span from "+start+" to "+end);
								
							}
					    })
					    .setNegativeButton("Cancel", null).create();
						dialog.show();
					}
					System.out.println(Html.toHtml(content.getText()));
				}
			}
		});  
    	  	
    	b_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edit = (EditText) ll.findViewById(R.id.editlist_content);
				Bundle b = new Bundle();
				b.putString("content", Html.toHtml(edit.getText()));
				b.putLong("id", rowid);
				showDialog(0, b);
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
	}

	
	@Override
	protected Dialog onCreateDialog(int id, final Bundle args) {
		if(id==0){
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			
			adb.setTitle(getResources().getString(R.string.ask_save))
				.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//SpannableStringBuilder content = SpannableStringBuilder.valueOf(args.getString("content"));
						//SpannableStringBuilder content = SpannableStringBuilder.valueOf("<b>vaffa</b>");
						ShoppingDb db = new ShoppingDb(EditList.this);
						db.open();
						//db.addListContent(args.getLong("id"), Html.toHtml(content));
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
