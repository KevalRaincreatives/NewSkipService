package com.skipservices.parsers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DELL on 10/04/2016.
 */

public class TaskListModel implements Serializable{
    private String id;
    private String task_number;
    private String client_name;
    private String address;
    private String short_description;
    private String description;
    private String image_path;
    private String job_type;
    private String task_process;
    private String date;
    private String time;
    private String latitude;
    private String longitude;
    private String acknowledged_by;
    private String acknowledged_date;
    private String acknowledged_time;
    private String disposal_date;
    private String disposal_time;
    private String second_man;
    private String disposal_site_id;
    private String disposal_site_name;
    private String job_started;
    private String sing_off_status;
    private String job_start_datetime;
    private String task_sync_status;
    private String ss1;
    private String ss2;
    private String ss2_A;
    private String ss3;
    private String final_sync;
    private ArrayList<DisposalModel> disposal_site=new ArrayList<>();
    private String instructions;

    public TaskListModel(String id, String task_number, String client_name, String address, String short_description, String description, String image_path, String job_type, String task_process, String date, String time, String latitude, String longitude, String acknowledged_by, String acknowledged_date, String acknowledged_time, String disposal_date, String disposal_time, String second_man, String disposal_site_id, String disposal_site_name, String job_started, String sing_off_status, String job_start_datetime, String task_sync_status, String ss1, String ss2, String ss2_A, String ss3, String final_sync, ArrayList<DisposalModel> disposal_site, String instructions) {
        this.id = id;
        this.task_number = task_number;
        this.client_name = client_name;
        this.address = address;
        this.short_description = short_description;
        this.description = description;
        this.image_path = image_path;
        this.job_type = job_type;
        this.task_process = task_process;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.acknowledged_by = acknowledged_by;
        this.acknowledged_date = acknowledged_date;
        this.acknowledged_time = acknowledged_time;
        this.disposal_date = disposal_date;
        this.disposal_time = disposal_time;
        this.second_man = second_man;
        this.disposal_site_id = disposal_site_id;
        this.disposal_site_name = disposal_site_name;
        this.job_started = job_started;
        this.sing_off_status = sing_off_status;
        this.job_start_datetime = job_start_datetime;
        this.task_sync_status = task_sync_status;
        this.ss1 = ss1;
        this.ss2 = ss2;
        this.ss2_A = ss2_A;
        this.ss3 = ss3;
        this.final_sync = final_sync;
        this.disposal_site = disposal_site;
        this.instructions = instructions;
    }

    public TaskListModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
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

    public String getSecond_man() {
        return second_man;
    }

    public void setSecond_man(String second_man) {
        this.second_man = second_man;
    }

    public String getDisposal_site_id() {
        return disposal_site_id;
    }

    public void setDisposal_site_id(String disposal_site_id) {
        this.disposal_site_id = disposal_site_id;
    }

    public String getDisposal_site_name() {
        return disposal_site_name;
    }

    public void setDisposal_site_name(String disposal_site_name) {
        this.disposal_site_name = disposal_site_name;
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

    public ArrayList<DisposalModel> getDisposal_site() {
        return disposal_site;
    }

    public void setDisposal_site(ArrayList<DisposalModel> disposal_site) {
        this.disposal_site = disposal_site;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}