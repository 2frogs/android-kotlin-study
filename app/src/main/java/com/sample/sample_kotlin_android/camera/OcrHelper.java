package com.sample.sample_kotlin_android.camera;



import android.graphics.Bitmap;
import android.os.Environment;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;


/**
 * 图像识别工具类
 */
public class OcrHelper {

    private TessBaseAPI mTess = null;

    public OcrHelper() {
        initTessBase();
    }

    /**
     * 初始化TessBaseApi
     * @return
     */
    public String initTessBase() {
        mTess = new TessBaseAPI();
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return "SD CARD is not ready";
        }
        String lang = "eng";
        String dataPath = Environment.getExternalStorageDirectory()
                                     .getAbsolutePath() + "/ATest/";
        try {
            /* 语言包目录必须是tessdata */
            File tessDataDir = new File(dataPath + "tessdata");
            if (!tessDataDir.exists()) {
                tessDataDir.mkdirs();
            }
            checkAndCopyTrainFile(tessDataDir);
            /* 使用eng.traineddata语言包,需放在dataPath的tessdata目录下 */
            mTess.init(dataPath, lang);
        } catch (Exception e) {
            return "exception catched";
        }
        return null;
    }

    public String getOcrResult(Bitmap bitmap) {
        mTess.setImage(bitmap);
        return mTess.getUTF8Text();
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }

    private void checkAndCopyTrainFile(File dir) {

    }

}
