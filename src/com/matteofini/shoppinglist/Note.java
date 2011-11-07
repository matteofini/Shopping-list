package com.matteofini.shoppinglist;

import java.util.List;

import android.location.Location;
import android.net.Uri;
import android.text.Spanned;

public class Note {
	
	private String mTitle;
	private Spanned mText;
	private List<Uri> mImgURI;
	private List<Location> mLoc;
	// voice recording?

	public Note() {}
	
	public Note(String title, Spanned text, List<Uri> images, List<Location> positions) {
		mTitle = title;
		mText = text;
		mImgURI = images;
		mLoc = positions;
	}

	public Spanned getText() {
		return mText;
	}

	public void setText(Spanned mText) {
		this.mText = mText;
	}

	public List<Uri> getImgURI() {
		return mImgURI;
	}

	public void setImgURI(List<Uri> mImgURI) {
		this.mImgURI = mImgURI;
	}

	public List<Location> getmLoc() {
		return mLoc;
	}

	public void setLoc(List<Location> mLoc) {
		this.mLoc = mLoc;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
}
