package com.example.beanandleaf;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseHelper;


public class AddMenuItem extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_menu_item, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        final TextView nameEdit = view.findViewById(R.id.item_name_edit);
        final TextView calSmallEdit = view.findViewById(R.id.calories_for_small_edit);
        final TextView calMediumEdit = view.findViewById(R.id.calories_for_medium_edit);
        final TextView calLargeEdit = view.findViewById(R.id.calories_for_large_edit);
        final TextView cafSmallEdit = view.findViewById(R.id.caffeine_small_edit);
        final TextView cafMediumEdit = view.findViewById(R.id.caffeine_medium_edit);
        final TextView cafLargeEdit = view.findViewById(R.id.caffeine_large_edit);
        final TextView priceSmallEdit = view.findViewById(R.id.price_small_edit);
        final TextView priceMediumEdit = view.findViewById(R.id.price_medium_edit);
        final TextView priceLargeEdit = view.findViewById(R.id.price_large_edit);
        final Button addItemButton = view.findViewById(R.id.add_item);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String calSmall = calSmallEdit.getText().toString();
                String calMedium = calMediumEdit.getText().toString();
                String calLarge = calLargeEdit.getText().toString();
                String cafSmall = cafSmallEdit.getText().toString();
                String cafMedium = cafMediumEdit.getText().toString();
                String cafLarge = cafLargeEdit.getText().toString();
                String priceSmall = priceSmallEdit.getText().toString();
                String priceMedium = priceMediumEdit.getText().toString();
                String priceLarge = priceLargeEdit.getText().toString();
                boolean addSmall = false;
                boolean addMedium = false;
                boolean addLarge = false;
                if (name.contentEquals(""))
                    return;


                if (cafSmall.contentEquals("") && !priceSmall.contentEquals("") && !calSmall.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the amount of caffeine in mg for the small size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafSmall.contentEquals("") && priceSmall.contentEquals("") && !calSmall.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a price for the small size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafSmall.contentEquals("") && !priceSmall.contentEquals("") && calSmall.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the number of calories for the small size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafSmall.contentEquals("") && !priceSmall.contentEquals("") && !calSmall.contentEquals("")) {
                    if (!isValidInteger(cafSmall)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid caffeine amount in mg for the small size (three digits or less)", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidDouble(priceSmall)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid price for the small size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidInteger(calSmall)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid number of calories for the small size (three digits or less)", Toast.LENGTH_LONG).show();
                    }
                    else {
                        addSmall = true;
                    }
                }
                if (cafMedium.contentEquals("") && !priceMedium.contentEquals("") && !calMedium.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the amount of caffeine in mg for the medium size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafMedium.contentEquals("") && priceMedium.contentEquals("") && !calMedium.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a price for the medium size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafMedium.contentEquals("") && !priceMedium.contentEquals("") && calMedium.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the number of calories for the medium size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafMedium.contentEquals("") && !priceMedium.contentEquals("") && !calMedium.contentEquals("")) {
                    if (!isValidInteger(cafMedium)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid caffeine amount in mg for the medium size (three digits or less)", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidDouble(priceMedium)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid price for the medium size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidInteger(calMedium)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid number of calories for the medium size (three digits or less)", Toast.LENGTH_LONG).show();
                    }
                    else {
                        addMedium = true;
                    }
                }
                if (cafLarge.contentEquals("") && !priceLarge.contentEquals("") && !calLarge.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the amount of caffeine in mg for the large size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafLarge.contentEquals("") && priceLarge.contentEquals("") && !calLarge.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a price for the large size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafLarge.contentEquals("") && !priceLarge.contentEquals("") && calLarge.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the number of calories for the large size", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!cafLarge.contentEquals("") && !priceLarge.contentEquals("") && !calLarge.contentEquals("")) {
                    if (!isValidInteger(cafLarge)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid caffeine amount in mg for the large size (three digits or less)", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidDouble(priceLarge)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid price for the large size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidInteger(calLarge)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid number of calories for the large size (three digits or less)", Toast.LENGTH_LONG).show();
                    }
                    else {
                        addLarge = true;
                    }
                }

                DatabaseHelper db = new DatabaseHelper(getActivity());
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                int storeID = pref.getInt("selectedStore", 0);
                String timestamp = Long.toString(System.currentTimeMillis());
                if (addSmall || addMedium || addLarge) {
                    if (db.checkMenuItemNameExists(storeID, name)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error: an item with that name already exists in your store", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (addSmall) {
                    if (!db.insertMenuItem(storeID, name, calSmall, "Small", cafSmall, priceSmall, timestamp)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error: small menu item not added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (addMedium) {
                    if (!db.insertMenuItem(storeID, name, calMedium, "Medium", cafMedium, priceMedium, timestamp)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error: medium menu item not added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (addLarge) {
                    if (!db.insertMenuItem(storeID, name, calLarge, "Large", cafLarge, priceLarge, timestamp)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error: large menu item not added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!addSmall && !addMedium && !addLarge) {
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), name + " added to the menu", Toast.LENGTH_SHORT).show();
                Fragment menuFragment = new EditMenu();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_merchant, menuFragment)
                        .commit();
            }
        });
    }

    public boolean isValidDouble(String s) {
        String regex = "[0-9]*\\.?[0-9]+";
        String[] split = s.split("\\.");
        if (split.length == 1) {
            return s.matches(regex);
        }
        if (split.length != 2)
            return false;
        return s.matches(regex) && split[1].length() <= 2;
    }

    public boolean isValidInteger(String s) {
        String regex = "\\d+";
        return s.matches(regex) && s.length() <= 3;
    }
}
