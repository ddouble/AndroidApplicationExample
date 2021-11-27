package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {


    private CountDownTimer mCountDownTimer = null;
    private ProgressBar mProgressBar;
    private TextView txtProgress;

    private CountDownTimer mRandomNumberGeneratorTimer;
    private Button btnGenerateRandomNumber;
    String generateRandomNumberButtonText = null;
    ArrayList<Integer> randomNumbers = new ArrayList<Integer>();
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setProgress(0);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        btnGenerateRandomNumber = (Button) findViewById(R.id.btnGenerateRandomNumber);

        setTitle("My Application");
    }

    public void btnProgressBarOnClick(View view) {
        if (mCountDownTimer == null) {

            mCountDownTimer = new CountDownTimer(500000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
//                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);

                    int current = mProgressBar.getProgress();
                    if (current >= 100) {
                        mProgressBar.setProgress(0);
                        txtProgress.setText(String.valueOf(current) + "%");

                    } else {
                        int newProgress = current + 10;
                        mProgressBar.setProgress(newProgress);
                        txtProgress.setText(String.valueOf(newProgress) + "%");

                    }
                }

                @Override
                public void onFinish() {
                    //Do what you want
//                mProgressBar.setProgress(100);
                }

            };

            mCountDownTimer.start();

        } else {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }


    }

    private void saveRandomDataToCSV() {

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

            String csv_data = (String) randomNumbers.stream().map(Object::toString).collect(Collectors.joining("\n"));/// your csv data as string;
            File rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

//            //if you want to create a sub-dir
//            root = new File(root, "SubDir");
//            root.mkdir();

            // select the name for your file
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();

            String filename = "random-" + dtf.format(now) + ".csv";
            File csvFile = new File(rootDir, filename);

            try {
                FileOutputStream fout = new FileOutputStream(csvFile);
                fout.write(csv_data.getBytes());

                fout.close();

                Toast.makeText(getApplicationContext(), filename + " \nsaved in Downloads", Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();

                boolean bool = false;
                try {
                    // try to create the file
                    bool = csvFile.createNewFile();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (bool) {
                    // call the method again
                    saveRandomDataToCSV();

                } else {
                    throw new IllegalStateException("Failed to create image file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
//            showInContextUI(...);

            // You can directly ask for the permission.
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            // You can directly ask for the permission.
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case 0:
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 &&
//                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission is granted. Continue the action or workflow
//                    // in your app.
//                } else {
//                    // Explain to the user that the feature is unavailable because
//                    // the features requires a permission that the user has denied.
//                    // At the same time, respect the user's decision. Don't link to
//                    // system settings in an effort to convince the user to change
//                    // their decision.
//                }
//                return;
//        }
//        // Other 'case' lines to check for other
//        // permissions this app might request.
//
//    }

    public void btnGenerateRandomNumberOnClick(View view) {
        Button self = (Button) view;
        if (mRandomNumberGeneratorTimer == null) {

            mRandomNumberGeneratorTimer = new CountDownTimer(50000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
//                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                    Random rand = new Random();
                    randomNumbers.add(rand.nextInt());

                    if (generateRandomNumberButtonText == null)
                        generateRandomNumberButtonText = self.getText().toString();

                    self.setText(String.valueOf(randomNumbers.size()) + " number generated ... ");
                }

                @Override
                public void onFinish() {
                    //Do what you want
//                mProgressBar.setProgress(100);
                }

            };

            mRandomNumberGeneratorTimer.start();

        } else {
            mRandomNumberGeneratorTimer.cancel();
            mRandomNumberGeneratorTimer = null;

            saveRandomDataToCSV();

            randomNumbers.clear();
            self.setText(generateRandomNumberButtonText);

        }
    }

    public void btnBluetoothOnClick(View view) {
        ListView lvBluetoothDevices = (ListView) findViewById(R.id.lvBluetoothDevices);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.BLUETOOTH_ADMIN) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

//            List<String> s = new ArrayList<String>();
//            s.add("abcf");
//            s.add("fgerg");
//            s.add("efwwoi");
//
//            lvBluetoothDevices.setAdapter(
//                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s)
//            );

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "Please turn on Bluetooth in Settings", Toast.LENGTH_LONG).show();
            }
            if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled())
            {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                List<String> s = new ArrayList<String>();
                for (BluetoothDevice bt : pairedDevices)
                    s.add(bt.getName());

                lvBluetoothDevices.setAdapter(
                        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s)
                );
            }


        } else if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_ADMIN)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
//            showInContextUI(...);

            // You can directly ask for the permission.
            requestPermissions(
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    2);
        } else {
            // You can directly ask for the permission.
            requestPermissions(
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    2);
        }
    }
}