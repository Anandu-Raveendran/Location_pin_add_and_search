package com.example.anandu_sem2_final_project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.anandu_sem2_final_project.Database.AppDatabase;
import com.example.anandu_sem2_final_project.Database.UserData;
import com.example.anandu_sem2_final_project.databinding.ActivityEditBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityEditBinding binding;
    private UserData selectedUserData = null;
    private Bitmap selectedImage;

    private AppDatabase db;
    private Date birthday;
    private int LOCATION_REQUEST = 100, CAMERA_REQUEST = 200;
    private ArrayList<String> spinnerArray = new ArrayList<String>(Arrays.asList("Male", "Female", "Other"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getDbInstance(getApplicationContext());

        ArrayAdapter<String> spinnerAdaptor = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        binding.genderSpinner.setAdapter(spinnerAdaptor);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedUserData = (UserData) bundle.getSerializable("SelectedItem");
            if (selectedUserData != null) {
                Log.i("anandu", "selected user is NOT null");
                binding.name.setText(selectedUserData.getName());
                int index = spinnerArray.indexOf(selectedUserData.getGender());
                binding.genderSpinner.setSelection(index);
                birthday = selectedUserData.getBirthday();
                binding.birthdayBtn.setText(DateFormat.getDateInstance().format(birthday.getTime()));
                binding.lattitudeEditText.setText(String.valueOf(selectedUserData.getLatitude()));
                binding.longitudeEditText.setText(String.valueOf(selectedUserData.getLongitude()));
                binding.countryBtn.setText(selectedUserData.getCountry());
                binding.deleteBtn.setVisibility(View.VISIBLE);
                selectedImage =  BitmapFactory.decodeByteArray(selectedUserData.getImage(),0,selectedUserData.getImage().length);
                binding.imageView2.setImageBitmap(selectedImage);
            } else {
                Log.i("anandu", "selected user is null");
                binding.deleteBtn.setVisibility(View.GONE);
            }
        }

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.userDao().delete(selectedUserData);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        binding.birthdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment(EditActivity.this);
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        binding.countryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, CountryListActivity.class);
                startActivityForResult(intent, LOCATION_REQUEST);
            }
        });
        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(v);
            }
        });
    }

    private void openGallery(View v) {
        Log.i("anandu", "OpenGallery called");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Title"), CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == LOCATION_REQUEST) {
            binding.countryBtn.setText(data.getStringExtra("country"));
        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST) {
            Uri uri = data.getData();
            binding.imageView2.setImageURI(uri);
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                Log.e("anandu", "Couldnt convert uri to bitmap");
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Save").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String message = "";
                if (binding.name.getText().toString().isEmpty()) {
                    message = "Title is empty";
                } else if (binding.longitudeEditText.getText().toString().isEmpty()) {
                    message += " Longitude is empty";
                } else if (binding.lattitudeEditText.getText().toString().isEmpty()) {
                    message += " Latitude is empty";
                } else {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();

                    if (selectedUserData == null) {
                        binding.imageView2.setImageResource(R.drawable.ic_launcher_foreground);
                        selectedUserData = new UserData(0,
                                binding.name.getText().toString(),
                                spinnerArray.get(binding.genderSpinner.getSelectedItemPosition()),
                                birthday,
                                binding.countryBtn.getText().toString(),
                                Double.valueOf(String.valueOf(binding.lattitudeEditText.getText())),
                                Double.valueOf(String.valueOf(binding.longitudeEditText.getText())), q);
                        db.userDao().inserData(selectedUserData);
                    } else {
                        selectedUserData.setName(binding.name.getText().toString());
                        selectedUserData.setGender(spinnerArray.get(binding.genderSpinner.getSelectedItemPosition()));
                        selectedUserData.setCountry(binding.countryBtn.getText().toString());
                        selectedUserData.setLatitude(Double.valueOf(binding.lattitudeEditText.getText().toString()));
                        selectedUserData.setLatitude(Double.valueOf(binding.longitudeEditText.getText().toString()));
                        selectedUserData.setImage(bytes);
                        db.userDao().update(selectedUserData);
                    }
                    finish();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());
        binding.birthdayBtn.setText(currentDateString);
        birthday = c.getTime();
    }
}