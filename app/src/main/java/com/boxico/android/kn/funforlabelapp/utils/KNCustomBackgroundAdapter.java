package com.boxico.android.kn.funforlabelapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.boxico.android.kn.funforlabelapp.R;
import com.boxico.android.kn.funforlabelapp.dtos.LabelImage;


public class KNCustomBackgroundAdapter extends ArrayAdapter<LabelImage> {

	Context myContext;


	public KNCustomBackgroundAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull LabelImage[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.myContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);
		TextView txt = view.findViewById(R.id.rowValor);
		LabelImage li = getItem(position);

		int srcWidth = li.getImage().getWidth();
		int srcHeight = li.getImage().getHeight();
		int dstWidth = (int)(srcWidth*0.15f);
		int dstHeight = (int)(srcHeight*0.15f);
	//	Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);
		Bitmap b =Bitmap.createScaledBitmap(li.getImage(), dstWidth,dstHeight, false);
	//	Bitmap b =Bitmap.createScaledBitmap
		BitmapDrawable icon = new BitmapDrawable(myContext.getResources(), b);
		txt.setCompoundDrawablesWithIntrinsicBounds(null, null, icon,null);
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
		LabelImage li = getItem(position);
		int srcWidth = li.getImage().getWidth();
		int srcHeight = li.getImage().getHeight();
		int dstWidth = (int)(srcWidth*0.50f);
		int dstHeight = (int)(srcHeight*0.50f);
		//	Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);
		Bitmap b =Bitmap.createScaledBitmap(li.getImage(), dstWidth,dstHeight, false);
		BitmapDrawable icon = new BitmapDrawable(myContext.getResources(), b);
		txt.setCompoundDrawablesWithIntrinsicBounds(null, null, icon,null);
		GradientDrawable border = new GradientDrawable();
		border.setColor(Color.TRANSPARENT); //white background
		border.setStroke(2, Color.DKGRAY); //black border with full opacity
		//linearTag.

		view.setBackground(border);
		view.setPadding(10,10,10,10);
		return view;
	}
}
