package com.example.anandu_sem2_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.anandu_sem2_final_project.databinding.CountryListBinding;


/**
 * A fragment representing a list of Items.
 */
public class CountryListActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CountryListBinding binding = CountryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String countries[] = {"Canada", "USA", "India", "China", "Pakistan", "UK", "France", "Spain", "Brazil", "Mexico" };
        binding.list.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, countries));
        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("anandu", "country: " + countries[position]);
                Intent intent = new Intent();
                intent.putExtra("country", countries[position]);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}