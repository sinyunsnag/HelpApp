package com.helpapplication.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by massivcode on 2017-02-25.
 */

public class FileHelper {
    // 파일이 업로드 될 경로 지정
    public static final String APP_FOLDER_PATH =
            Environment.getExternalStorageDirectory().getAbsoluteFile()
                    + File.separator
                    + "records"
                    + File.separator;

    /**
     * 파일을 저장할 폴더 생성
     */
    public static boolean createDirectory() {
        boolean result = false;

        File dir = new File(APP_FOLDER_PATH);

        if (!dir.exists()) {
            result = dir.mkdir();
        }

        return result;
    }
}
