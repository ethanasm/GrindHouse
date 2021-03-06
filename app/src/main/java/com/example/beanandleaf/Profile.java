package com.example.beanandleaf;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import database.DatabaseHelper;

public class Profile extends Fragment {




    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.fragment_profile, null);
    }



    public void onViewCreated(View view, Bundle savedInstanceState) {
        final View vx = view;
        final EditText usernameText = view.findViewById(R.id.name_edit);
        final EditText emailText = view.findViewById(R.id.email_edit);
        final RadioGroup genderOptions = view.findViewById(R.id.genderOptions);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        final String username = pref.getString("username", null);
        final String email = pref.getString("email", null);
        final String gender = pref.getString("gender", null);
        final String userType = pref.getString("userType", null);

        usernameText.setText(username);
        emailText.setText(email);

        if (gender.contentEquals("Male")) {
            genderOptions.check(R.id.male);
        }
        else if (gender.contentEquals("Female")) {
            genderOptions.check(R.id.female);
        }
        else {
            genderOptions.check(R.id.other);
        }

        Button updateButton = view.findViewById(R.id.update_profile);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newGenderId =  genderOptions.getCheckedRadioButtonId();
                RadioButton genderButton = vx.findViewById(newGenderId);
                String newGender = genderButton.getText().toString();
                String newUsername = usernameText.getText().toString();
                String newEmail = emailText.getText().toString().toLowerCase(); //making sure log in is not case sensitive

                DatabaseHelper db = new DatabaseHelper(getActivity());
                ArrayList<String> updates = new ArrayList<>();
                SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = preferences.edit();

                boolean error = false;

                if (!newUsername.contentEquals(username)) {
                    if (isUsernameValid(newUsername)) {
                        updates.add("username");
                        if (db.updateUserName(email, userType, newUsername))
                            editor.putString("username", newUsername);
                        else
                            error = true;
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter your first and last name separated by a space", Toast.LENGTH_LONG).show();
                        usernameText.setText(username);
                    }

                }
                if (!newEmail.contentEquals(email)) {
                    if (isEmailValid(newEmail)) {
                        updates.add("email");
                        if (db.updateUserEmail(email, userType, newEmail))
                            editor.putString("email", newEmail);
                        else
                            error = true;
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
                        emailText.setText(email);
                    }

                }
                if (!newGender.contentEquals(gender)) {
                    updates.add("gender");
                    if (db.updateUserGender(email, userType, newGender))
                        editor.putString("gender", newGender);
                    else
                        error = true;
                }
                editor.commit();
                String updateString;
                if (updates.size() == 1) {
                    updateString = "Your " + updates.get(0) + " has been updated";
                }
                else if (updates.size() == 2) {
                    updateString = "Your " + updates.get(0) + " and " + updates.get(1) + " have been updated";
                }
                else if (updates.size() == 3) {
                    updateString = "Your username, email, and gender have been updated";
                }
                else {
                    updateString = "No updates have been made";
                }
                if (!error) {
                    Toast.makeText(getActivity().getApplicationContext(), updateString, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error: please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button changePasswordBtn = view.findViewById(R.id.change_password);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id;
                Fragment fragment = new ChangePassword();
                if (userType.contentEquals("Customer")) {
                    id = R.id.fragment_container_customer;
                }
                else {
                    id = R.id.fragment_container_merchant;
                }
                getFragmentManager()
                        .beginTransaction()
                        .replace(id, fragment)
                        .commit();
            }
        });

    }

    private boolean isUsernameValid(String username) {
        return username != null && username.split(" ").length > 1;
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return false;
        }
    }

}

