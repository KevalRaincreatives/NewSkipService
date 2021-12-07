package com.skipservices.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.skipservices.R;
import com.skipservices.interfaces.OnBtnRemoveBookClickListner;
import com.skipservices.utils.WebService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class BookAddAdapter  extends BaseAdapter
{

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<HashMap<String, String>>arrayList = new ArrayList<HashMap<String, String>>();
	OnBtnRemoveBookClickListner onBtnRemoveBookClickListner;
	
	
	public BookAddAdapter(Context context, ArrayList<HashMap<String, String>> arrayList, OnBtnRemoveBookClickListner onBtnRemoveBookClickListner) {
		super();
		this.context = context;
		this.arrayList = arrayList;
		layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.onBtnRemoveBookClickListner = onBtnRemoveBookClickListner;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	static class ViewHolder
	{
		TextView txt_title;
		Button btn_delete;
	}
	
	

	@Override
	public View getView( final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = new ViewHolder();
		try
		{
			
			   
				convertView = layoutInflater.inflate(R.layout.adapter_listticket, null);
				holder.txt_title = (TextView)convertView.findViewById(R.id.txt_title);
				holder.btn_delete = (Button)convertView.findViewById(R.id.btn_delete);
				holder.btn_delete.setVisibility(View.INVISIBLE);
				HashMap<String, String>map = arrayList.get(position);
				holder.txt_title.setText(map.get(WebService.KEY_TASKDETAIL_USEDTICKETBOOK));
			
				
				holder.btn_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//onBtnRemoveBookClickListner.OnBtnRemoveBookClick(position);
					}
				});
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return convertView;
	}

}
