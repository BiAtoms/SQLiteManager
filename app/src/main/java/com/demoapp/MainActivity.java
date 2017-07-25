package com.demoapp;

import android.database.sqlite.SQLiteQueryBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.demoapp.Models.CarModel;
import com.demoapp.Models.CityModel;
import com.demoapp.Models.DriverModel;
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

//        sqliteManager = new SQliteManager.Builder(getApplicationContext())
//                .setTableList(new ArrayList<Class>() {{
//                    add(WeightModel.class);
//                    add(ActivenessModel.class);
//                    add(FoodModel.class);
//                    add(MealTimeModel.class);
//                    add(ConsumedFoodModel.class);
//                    add(WaterModel.class);
//                    add(StepModel.class);
//                    add(UserImage.class);
//                }})
//                .buildDatabase();

//Example:  foods  FoodModel  foodModel

        // Initialize singleton SqLiteManager;
        new SQLiteManager.Builder(this)
                .setDBName("my_new_db")
                .setTableList(
                        CarModel.class,
                        DriverModel.class,
                        CityModel.class,
                        UserModel.class)
                .buildDatabase();

        Long[] logs = new Long[3];

        // Add tables to be created
        UserModel userModel = new UserModel();
        userModel.age = 14;
        userModel.profilePicture = "31231 as a asd aaaaa";
        userModel.fullname = "Amras ll alsk";

        userModel.insert();

        Log.i(TAG, logs[0] + "");

        DriverModel driver = new DriverModel();
        driver.birtdate = "132391";
        driver.fullname = "afkjkhfaa";
        driver.email = "asddafkjfaaff";

        logs[1] = driver.insert();
        Log.i(TAG, logs[0] + "");


        // Set data to a table

//
//        ArrayList<DriverModel> asdas = SQLiteManager.all(DriverModel.class);
//        ArrayList<CityModel> cityModels = SQLiteManager.all("cities");
//        ArrayList<UserModel> users  = SQLiteManager.all(UserModel.class);
//
//
//    ArrayList<DriverModel> asdasda= SQLiteManager.all(DriverModel.class);
//        SQLiteManager.Selector asdd  = new SQLiteManager.Selector();
//
//
//        ArrayList<DriverModel> driverModels = SQLiteManager.all(DriverModel.class).get();
//
//        ArrayList<UserModel> userModels = new
//                SQLiteManager.Select(DriverModel.class)
//                        .select("drivers")
//                        .where("id>? and name=?", "12", "name")
//                        .whereIn()
//                        .sort(SortOrder.Desc)
//                        .limit()
//                        .joinWith()
//                        .get();
//
//
//                        .random()
//

        ArrayList<UserModel> userModels = new SQLiteManager
                .Select("driver")
                .where("id>? and name=?", "12", "name")
                .sort(SortOrder.ASC)
                .limit(5)
                .innerJoin("carId")
                .columns("id", "email")
                .get();


        ArrayList<CityModel> userMojdels = SQLiteManager.all("drivers");


        ArrayList<DriverModel> arrayList = new SQLiteManager
                .Select("asda")
                .get();


        Log.i(TAG, logs[0] + "");


        // Get (Select) with condition

        this.deleteDatabase(SQLiteManager.getInstance().getDatabaseName());

        // Get (Select) with SortOrder

        // Get (Select) with Limit (certain amount)

        // Get (Select) random data


        // Update in table

        // Delete data from DB with condition

        // Query Execute method

        // Select data from several tables with Foreign Key


    }
}
