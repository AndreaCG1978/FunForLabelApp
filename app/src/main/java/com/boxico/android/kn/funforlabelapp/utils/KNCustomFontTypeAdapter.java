package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.LabelFont;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;

import java.io.File;


public class KNCustomFontTypeAdapter extends ArrayAdapter<LabelFont> {

	Context myContext;

	public KNCustomFontTypeAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull LabelFont[] objects) {
		super(context, resource, textViewResourceId, objects);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);
		TextView txt = view.findViewById(R.id.rowValor);
		txt.setTextSize(17);
		LabelFont li = getItem(position);
		File fileFont = ConstantsAdmin.getFile(li.getBasename());
		Typeface face = Typeface.createFromFile(fileFont);
		txt.setTypeface(face);
		return view;
	}


	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		/*if (null == convertView) {
			LayoutInflater inflater = (LayoutInflater) localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.stagerow, null);
		}
*/
		final View view = super.getView(position, convertView, parent);
		TextView txt = view.findViewById(R.id.rowValor);
		LabelFont li = getItem(position);
		txt.setTextSize(24);
		File fileFont = ConstantsAdmin.getFile(li.getBasename());
		Typeface face = Typeface.createFromFile(fileFont);
		txt.setTypeface(face);

		GradientDrawable border = new GradientDrawable();
		border.setColor(Color.WHITE); //white background
		border.setStroke(2, Color.DKGRAY); //black border with full opacity
		//linearTag.

		view.setBackground(border);
		view.setPadding(10,10,10,10);
		return view;
	}
}
