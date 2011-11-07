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
