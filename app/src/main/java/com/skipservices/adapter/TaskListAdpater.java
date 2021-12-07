package com.skipservices.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.skipservices.R;
import com.skipservices.utils.WebService;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskListAdpater extends BaseAdapter{

	Context context;
	LayoutInflater layoutInflater;
	ArrayList<HashMap<String, String>>arrayList = new ArrayList<HashMap<String,String>>();
	
	
	public TaskListAdpater(Context context, ArrayList<HashMap<String, String>>arrayList) {
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
		TextView txt_number;
		TextView txt_clientname;
		TextView txt_address;
		TextView txt_discription;
		TextView txt_jobtype;
		TextView txt_status;
		LinearLayout layout;
		
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try{
			ViewHolder holder = new ViewHolder();
			
			convertView = layoutInflater.inflate(R.layout.adapter_tasklist, null);
			holder.txt_number = (TextView)convertView.findViewById(R.id.txt_snumber);
			holder.txt_clientname = (TextView)convertView.findViewById(R.id.txt_clientname);
			holder.txt_address = (TextView)convertView.findViewById(R.id.txt_address);
			holder.txt_discription = (TextView)convertView.findViewById(R.id.txt_description);
			holder.txt_jobtype = (TextView)convertView.findViewById(R.id.txt_jobtype);
			holder.txt_status = (TextView)convertView.findViewById(R.id.txt_status);
			holder.layout = (LinearLayout)convertView.findViewById(R.id.layout_tasklist);
			 holder.txt_number.setText(String.valueOf(position+1));
			 HashMap<String, String>hashMap = arrayList.get(position);
			 holder.txt_clientname.setText(hashMap.get(WebService.KEY_TASKLIST_CLIENTNAME));
			 holder.txt_address.setText(hashMap.get(WebService.KEY_TASKLIST_ADDRESS));
			 holder.txt_discription.setText(hashMap.get(WebService.KEY_TASKLIST_SHORTDESCRIPTION));
			 holder.txt_jobtype.setText(hashMap.get(WebService.KEY_TASKLIST_JOBTYPE));
			 holder.txt_status.setText(hashMap.get(WebService.KEY_TASKLIST_TASKPROCESS));
			if (position % 2 == 1) {
				holder.layout.setBackgroundColor(Color.WHITE); 
				holder.txt_number.setBackgroundResource(R.drawable.txtshapecolor_green);
			   
			} else {
				holder.layout.setBackgroundColor(Color.parseColor("#c1c1c1"));  
				holder.txt_number.setBackgroundResource(R.drawable.txtshapecolor_blue);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return convertView;
	}

}
