package com.example.tomczas_med;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

public class InternetCheckTask extends AsyncTask<Void, Void, Boolean> {

    private Activity activity;
    private boolean isRunning = true;

    public InternetCheckTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        while (isRunning) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!isNetworkAvailable()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean isConnected) {
        if (!isConnected) {
            showNoInternetDialogAndExit();
        }
    }

    public void stopChecking() {
        isRunning = false;
        cancel(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void showNoInternetDialogAndExit() {
        activity.runOnUiThread(() -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Brak połączenia internetowego")
                    .setMessage("Aplikacja wymaga aktywnego połączenia internetowego do działania. Połącz się z internetem i uruchom aplikację ponownie.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        activity.finishAffinity();
                        System.exit(0);
                    })
                    .setCancelable(false)
                    .show();
        });
    }
}