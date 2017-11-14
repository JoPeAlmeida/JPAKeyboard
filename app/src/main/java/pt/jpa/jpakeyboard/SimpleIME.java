package pt.jpa.jpakeyboard;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.content.FileProvider;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.io.InputStream;

/**
 * Created by sebasi on 21/07/2017.
 */

public class SimpleIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private static final String TAG = "SimpleIME";
    private static final String AUTHORITY = "pt.jpa.jpakeyboard.ime.inputcontent";
    private File file;

    private boolean caps = false;


    private static File getFileForResource(
            @NonNull Context context, @RawRes int res, @NonNull File outputDir,
            @NonNull String filename) {
        final File outputFile = new File(outputDir, filename);
        final byte[] buffer = new byte[4096];
        InputStream resourceReader = null;
        try {
            try {
                resourceReader = context.getResources().openRawResource(res);
                OutputStream dataWriter = null;
                try {
                    dataWriter = new FileOutputStream(outputFile);
                    while (true) {
                        final int numRead = resourceReader.read(buffer);
                        if (numRead <= 0) {
                            break;
                        }
                        dataWriter.write(buffer, 0, numRead);
                    }
                    return outputFile;
                } finally {
                    if (dataWriter != null) {
                        dataWriter.flush();
                        dataWriter.close();
                    }
                }
            } finally {
                if (resourceReader != null) {
                    resourceReader.close();
                }
            }
        } catch (IOException e) {
            Log.e("FIle Resource", "DEU MERDA");
            return null;
        }
    }

    @Override
    public View onCreateInputView() {

        keyboardView = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        FontsOverride.setDefaultFont(this, "DEFAULT", "jpaFont.ttf");
        return keyboardView;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                keyboardView.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setPackage(getCurrentAppPackage(getApplicationContext(), getCurrentInputEditorInfo()));
                sendIntent.setType("image/png");
                final File imagesDir = new File(getFilesDir(), "images");
                imagesDir.mkdirs();
                file = getFileForResource(this, R.raw.dessert_android, imagesDir, "image.png");
                Uri uri = FileProvider.getUriForFile(this, AUTHORITY, file);
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(sendIntent);
                ic.setComposingText(String.valueOf(code), 1);*/
        }
    }

    public String getCurrentAppPackage(Context context, EditorInfo info) {
        if(info != null && info.packageName != null) {
            return info.packageName;
        }
        final PackageManager pm = context.getPackageManager();
        //Get the Activity Manager Object
        ActivityManager aManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //Get the list of running Applications
        List<ActivityManager.RunningAppProcessInfo> rapInfoList = aManager.getRunningAppProcesses();
        //Iterate all running apps to get their details
        for (ActivityManager.RunningAppProcessInfo rapInfo : rapInfoList) {
            //error getting package name for this process so move on
            if (rapInfo.pkgList.length == 0) {
                Log.i("DISCARDED PACKAGE", rapInfo.processName);
                continue;
            }
            try {
                PackageInfo pkgInfo = pm.getPackageInfo(rapInfo.pkgList[0], PackageManager.GET_ACTIVITIES);
                return pkgInfo.packageName;
            } catch (PackageManager.NameNotFoundException e) {
                // Keep iterating
            }
        }
        return null;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {

    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }
}
