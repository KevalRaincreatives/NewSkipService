package com.skipservices.adapter;

import java.util.ArrayList;
import com.skipservices.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TicketSpinnerAdapter extends BaseAdapter
{

	Context context;
	ArrayList<String>arrayList = new ArrayList<String>();
	LayoutInflater layoutInflater;
	
	public TicketSpinnerAdapter(Context context, ArrayList<String>arrayList) {
		super();
		this.context = context;
		this.arrayList = arrayList;
		layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		try
		{
			
			   
				convertView = layoutInflater.inflate(R.layout.adapter_dialogbook, null);
				holder.txt_title = (TextView)convertView.findViewById(R.id.txt_dataname);
				
				
				holder.txt_title.setText(arrayList.get(position));
			
				
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return convertView;
	}

}
