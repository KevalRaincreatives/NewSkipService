package com.skipservices.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL on 09/25/2016.
 */
public class TaskDetailCartModel {
    int id;
    String userId,task_id, task_number, client_name, date,time,address, latitude,longitude,short_description
            ,description,acknowledged_by,acknowledged_date,acknowledged_time,image_path,image_bitmap,task_process,disposal_site_name
            ,disposal_site_id,disposal_date,disposal_time, job_type,second_man,job_started, sing_off_status,job_start_datetime
            ,task_sync_status,ss1,ss2,ss2_A,ss3,final_sync,new_rental,final_removal,instructions;

    public TaskDetailCartModel(int id, String userId, String task_id, String task_number, String client_name, String date, String time, String address, String latitude, String longitude, String short_description, String description, String acknowledged_by, String acknowledged_date, String acknowledged_time, String image_path, String image_bitmap, String task_process, String disposal_site_name, String disposal_site_id, String disposal_date, String disposal_time, String job_type, String second_man, String job_started, String sing_off_status, String job_start_datetime, String task_sync_status, String ss1, String ss2, String ss2_A, String ss3, String final_sync, String new_rental, String final_removal, String instructions) {
        this.id = id;
        this.userId = userId;
        this.task_id = task_id;
        this.task_number = task_number;
        this.client_name = client_name;
        this.date = date;
        this.time = time;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.short_description = short_description;
        this.description = description;
        this.acknowledged_by = acknowledged_by;
        this.acknowledged_date = acknowledged_date;
        this.acknowledged_time = acknowledged_time;
        this.image_path = image_path;
        this.image_bitmap = image_bitmap;
        this.task_process = task_process;
        this.disposal_site_name = disposal_site_name;
        this.disposal_site_id = disposal_site_id;
        this.disposal_date = disposal_date;
        this.disposal_time = disposal_time;
        this.job_type = job_type;
        this.second_man = second_man;
        this.job_started = job_started;
        this.sing_off_status = sing_off_status;
        this.job_start_datetime = job_start_datetime;
        this.task_sync_status = task_sync_status;
        this.ss1 = ss1;
        this.ss2 = ss2;
        this.ss2_A = ss2_A;
        this.ss3 = ss3;
        this.final_sync = final_sync;
        this.new_rental = new_rental;
        this.final_removal = final_removal;
        this.instructions = instructions;
    }

    public TaskDetailCartModel() {

    }





    public String TableName() {
        return "Details2";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_number() {
        return task_number;
    }

    public void setTask_number(String task_number) {
        this.task_number = task_number;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcknowledged_by() {
        return acknowledged_by;
    }

    public void setAcknowledged_by(String acknowledged_by) {
        this.acknowledged_by = acknowledged_by;
    }

    public String getAcknowledged_date() {
        return acknowledged_date;
    }

    public void setAcknowledged_date(String acknowledged_date) {
        this.acknowledged_date = acknowledged_date;
    }

    public String getAcknowledged_time() {
        return acknowledged_time;
    }

    public void setAcknowledged_time(String acknowledged_time) {
        this.acknowledged_time = acknowledged_time;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getImage_bitmap() {
        return image_bitmap;
    }

    public void setImage_bitmap(String image_bitmap) {
        this.image_bitmap = image_bitmap;
    }

    public String getTask_process() {
        return task_process;
    }

    public void setTask_process(String task_process) {
        this.task_process = task_process;
    }

    public String getDisposal_site_name() {
        return disposal_site_name;
    }

    public void setDisposal_site_name(String disposal_site_name) {
        this.disposal_site_name = disposal_site_name;
    }

    public String getDisposal_site_id() {
        return disposal_site_id;
    }

    public void setDisposal_site_id(String disposal_site_id) {
        this.disposal_site_id = disposal_site_id;
    }

    public String getDisposal_date() {
        return disposal_date;
    }

    public void setDisposal_date(String disposal_date) {
        this.disposal_date = disposal_date;
    }

    public String getDisposal_time() {
        return disposal_time;
    }

    public void setDisposal_time(String disposal_time) {
        this.disposal_time = disposal_time;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getSecond_man() {
        return second_man;
    }

    public void setSecond_man(String second_man) {
        this.second_man = second_man;
    }

    public String getJob_started() {
        return job_started;
    }

    public void setJob_started(String job_started) {
        this.job_started = job_started;
    }

    public String getSing_off_status() {
        return sing_off_status;
    }

    public void setSing_off_status(String sing_off_status) {
        this.sing_off_status = sing_off_status;
    }


    public String getJob_start_datetime() {
        return job_start_datetime;
    }

    public void setJob_start_datetime(String job_start_datetime) {
        this.job_start_datetime = job_start_datetime;
    }

    public String getTask_sync_status() {
        return task_sync_status;
    }

    public void setTask_sync_status(String task_sync_status) {
        this.task_sync_status = task_sync_status;
    }

    public String getSs1() {
        return ss1;
    }

    public void setSs1(String ss1) {
        this.ss1 = ss1;
    }

    public String getSs2() {
        return ss2;
    }

    public void setSs2(String ss2) {
        this.ss2 = ss2;
    }

    public String getSs2_A() {
        return ss2_A;
    }

    public void setSs2_A(String ss2_A) {
        this.ss2_A = ss2_A;
    }

    public String getSs3() {
        return ss3;
    }

    public void setSs3(String ss3) {
        this.ss3 = ss3;
    }

    public String getFinal_sync() {
        return final_sync;
    }

    public void setFinal_sync(String final_sync) {
        this.final_sync = final_sync;
    }

    public String getNew_rental() {
        return new_rental;
    }

    public void setNew_rental(String new_rental) {
        this.new_rental = new_rental;
    }

    public String getFinal_removal() {
        return final_removal;
    }

    public void setFinal_removal(String final_removal) {
        this.final_removal = final_removal;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonobj = new JSONObject();
        try {

            jsonobj.put("task_id", task_id);
            jsonobj.put("task_number", task_number);
            jsonobj.put("client_name", client_name);
            jsonobj.put("date", date);
            jsonobj.put("time", time);
            jsonobj.put("address", address);
            jsonobj.put("latitude", latitude);
            jsonobj.put("longitude", longitude);
            jsonobj.put("short_description", short_description);
            jsonobj.put("description", description);
            jsonobj.put("acknowledged_by", acknowledged_by);
            jsonobj.put("acknowledged_date", acknowledged_date);
            jsonobj.put("acknowledged_time", acknowledged_time);
            jsonobj.put("image_path", image_path);
            jsonobj.put("task_process", task_process);
            jsonobj.put("disposal_site_name", disposal_site_name);
            jsonobj.put("disposal_site_id", disposal_site_id);
            jsonobj.put("disposal_date", disposal_date);
            jsonobj.put("disposal_time", disposal_time);
            jsonobj.put("job_type", job_type);
            jsonobj.put("second_man", second_man);
            jsonobj.put("job_started", job_started);
            jsonobj.put("sing_off_status", task_process);


//            jsonobj.remove("\\\\");
        } catch (JSONException e) {

        }
        return jsonobj;
    }


}
