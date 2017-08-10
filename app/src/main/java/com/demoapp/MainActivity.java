package com.demoapp;

import android.database.sqlite.SQLiteQueryBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.demoapp.Models.CarModel;
import com.demoapp.Models.CityModel;
import com.demoapp.Models.DriverModel;
import com.demoapp.Models.SmthModel;
import com.demoapp.Models.UserModel;
import com.sqlitemanager.SQLiteManager;
import com.sqlitemanager.SortOrder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize singleton SqLiteManager;
        //Todo: Take into accoint upgrade stuff in version number and Handle it in Annotations, OK?
        new SQLiteManager.Builder(this)
                .setDBName("my_new_db")
                .setTableList(
                        CarModel.class,
                        DriverModel.class,
                        CityModel.class,
                        UserModel.class,
                        SmthModel.class)
                .buildDatabase();


        // Creating model
        UserModel userModel = new UserModel();
        userModel.id = 5;
        userModel.profilePicture = "some image uri";
        userModel.fullname = "First Last name";

        //Inserting it to the table
        long info1 = userModel.insert();


        //Creating model
        DriverModel driver = new DriverModel();
        driver.birtdate = "12-01-12";
        driver.fullname = "Fullname here";
        driver.email = "Email here";
        driver.id = 2;

        //Inserting to the table
        long info = driver.insert();


        //Get all data with type
        ArrayList<DriverModel> drivers = SQLiteManager.all(DriverModel.class);

        //Get all data with table name
        ArrayList<CityModel> cities = SQLiteManager.all("cities");

    //    SQLiteManager.deleteDatabase();

//        ArrayList<UserModel> userModels = new SQLiteManager
//                .Select("driver")
//                .where("id>? and name=?", "12", "name")
//                .sort(SortOrder.ASC)
//                .limit(5)
//                .innerJoin("carId")
//                .columns("id", "email")
//                .get();
//
//
//        ArrayList<UserModel> usersArrayList = SQLiteManager.all("users");
//
//        ArrayList<DriverModel> arrayList = new SQLiteManager
//                .Select("asda")
//                .get();
//
//
//
//
//
//        // Get (Select) with condition
//
//        this.deleteDatabase(SQLiteManager.getInstance().getDatabaseName());

        // Get (Select) with SortOrder

        // Get (Select) with Limit (certain amount)

        // Get (Select) random data


        // Update in table

        // Delete data from DB with condition

        // Query Execute method

        // Select data from several tables with Foreign Key


    }
}
