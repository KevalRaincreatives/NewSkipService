package com.skipservices.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL on 09/25/2016.
 */
public class TaskListCartModel {
    int id;
    String userId,task_id, task_number, client_name, address, short_description, job_type, task_process;

    public TaskListCartModel(int id, String userId, String task_id, String task_number, String client_name, String address, String short_description, String job_type, String task_process) {
        this.id = id;
        this.userId = userId;
        this.task_id = task_id;
        this.task_number = task_number;
        this.client_name = client_name;
        this.address = address;
        this.short_description = short_description;
        this.job_type = job_type;
        this.task_process = task_process;
    }

    public TaskListCartModel() {

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getTask_process() {
        return task_process;
    }

    public void setTask_process(String task_process) {
        this.task_process = task_process;
    }

    public String TableName() {
        return "Details";
    }


    public JSONObject getJSONObject() {
        JSONObject jsonobj = new JSONObject();
        try {

            jsonobj.put("task_id", task_id);
            jsonobj.put("task_number", task_number);
            jsonobj.put("client_name", client_name);
            jsonobj.put("address", address);
            jsonobj.put("short_description", short_description);
            jsonobj.put("job_type", job_type);
            jsonobj.put("task_process", task_process);


//            jsonobj.remove("\\\\");
        } catch (JSONException e) {

        }
        return jsonobj;
    }


}
