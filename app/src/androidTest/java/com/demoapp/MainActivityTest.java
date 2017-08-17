package com.demoapp;

import android.database.SQLException;
import android.support.test.espresso.core.deps.guava.primitives.Booleans;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.demoapp.Models.UserModel;
import com.sqlitemanager.SQLiteManager;

import junit.extensions.ActiveTestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aslan on 8/13/2017.
 */
public class MainActivityTest {

    public final static String TAG = "MainActivityTestTAG";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testUI() {
        TextView view = (TextView) mainActivity.findViewById(R.id.myTextView);
        assertNotNull(view);
        assertEquals(view.getText(), mainActivity.getResources().getString(R.string.my_name));
    }

    @Test
    public void testInsertData() {
        UserModel userModel = new UserModel();
        userModel.profilePicture = "some image uri";
        userModel.fullname = "First Last name";
        userModel.age = 12;
        assertTrue(userModel.insert() > 0);
    }

    @Test
    public void testUpdateData() {
        UserModel userModel = new UserModel();
        userModel.profilePicture = "some imajhge uri" + AppUtils.randomWithRange(1, 100000);
        userModel.fullname = "Firs,pt Lasts name" + AppUtils.randomWithRange(1, 100000);
        userModel.age = AppUtils.randomWithRange(2, 1000);

        List<UserModel> userModels = new SQLiteManager.Select(UserModel.class).limit(1).getList();
        userModel.id = userModels.get(0).id;

        long updateFeedback = userModel.update();
        Log.d(TAG, updateFeedback + " | testUpdateData() , updateFeedback");

    }

    @Test
    public void testFillData() {
        UserModel newUserModel = new UserModel();
        newUserModel.fill(-1);
        assertTrue(newUserModel.id == 0);

        Log.d(TAG, newUserModel.id + " | fill");

        List<UserModel> userModels = new SQLiteManager.Select(UserModel.class).limit(1).getList();
        newUserModel.fill(userModels.get(0).id);

        assertTrue(newUserModel.id == userModels.get(0).id);
        assertTrue(newUserModel.fullname.equals(userModels.get(0).fullname));
        assertTrue(newUserModel.profilePicture.equals(userModels.get(0).profilePicture));
        assertTrue((long) newUserModel.age == userModels.get(0).age);

        UserModel userModel1 = SQLiteManager.find(UserModel.class, userModels.get(0).id);
        assertNotNull(userModel1);
        assertTrue(userModel1.id == userModels.get(0).id);
        assertTrue(userModel1.fullname.equals(userModels.get(0).fullname));
        assertTrue(userModel1.profilePicture.equals(userModels.get(0).profilePicture));
        assertTrue((long) userModel1.age == userModels.get(0).age);

    }

    @Test
    public void testDeleteData() {
        UserModel newUserModel = new UserModel();
        List<UserModel> userModels = new SQLiteManager.Select(UserModel.class).limit(1).getList();
        newUserModel.id = userModels.get(0).id;
        assertTrue(newUserModel.delete() > 0);
    }

    @Test
    public void testDataExists() {
        List<UserModel> userModels = new SQLiteManager.Select(UserModel.class).limit(1).getList();

        Boolean isFull = SQLiteManager.exists("users", userModels.get(0).id);

        assertTrue(isFull);

        assertTrue(SQLiteManager.delete("users", userModels.get(0).id) > 0);

        isFull = SQLiteManager.exists("users", userModels.get(0).id);
        Log.d(TAG, isFull + " | testDataExists() , isFull");
        assertTrue(!isFull);
    }

    @Test
    public void testFindAndFill() {
//        Boolean exists = SQLiteManager.exists("users", 2);
//        Log.d(TAG, exists + " | exists testFindAndFill() , id first");
//
//        if (exists) {
//            UserModel userModel1 = SQLiteManager.find(UserModel.class, 2);
//            assertNotNull(userModel1);
//            Log.d(TAG, userModel1.id + " | testFindAndFill() , id first");
//
//            userModel1.fullname = "sdAmiraslan Bakhshili";
//            userModel1.age = 34;
//
//            UserModel userModel2 = new UserModel();
//            userModel2.fill(2);
//            assertTrue(userModel1.id == userModel2.id);
//            Log.d(TAG, userModel2.id + " | testFindAndFill() , id second");
//
//            assertTrue(userModel1.delete() > 0);
//        } else {
//            List<UserModel> userModels = new SQLiteManager.Select("users").getList();
//            if (userModels.size() > 0) {
//                UserModel userModel1 = SQLiteManager.find(UserModel.class, userModels.get(0).id);
//                assertNotNull(userModel1);
//                Log.d(TAG, userModel1.id + " | testFindAndFill() , id first");
//
//                userModel1.fullname = "sdAmiraslan Bakhshili";
//                userModel1.age = 34;
//
//                UserModel userModel2 = new UserModel();
//                userModel2.fill(userModel1.id);
//                assertTrue(userModel1.id == userModel2.id);
//                Log.d(TAG, userModel2.id + " | testFindAndFill() , id second");
//                assertTrue(userModel1.delete() > 0);
//            }
//        }
    }

    @After
    public void tearDown() throws Exception {

    }
}