package com.todolist.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.todolist.Config.CommonStrings;

/**
 * Handles ProductTubeApp offline database.
 * <p/>
 * It will create DataBase and all the tables related to table.
 * <p/>
 * There are total 12 tables in offline sqlite database.
 * <p/>
 * <ol type="1">
 * <li>
 * <b> Table Name : </b>"my_profile" <br>
 * <b>Use : </b>Store data of user's profile.<br>
 * <b> Columns :</b><br>
 * "first_name","last_name","email","gender","age","zipcode","household_income",
 * "race_background","household_size","children_household","grocery_shopping".</li>
 * <br>
 * <li>
 * <b> Table Name : </b>"live_project" <br>
 * <b>Use : </b>Store all the data related to live projects.<br>
 * <b> Columns :</b><br>
 * "project_id","external_project_name","short_desc","final_comp_date","amount",
 * "category","email_subject",
 * "email_desc","smlink","flag","video_type","project_type"
 * ,"apply_instr","waitlist_instr","videos_count","survey_desc".</li>
 * <br>
 * <li>
 * <b> Table Name : </b>"completed_project" <br>
 * <b>Use : </b>Store all the data related to completed projects. Projects are
 * closed for the user. e.g project's final date is gone , user got reward or
 * rejected for particular project. <br>
 * <b> Columns :</b><br>
 * "project_id","external_project_name","short_desc","final_comp_date","amount",
 * "category","flag","video_type".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"in_progress_status" <br>
 * <b>Use : </b>Store some data of projects when video is getting uploaded. <br>
 * <b> Columns :</b><br>
 * "ip_project_id","ip_video_path", "ip_status".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"in_progress_project" <br>
 * <b>Use : </b>Store all the data related to in-progress projects, in which are
 * user are already participated. and process is going on with some projects. <br>
 * <b> Columns :</b><br>
 * "in_progress_project_id","in_progress_external_project_name",
 * "in_progress_final_completion_date"
 * ,"in_progress_amount","in_progress_category",
 * "in_progress_flag","in_progress_short_desc".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"questionnaire" <br>
 * <b>Use : </b>Store all the questions and answers related to survey questions
 * for particular project of user. <br>
 * <b> Columns :</b><br>
 * "que_id","ans".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"upload_video" <br>
 * <b>Use : </b>Store data related to particular uploading videos. <br>
 * <b> Columns :</b><br>
 * "upload_video_id","upload_video_name","upload_video_path","upload_video_pid",
 * "upload_video_pcat","upload_video_no".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"rewards_received" <br>
 * <b>Use : </b>Store data related to rewards which are received by the user. <br>
 * <b> Columns :</b><br>
 * "reward_received_id","reward_received_project_name",
 * "reward_received_company_name","reward_received_card_no",
 * "reward_received_amount","reward_received_date".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"rewards_redeemed" <br>
 * <b>Use : </b>Store data related to rewards which are redeemed by the user. <br>
 * <b> Columns :</b><br>
 * "reward_redeemed_id","reward_redeemed_amount", "reward_redeemed_date".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"notification" <br>
 * <b>Use : </b>Store all the notification of user. <br>
 * <b> Columns :</b><br>
 * "reward_redeemed_id","notification_message",
 * "notification_tag","notification_dead","notification_timestamp".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"video_data_new" <br>
 * <b>Use : </b>Store all the New Tab project's video data whether if it is
 * rewarded or not. <br>
 * <b> Columns :</b><br>
 * "vproject_id","video_id",
 * "video_no","video_url","video_thumb","video_instr","is_rewarded"
 * ,"reward_amount".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"video_data_inprogress" <br>
 * <b>Use : </b>Store all the In-Progress project's video data whether if it is
 * rewarded or not. <br>
 * <b> Columns :</b><br>
 * "vproject_id","video_id",
 * "video_no","video_url","video_thumb","video_instr","is_rewarded"
 * ,"reward_amount".</li>
 * <p/>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"category" <br>
 * <b>Use : </b>Store all the category list related to user <br>
 * <b> Columns :</b><br>
 * "category_id","category_name","category_state","category_image",
 * "nearest_location","distance","is_uploaded","is_lock","is_featured"
 * ,"days_left"
 * ,"locked_text","description","video_thumb","video_link","video_desc".</li>
 * <p/>
 * <br>
 * <li>
 * <b> Table Name : </b>"upload_video_for_ptx" <br>
 * <b>Use : </b>Store data related to particular uploading videos for
 * ProductTube X. <br>
 * <b> Columns :</b><br>
 * "upload_video_id","upload_video_name","upload_video_path","upload_video_cid",
 * .</li>
 * <p/>
 * </ol>
 *
 * @author Nilay Sheth
 * @author Gulnaz Ghanchi
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, CommonStrings.DATABASE_NAME, null,
                CommonStrings.DATABASE_VERSION);
    }

    /**
     * Creates all the tables in off line database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CommonStrings.CREATE_TODO_TABLE);
    }

    /**
     * Upgrades all the tables of database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        this.onCreate(db);
    }
}
