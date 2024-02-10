package com.ynov.vernet.botbubulle.callback;

public interface CronCallback {
    void onCronReceived(int[] cron);
    void onFailure(String errorMessage);
}
