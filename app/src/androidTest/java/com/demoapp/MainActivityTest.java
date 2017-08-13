package com.demoapp;

import android.database.SQLException;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.TextView;

import com.demoapp.Models.UserModel;
import com.sqlitemanager.SQLiteManager;

import junit.extensions.ActiveTestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aslan on 8/13/2017.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);


    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
        UserModel userModel = new UserModel();
        userModel.profilePicture = "some image uri";
        userModel.fullname = "First Last name";
        userModel.age = 12;

        long feedback = userModel.insert();
        assertTrue(feedback > 0);
        UserModel newUserModel = new UserModel();
        newUserModel.fill(2);
        assertTrue(newUserModel.id == 2);
        assertNotNull(newUserModel.fullname);
        assertNotNull(newUserModel.profilePicture);
        assertNotEquals((long) newUserModel.age, 0);

        UserModel userModel1 = SQLiteManager.find(UserModel.class, 3);
        assertNotNull(userModel);
        userModel1.fullname = "sdAmiraslan Bakhshili";
        assertTrue(userModel1.update() > 0);
    }

    @Test
    public void testLaunch() {
        TextView view = (TextView) mainActivity.findViewById(R.id.myTextView);
        assertNotNull(view);
        assertEquals(view.getText(), mainActivity.getResources().getString(R.string.my_name));
    }

    @After
    public void tearDown() throws Exception {

    }

}