package com.example.beanandleaf;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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


public class AddMenuItemFragment extends Fragment {
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
        final Button addItemButton = view.findViewById(R.id.add_item_button);

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
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the amount of caffeine for the small size", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid caffeine amount for the small size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidDouble(priceSmall)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid price for the small size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidInteger(calSmall)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid number of calories for the small size", Toast.LENGTH_LONG).show();
                    }
                    else {
                        addSmall = true;
                    }
                }
                if (cafMedium.contentEquals("") && !priceMedium.contentEquals("") && !calMedium.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the amount of caffeine for the medium size", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid caffeine amount for the medium size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidDouble(priceMedium)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid price for the medium size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidInteger(calMedium)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid number of calories for the medium size", Toast.LENGTH_LONG).show();
                    }
                    else {
                        addMedium = true;
                    }
                }
                if (cafLarge.contentEquals("") && !priceLarge.contentEquals("") && !calLarge.contentEquals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter the amount of caffeine for the large size", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid caffeine amount for the large size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidDouble(priceLarge)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid price for the large size", Toast.LENGTH_LONG).show();
                    }
                    else if (!isValidInteger(calLarge)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please enter a valid number of calories for the large size", Toast.LENGTH_LONG).show();
                    }
                    else {
                        addLarge = true;
                    }
                }

                DatabaseHelper db = new DatabaseHelper(getActivity());
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                int storeID = pref.getInt("selectedStore", 0);

                if (addSmall) {
                    if (!db.insertMenuItem(storeID, name, calSmall, "Small", cafSmall, priceSmall)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error: small menu item not added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (addMedium) {
                    if (!db.insertMenuItem(storeID, name, calMedium, "Medium", cafMedium, priceMedium)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error: medium menu item not added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (addLarge) {
                    if (!db.insertMenuItem(storeID, name, calLarge, "Large", cafLarge, priceLarge)) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error: large menu item not added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!addSmall && !addMedium && !addLarge) {
                    return;
                }
                Toast.makeText(getActivity().getApplicationContext(), name + " added to the menu", Toast.LENGTH_SHORT).show();
                Fragment menuFragment = new MenuFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_merchant, menuFragment)
                        .commit();
            }
        });
    }

    private boolean isValidDouble(String s) {
        String regex = "[0-9]*\\.?[0-9]+";
        return s.matches(regex);
    }

    private boolean isValidInteger(String s) {
        String regex = "\\d+";
        return s.matches(regex);
    }
}
