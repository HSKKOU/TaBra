package jp.ac.titech.itpro.sdl.tabra.Activity.SpeechRecognizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hskk1120551 on 15/07/22.
 */
public class VoiceRecog implements RecognitionListener {
    private static final String TAG = SpeechRecognizer.class.getSimpleName();

    public interface OnVoiceRecog {
        public void onReciveVoice(String voiceStr);
    }

    private SpeechRecognizer mSpeechRecognizer;
    private Context mContext;

    private OnVoiceRecog delegate;

    public VoiceRecog(Context context, OnVoiceRecog delegate) {
        this.mContext = context;
        this.delegate = delegate;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
        mSpeechRecognizer.setRecognitionListener(this);
    }

    public void startRecognize() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());

        mSpeechRecognizer.startListening(intent);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Toast.makeText(mContext, "Ready for Voice Recognition", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBeginningOfSpeech() {
        Toast.makeText(mContext, "Start Input", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.v(TAG, "onBufferedRecieved");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
//        Log.v(TAG, "recieve: " + rmsdB + "dB");
    }

    @Override
    public void onEndOfSpeech() {
        Toast.makeText(mContext, "End Input", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int error) {
        String errorMsg = "";
        switch(error){
            case SpeechRecognizer.ERROR_AUDIO:
                errorMsg = "Failed save voice data";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                errorMsg = "Error in your Android";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                errorMsg = "You don't have permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                errorMsg = "Network Error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                errorMsg = "Network Timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                errorMsg = "No Match Text Data";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                errorMsg = "Cannot request to RecognitionService";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                errorMsg = "Recieved error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                errorMsg = "No Input?";
                break;
            default:
        }

        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.v(TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.v(TAG, "onPartialResults");
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> recData = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String getData = "";
        for (String s : recData) {
            getData += s + ",";
        }

        if(recData.size() > 0){
            delegate.onReciveVoice(recData.get(0));
        }

        Toast.makeText(mContext, getData, Toast.LENGTH_SHORT).show();
    }
}
