package com.bjtu.pose.utils;

import android.os.AsyncTask;

public class LogUtils {
    public static String logFilePath = null;

    public static void printLog(String level,String time,String fileName, Integer line, String info){
        String dataString = "[" + level + "]  " + time + "  " + fileName + "  " + line + "  \"" + info + "\"";
        String[] contents = {dataString, logFilePath};
        writeWork writeWork = new writeWork();
        writeWork.execute(contents);
    }

    public static class writeWork extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            FileOperation fileOperation = new FileOperation();
            fileOperation.writeCsv(strings[0], strings[1]);
            return null;
        }
    }
}
