package com.example.beanandleaf;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import database.DatabaseHelper;
import model.MenuItem;
import model.Store;

public class MapClickMenu extends Fragment {

    private int storeID;

    public MapClickMenu(int storeID) {
        this.storeID = storeID;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_view_menu, null);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView title = view.findViewById(R.id.map_click_menu_title);
        DatabaseHelper db = new DatabaseHelper(getActivity());
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",0);
        String userType = pref.getString("userType", null);

        final Store selectedStore = db.getStore(storeID);
        title.setText(selectedStore.getName() + " Menu");

        TableLayout table = view.findViewById(R.id.map_click_menu_table);
        final ArrayList<MenuItem> menu = db.getMenu(storeID);
        ArrayList<Integer> created = new ArrayList<>();
        for (int i = 0; i < menu.size(); ++i) {
            boolean alreadyCreated = false;
            for (Integer n : created) {
                if (i == n) {
                    alreadyCreated = true;
                }
            }
            if (alreadyCreated)
                continue;

            TableRow row = new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setGravity(Gravity.CENTER);
            row.setLayoutParams(lp);
            TextView itemNameView = new TextView(getActivity());
            TextView descriptionView = new TextView(getActivity());
            itemNameView.setText("\n" + menu.get(i).getName() + "\n");
            MenuItem cur = menu.get(i);
            String prices = "Price = $" + String.format("%.2f",cur.getPrice());
            String caffeine = "Caffeine = " + cur.getCaffeine() + "mg";
            String calories = "Calories = " + cur.getCalories();
            if (i + 1 != menu.size()) {
                for (int j = i + 1 ; j < menu.size(); ++j) {
                    MenuItem next = menu.get(j);
                    if (next.getTimeCreated().contentEquals(cur.getTimeCreated())) {
                        prices += ", $" + String.format("%.2f",next.getPrice());
                        caffeine += ", " + next.getCaffeine() + "mg";
                        calories += ", " + next.getCalories();
                        created.add(j);
                    }
                }
            }
            descriptionView.setText(prices + " \n " + caffeine + " \n " + calories);

            TableRow.LayoutParams paramsNameText = new TableRow.LayoutParams(0);
            itemNameView.setBackgroundResource(R.drawable.black_border);
            itemNameView.setTextAppearance(R.style.fontForMenuTable);
            itemNameView.setPadding(10,10,10,10);
            itemNameView.setGravity(Gravity.CENTER);
            itemNameView.setLayoutParams(paramsNameText);

            TableRow.LayoutParams paramsDescText = new TableRow.LayoutParams(1);
            descriptionView.setBackgroundResource(R.drawable.black_border);
            descriptionView.setTextAppearance(R.style.fontForMenuTable);
            descriptionView.setPadding(10,10,10,10);
            descriptionView.setGravity(Gravity.CENTER);
            descriptionView.setLayoutParams(paramsDescText);

            row.addView(itemNameView);
            row.addView(descriptionView);
            table.addView(row);

            Button recordOrderButton = view.findViewById(R.id.record_order_button);
            if (userType.contentEquals("Merchant")) {
                recordOrderButton.setVisibility(View.GONE);
            }
            else {
                recordOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment addOrderFragment = new AddOrder(storeID);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_customer, addOrderFragment)
                                .commit();
                    }
                });
            }

        }
    }

}
