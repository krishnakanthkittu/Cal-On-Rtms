package cal_on.usbterminal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Coverage extends AppCompatActivity {

    private final String TAG = Coverage.class.getSimpleName();
 //   BTConnection uConn;
    private UsbManager mUsbManager;
    TextToSpeech t1;
    private ListView mListView;
    private static UsbSerialPort sPort = null;
    UsbDeviceConnection connection;
    private TextView mDumpTextView;
    private ScrollView mScrollView;
    private BroadcastReceiver mUsbReceiver;
    private String totalData = "";
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    private UsbManager usbManager;
    private PendingIntent permissionIntent;
    private Spinner baudrate;
    TextView filePath;
    String color="#CCCCCC";
    String getColor="#E6DB83";
    EditText deviceid;
    private Boolean exit = false;
    UsbEndpoint endpointOut = null;
    Button Off_Line,On_Line,Time_date,Rtms_Url,CalOn_Url,rtms_up,calon_up,Reset,count,logid,cat_log_onD,cat_log_offd;
    //    TextView consoleTextUSB;


    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);

                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);


                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);

                                if (s.endsWith("#")) {

                                    try {
                                        int usbResult;
                                        String str1 = "U";
                                        byte[] bytesOut = str1.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        // Off_Line.setBackgroundColor(android.R.id.background);
                                        Off_Line.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in OFF Line Mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "OFF_Line _OK", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured In Off Line mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "OFF_Line _Not_OK", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }
                            catch (Exception e) {

                            }

                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListener1 =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);

                                if (s.endsWith("#")) {

                                    try {
                                        int usbResult;
                                        String str1 = "O";
                                        byte[] bytesOut = str1.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        //  On_Line.setBackgroundColor(android.R.id.background);
                                        On_Line.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in ON Line Mode ";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "ON_Line _OK", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured In ON Line Mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "ON_Line _Not_OK", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                            catch (Exception e) {


                            }
                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListener2 =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);

                                if (s.endsWith("#")) {
                                    try {
                                        int usbResult;
                                        String date = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
                                        String str1 = "$";
                                        String str2 = "T";
                                        String datetime = "" + date;
                                        String total1 = str1.concat("" + str2);
                                        String total = total1.concat("" + datetime);

                                        byte[] bytesOut = total.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        // Time_date.setBackgroundColor(android.R.id.background);
                                        //  Time_date.setBackgroundColor();
                                        Time_date.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in Time Stamping Mode ";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Date_Time_Ok :  " + date, Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured in Time Stamping Mode  ";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Date_Time _Not_OK", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            catch (Exception e) {

                            }
                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListener3 =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);
                                if (s.endsWith("#")) {
                                    try {
                                        int usbResult;
                                        String str2 = "S";
                                        String str0 = "L";
                                        String str1 = "R";

                                        String total1 = str2.concat("" + str0);
                                        String total = total1.concat("" + str1);

                                        byte[] bytesOut = total.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        //   Rtms_Url.setBackgroundColor(android.R.id.background);
                                        Rtms_Url.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in RTMS  URL Mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "RTMS_URL", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured in RTMS CAl On Url Mode  ";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Can't Configured in RTMS_URL", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            catch (Exception e) {

                            }
                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListener4 =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);
                                if (s.endsWith("#")) {
                                    try {
                                        int usbResult;
                                        String str2 = "S";
                                        String str0 = "L";
                                        String str1 = "T";
                                        String total1 = str2.concat("" + str0);
                                        String total = total1.concat("" + str1);
                                        byte[] bytesOut = total.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        // CalOn_Url.setBackgroundColor(android.R.id.background);
                                        CalOn_Url.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in  Cal on URL Mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Cal-on URL", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured in  CAl On Url Mode ";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Can't Send Cal-on_URL", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            catch (Exception e) {

                            }


                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListener5 =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();

                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);
                                if (s.endsWith("#")) {
                                    try {
                                        int usbResult;
                                        String str2 = "L";
                                        String str1 = "R";
                                        String total = str2.concat("" + str1);
                                        char z1 = '\n';
                                        String string11 = Character.toString(z1);
                                        String sk=deviceid.getText().toString();
                                        char second12 = sk.charAt(0);
                                        char second5 = sk.charAt(1);
                                        char second6 = sk.charAt(2);
                                        char second7 = sk.charAt(3);
                                        char second8 = sk.charAt(4);
                                        char second9 = sk.charAt(5);
                                        char second10= sk.charAt(6);
                                        StringBuilder myName = new StringBuilder("http://rtmsapi.laurelsolutions.net:6001/coverage?msg=/CDev0TEST/");
                                        myName.setCharAt(56, second12);
                                        myName.setCharAt(57,second5);
                                        myName.setCharAt(58,second6);
                                        myName.setCharAt(59, second7);
                                        myName.setCharAt(60, second8);
                                        myName.setCharAt(61, second9);
                                        myName.setCharAt(62, second10);
                                        String conz = total.concat(string11);
                                        String conf = conz.concat(String.valueOf(myName));
                                        String conf1 = conf.concat(string11);
                                        byte[] bytesOut = conf1.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        // rtms_up.setBackgroundColor(android.R.id.background);
                                        rtms_up.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in  RTMS Mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "RTMS_OK"+myName, Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured in  RTMS Mode ";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                                    }

                                }
                            }
                            catch (Exception e) {

                            }

                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListener6 =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);
                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);
                                if (s.endsWith("#")) {
                                    try {
                                        int usbResult;
                                        String str2 = "L";
                                        String str1 = "T";
                                        String total = str2.concat("" + str1);
                                        char z1 = '\n';
                                        String string11 = Character.toString(z1);
                                        String sk=deviceid.getText().toString();
                                        char second12 = sk.charAt(0);
                                        char second5 = sk.charAt(1);
                                        char second6 = sk.charAt(2);
                                        char second7 = sk.charAt(3);
                                        char second8 = sk.charAt(4);
                                        char second9 = sk.charAt(5);
                                        char second10= sk.charAt(6);
                                        StringBuilder myName = new StringBuilder("http://rtms.cal-on.com/api/v1/coverage?msg=/CDSTING01/");

                                        myName.setCharAt(46, second12);
                                        myName.setCharAt(47,second5);
                                        myName.setCharAt(48,second6);
                                        myName.setCharAt(49, second7);
                                        myName.setCharAt(50, second8);
                                        myName.setCharAt(51, second9);
                                        myName.setCharAt(52, second10);

                                        String conz = total.concat(string11);
                                        String conf = conz.concat(String.valueOf(myName));
                                        String conf1 = conf.concat(string11);
                                        byte[] bytesOut = conf1.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        // calon_up.setBackgroundColor(android.R.id.background);
                                        calon_up.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in  Cal on Mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Cal-On_Ok"+myName, Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured in  Cal on Mode ";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                                    }

                                }
                            }

                            catch (Exception e) {


                            }
                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListener7 =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);
                                if (s.endsWith("#")) {
                                    try {
                                        int usbResult;
                                        String str2 = "F";
                                        String str1 = "R";

                                        String total = str2.concat("" + str1);
                                        byte[] bytesOut = total.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        // Reset.setBackgroundColor(android.R.id.background);
                                        Reset.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in Rest Mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured in Reset Mode  ";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Can't Configured in Reset", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }
                            catch (Exception e) {

                            }

                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListener8 =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);

                                if (s.endsWith("#")) {

                                    try {
                                        int usbResult;
                                        String str1 = "K";
                                        String str2 = "T";
                                        String total=str1.concat(""+str2);
                                        byte[] bytesOut = total.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        //   count.setBackgroundColor(android.R.id.background);
                                        count.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Displaying the Count";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Count _OK", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to display the count";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Count _Not_OK", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                            catch (Exception e) {


                            }
                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListenerkk =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData1(data);
//logid.setBackgroundColor(android.R.id.background);
                            logid.setBackgroundColor(Color.parseColor(color));
                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListeneron =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);

                                if (s.endsWith("#")) {

                                    try {
                                        int usbResult;
                                        String str1 = "D";
                                        // String str2 = "T";
                                        //  String total=str1.concat(""+str2);
                                        byte[] bytesOut = str1.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        // cat_log_onD.setBackgroundColor(android.R.id.background);
                                        cat_log_onD.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "CAT Log mode IS ON";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "CAT_LOG_ON-OK", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to on CAT Log mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "CAT_LOG_ON-NO", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                            catch (Exception e) {


                            }
                        }
                    });
                }
            };
    private final SerialInputOutputManager.Listener mListeneroff =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }

                @Override
                public void onNewData(final byte[] data) {
                    Coverage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Coverage.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                char first = s.charAt(11);
                                char first1 = s.charAt(12);
                                char first2 = s.charAt(13);
                                char first3 = s.charAt(14);
                                char first4 = s.charAt(15);
                                String k = "$";
                                char second = k.charAt(0);

                                String k1 = "C";
                                char second1 = k1.charAt(0);
                                String k2 = "F";
                                char second2 = k2.charAt(0);
                                String k3 = "G";
                                char second3 = k3.charAt(0);
                                String k4 = "#";
                                char second4 = k4.charAt(0);

                                if (s.endsWith("#")) {

                                    try {
                                        int usbResult;
                                        String str1 = "d";
                                        // String str2 = "T";
                                        //  String total=str1.concat(""+str2);
                                        byte[] bytesOut = str1.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        //     cat_log_offd.setBackgroundColor(android.R.id.background);
                                        cat_log_offd.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "CAT Log mode IS OFF";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "CAT_LOG_OFF-OK", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        String toSpeak = "Unable to off CAT Log mode";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "CAT_LOG_OFF-NO", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                            catch (Exception e) {


                            }
                        }
                    });
                }
            };
    private static final int MESSAGE_REFRESH = 101;
    private List<UsbSerialPort> mEntries = new ArrayList<UsbSerialPort>();
    private ArrayAdapter<UsbSerialPort> mAdapter;
    TextView connect_btn;
    TextView btn_disconnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coverage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo_new1);
        toolbar.setTitle("   CAL-ON-Coverage ");
        setSupportActionBar(toolbar);
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mListView = (ListView) findViewById(R.id.deviceList);
        mDumpTextView = (TextView) findViewById(R.id.consoleTextusb);
        mScrollView = (ScrollView) findViewById(R.id.demoScrollerusb);
        deviceid=(EditText)findViewById(R.id.deviceid);
        count=(Button)findViewById(R.id.count);
        logid=(Button)findViewById(R.id.logid);
        Off_Line=(Button) findViewById(R.id.Off_Line);
        On_Line=(Button)findViewById(R.id.On_Line);
        Time_date=(Button)findViewById(R.id.time);
        Rtms_Url=(Button)findViewById(R.id.rtmsurl);
        CalOn_Url=(Button)findViewById(R.id.calonurl);
        rtms_up=(Button)findViewById(R.id.rtmsurlup);
        calon_up=(Button)findViewById(R.id.calonurlu) ;
        cat_log_onD=(Button)findViewById(R.id.onD);
        cat_log_offd=(Button)findViewById(R.id.offd);
        Reset=(Button)findViewById(R.id.reset);
        this.usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        this.mUsbReceiver = new C01102();
        baudrate = (Spinner) findViewById(R.id.spinner_baudrate);
        filePath = (TextView) findViewById(R.id.tv_filepath);
        TextView clear = (TextView) findViewById(R.id.tv_clear);
        TextView save = (TextView) findViewById(R.id.tv_save_usb);

        connect_btn = (TextView) findViewById(R.id.btn_connect);
        rtms_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceid.length()==0) {

                    String toSpeak = "Please Enter the Device ID";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(),"Please Enter the Device ID",Toast.LENGTH_SHORT).show();

                }
                else if(deviceid.length()!=7) {

                    String toSpeak = "Please Enter the Correct Device ID";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(),"Please Enter the Correct Device ID",Toast.LENGTH_SHORT).show();

                }
                else {
                    onDeviceStateChange5();
                    rtms_up.setBackgroundColor(Color.parseColor(getColor));
                    Off_Line.setBackgroundColor(Color.parseColor(color));
                    calon_up.setBackgroundColor(Color.parseColor(color));
                    count.setBackgroundColor(Color.parseColor(color));
                    logid.setBackgroundColor(Color.parseColor(color));
                    cat_log_onD.setBackgroundColor(Color.parseColor(color));
                    cat_log_offd.setBackgroundColor(Color.parseColor(color));
                    On_Line.setBackgroundColor(Color.parseColor(color));
                    Time_date.setBackgroundColor(Color.parseColor(color));
                    Rtms_Url.setBackgroundColor(Color.parseColor(color));
                    CalOn_Url.setBackgroundColor(Color.parseColor(color));
                    Reset.setBackgroundColor(Color.parseColor(color));

                    mDumpTextView.setText("");

                }
            }
        });
        calon_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deviceid.length()==0) {

                    String toSpeak = "Please Enter the Device ID";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(),"Please Enter the Device ID",Toast.LENGTH_SHORT).show();

                }
                else if(deviceid.length()!=7) {

                    String toSpeak = "Please Enter the Correct Device ID";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(),"Please Enter the Correct Device ID",Toast.LENGTH_SHORT).show();

                }
                else {
                    onDeviceStateChange6();
                    calon_up.setBackgroundColor(Color.parseColor(getColor));
                    Off_Line.setBackgroundColor(Color.parseColor(color));
                    rtms_up.setBackgroundColor(Color.parseColor(color));
                    count.setBackgroundColor(Color.parseColor(color));
                    logid.setBackgroundColor(Color.parseColor(color));
                    cat_log_onD.setBackgroundColor(Color.parseColor(color));
                    cat_log_offd.setBackgroundColor(Color.parseColor(color));
                    On_Line.setBackgroundColor(Color.parseColor(color));
                    Time_date.setBackgroundColor(Color.parseColor(color));
                    Rtms_Url.setBackgroundColor(Color.parseColor(color));
                    CalOn_Url.setBackgroundColor(Color.parseColor(color));
                    Reset.setBackgroundColor(Color.parseColor(color));
                    mDumpTextView.setText("");
                }

            }
        });
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange8();
                count.setBackgroundColor(Color.parseColor(getColor));
                Off_Line.setBackgroundColor(Color.parseColor(color));
                rtms_up.setBackgroundColor(Color.parseColor(color));
                calon_up.setBackgroundColor(Color.parseColor(color));
                logid.setBackgroundColor(Color.parseColor(color));
                cat_log_onD.setBackgroundColor(Color.parseColor(color));
                cat_log_offd.setBackgroundColor(Color.parseColor(color));
                On_Line.setBackgroundColor(Color.parseColor(color));
                Time_date.setBackgroundColor(Color.parseColor(color));
                Rtms_Url.setBackgroundColor(Color.parseColor(color));
                CalOn_Url.setBackgroundColor(Color.parseColor(color));
                Reset.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        logid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange9();
                logid.setBackgroundColor(Color.parseColor(getColor));
                Off_Line.setBackgroundColor(Color.parseColor(color));
                rtms_up.setBackgroundColor(Color.parseColor(color));
                calon_up.setBackgroundColor(Color.parseColor(color));
                count.setBackgroundColor(Color.parseColor(color));
                cat_log_onD.setBackgroundColor(Color.parseColor(color));
                cat_log_offd.setBackgroundColor(Color.parseColor(color));
                On_Line.setBackgroundColor(Color.parseColor(color));
                Time_date.setBackgroundColor(Color.parseColor(color));
                Rtms_Url.setBackgroundColor(Color.parseColor(color));
                CalOn_Url.setBackgroundColor(Color.parseColor(color));
                Reset.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        cat_log_onD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange10();
                cat_log_onD.setBackgroundColor(Color.parseColor(getColor));
                Off_Line.setBackgroundColor(Color.parseColor(color));
                rtms_up.setBackgroundColor(Color.parseColor(color));
                calon_up.setBackgroundColor(Color.parseColor(color));
                count.setBackgroundColor(Color.parseColor(color));
                logid.setBackgroundColor(Color.parseColor(color));
                cat_log_offd.setBackgroundColor(Color.parseColor(color));
                On_Line.setBackgroundColor(Color.parseColor(color));
                Time_date.setBackgroundColor(Color.parseColor(color));
                Rtms_Url.setBackgroundColor(Color.parseColor(color));
                CalOn_Url.setBackgroundColor(Color.parseColor(color));
                Reset.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        cat_log_offd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange11();
                cat_log_offd.setBackgroundColor(Color.parseColor(getColor));
                Off_Line.setBackgroundColor(Color.parseColor(color));
                rtms_up.setBackgroundColor(Color.parseColor(color));
                calon_up.setBackgroundColor(Color.parseColor(color));
                count.setBackgroundColor(Color.parseColor(color));
                logid.setBackgroundColor(Color.parseColor(color));
                cat_log_onD.setBackgroundColor(Color.parseColor(color));
                On_Line.setBackgroundColor(Color.parseColor(color));
                Time_date.setBackgroundColor(Color.parseColor(color));
                Rtms_Url.setBackgroundColor(Color.parseColor(color));
                CalOn_Url.setBackgroundColor(Color.parseColor(color));
                Reset.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        btn_disconnect = (TextView) findViewById(R.id.btn_disconnect);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });

        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectUSB();
            }
        });
        this.permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(USBCommonConstants.ACTION_USB_PERMISSION), 0);
        registerReceiver(this.mUsbReceiver, new IntentFilter(USBCommonConstants.ACTION_USB_PERMISSION));
        registerReceiver(this.mUsbReceiver, new IntentFilter("android.hardware.usb.action.USB_DEVICE_DETACHED"));
        mAdapter = new ArrayAdapter<UsbSerialPort>(this,
                android.R.layout.simple_expandable_list_item_2, mEntries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final TwoLineListItem row;
                if (convertView == null) {
                    final LayoutInflater inflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, null);
                } else {
                    row = (TwoLineListItem) convertView;
                }

                final UsbSerialPort port = mEntries.get(position);
                final UsbSerialDriver driver = port.getDriver();
                final UsbDevice device = driver.getDevice();

                final String title = String.format("Vendor %s Product %s",
                        HexDump.toHexString((short) device.getVendorId()),
                        HexDump.toHexString((short) device.getProductId()));
                row.getText1().setText(title);

                final String subtitle = driver.getClass().getSimpleName();
                row.getText2().setText(subtitle);

                return row;
            }

        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Pressed item " + position);
                if (position >= mEntries.size()) {
                    Log.w(TAG, "Illegal position.");
                    return;
                }

                final UsbSerialPort port = mEntries.get(position);
                showConsoleActivity(port);
            }
        });
        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectUSB();

            }
        });
        Off_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onDeviceStateChange();
                    Off_Line.setBackgroundColor(Color.parseColor(getColor));
                    cat_log_offd.setBackgroundColor(Color.parseColor(color));
                    rtms_up.setBackgroundColor(Color.parseColor(color));
                    calon_up.setBackgroundColor(Color.parseColor(color));
                    count.setBackgroundColor(Color.parseColor(color));
                    logid.setBackgroundColor(Color.parseColor(color));
                    cat_log_onD.setBackgroundColor(Color.parseColor(color));
                    On_Line.setBackgroundColor(Color.parseColor(color));
                    Time_date.setBackgroundColor(Color.parseColor(color));
                    Rtms_Url.setBackgroundColor(Color.parseColor(color));
                    CalOn_Url.setBackgroundColor(Color.parseColor(color));
                    Reset.setBackgroundColor(Color.parseColor(color));

                    mDumpTextView.setText("");

                }
                catch (Exception e) {

                }
            }
        });
        On_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onDeviceStateChange1();
                    On_Line.setBackgroundColor(Color.parseColor(getColor));
                    cat_log_offd.setBackgroundColor(Color.parseColor(color));
                    rtms_up.setBackgroundColor(Color.parseColor(color));
                    calon_up.setBackgroundColor(Color.parseColor(color));
                    count.setBackgroundColor(Color.parseColor(color));
                    logid.setBackgroundColor(Color.parseColor(color));
                    cat_log_onD.setBackgroundColor(Color.parseColor(color));
                    Off_Line.setBackgroundColor(Color.parseColor(color));
                    Time_date.setBackgroundColor(Color.parseColor(color));
                    Rtms_Url.setBackgroundColor(Color.parseColor(color));
                    CalOn_Url.setBackgroundColor(Color.parseColor(color));
                    Reset.setBackgroundColor(Color.parseColor(color));
                    mDumpTextView.setText("");

                }
                catch (Exception e) {

                }

            }
        });
        Time_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange2();
                Time_date.setBackgroundColor(Color.parseColor(getColor));
                cat_log_offd.setBackgroundColor(Color.parseColor(color));
                rtms_up.setBackgroundColor(Color.parseColor(color));
                calon_up.setBackgroundColor(Color.parseColor(color));
                count.setBackgroundColor(Color.parseColor(color));
                logid.setBackgroundColor(Color.parseColor(color));
                cat_log_onD.setBackgroundColor(Color.parseColor(color));
                Off_Line.setBackgroundColor(Color.parseColor(color));
                On_Line.setBackgroundColor(Color.parseColor(color));
                Rtms_Url.setBackgroundColor(Color.parseColor(color));
                CalOn_Url.setBackgroundColor(Color.parseColor(color));
                Reset.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        Rtms_Url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange3();
                Rtms_Url.setBackgroundColor(Color.parseColor(getColor));
                cat_log_offd.setBackgroundColor(Color.parseColor(color));
                rtms_up.setBackgroundColor(Color.parseColor(color));
                calon_up.setBackgroundColor(Color.parseColor(color));
                count.setBackgroundColor(Color.parseColor(color));
                logid.setBackgroundColor(Color.parseColor(color));
                cat_log_onD.setBackgroundColor(Color.parseColor(color));
                Off_Line.setBackgroundColor(Color.parseColor(color));
                On_Line.setBackgroundColor(Color.parseColor(color));
                Time_date.setBackgroundColor(Color.parseColor(color));
                CalOn_Url.setBackgroundColor(Color.parseColor(color));
                Reset.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    filePath.setText(exporttoCSV(mDumpTextView.getText().toString()));
                    mDumpTextView.setText("");
                    Toast.makeText(getApplicationContext(), "CSV file created and saved in internal storage" + filePath.getText().toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error Occurred while export file", Toast.LENGTH_SHORT).show();
                    filePath.setText("Error Occurred while export file");
                    e.printStackTrace();
                }
            }
        });
        CalOn_Url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange4();
                CalOn_Url.setBackgroundColor(Color.parseColor(getColor));
                cat_log_offd.setBackgroundColor(Color.parseColor(color));
                rtms_up.setBackgroundColor(Color.parseColor(color));
                calon_up.setBackgroundColor(Color.parseColor(color));
                count.setBackgroundColor(Color.parseColor(color));
                logid.setBackgroundColor(Color.parseColor(color));
                cat_log_onD.setBackgroundColor(Color.parseColor(color));
                Off_Line.setBackgroundColor(Color.parseColor(color));
                On_Line.setBackgroundColor(Color.parseColor(color));
                Time_date.setBackgroundColor(Color.parseColor(color));
                Rtms_Url.setBackgroundColor(Color.parseColor(color));
                Reset.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange7();

                Reset.setBackgroundColor(Color.parseColor(getColor));
                cat_log_offd.setBackgroundColor(Color.parseColor(color));
                rtms_up.setBackgroundColor(Color.parseColor(color));
                calon_up.setBackgroundColor(Color.parseColor(color));
                count.setBackgroundColor(Color.parseColor(color));
                logid.setBackgroundColor(Color.parseColor(color));
                cat_log_onD.setBackgroundColor(Color.parseColor(color));
                Off_Line.setBackgroundColor(Color.parseColor(color));
                On_Line.setBackgroundColor(Color.parseColor(color));
                Time_date.setBackgroundColor(Color.parseColor(color));
                Rtms_Url.setBackgroundColor(Color.parseColor(color));
                CalOn_Url.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDumpTextView.setText("");
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopIoManager();
        if (sPort != null) {
            try {
                sPort.close();
            } catch (IOException e) {
                // Ignore.

            }
            sPort = null;

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.bluetooth_menu, menu);
        return true;
    }
    void showStatus(TextView theTextView, String theLabel, boolean theValue) {
        String msg = theLabel + ": " + (theValue ? "enabled" : "disabled") + "\n";
        theTextView.append(msg);
    }
    /* @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case R.id.bluetooth_Shortcut :
             {
                 Intent i=new Intent(getApplicationContext(),Bluetooth.class);
                 startActivity(i);

                 return true;
             }
         }
         return super.onOptionsItemSelected(item);
     }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tagid :
            {
                Intent i=new Intent(getApplicationContext(),USBTerminal.class);
                startActivity(i);

                return true;
            }
            case R.id.offline :
            {
                Intent i=new Intent(getApplicationContext(),Offilne.class);
                startActivity(i);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed, port=" + sPort);
        connectUSB();
    }

    void connectUSB() {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            for (UsbDevice device : this.usbManager.getDeviceList().values()) {
                this.usbManager.requestPermission(device, this.permissionIntent);
            }

            return;
        }

        UsbSerialPort port = driver.getPorts().get(0);
        sPort = port;
        showConsoleActivity(port);
        if (sPort == null) {
            Toast.makeText(getApplicationContext(), "No serial device.", Toast.LENGTH_LONG).show();
        } else {
            final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

            connection = usbManager.openDevice(sPort.getDriver().getDevice());
            if (connection == null) {
                Toast.makeText(getApplicationContext(), "Opening device failed", Toast.LENGTH_LONG).show();
                for (UsbDevice device : this.usbManager.getDeviceList().values()) {
                    this.usbManager.requestPermission(device, this.permissionIntent);
                }
                return;
            }

            try {
                sPort.open(connection);
                sPort.setParameters(Integer.parseInt(baudrate.getSelectedItem().toString()), 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                connect_btn.setVisibility(View.GONE);
                btn_disconnect.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                Toast.makeText(getApplicationContext(), "Error opening device: " + e.getMessage(), Toast.LENGTH_LONG).show();
                try {
                    sPort.close();
                } catch (IOException e2) {

                }
                sPort = null;
                return;
            }
            Toast.makeText(getApplicationContext(), "Serial device: " + sPort.getClass().getSimpleName(), Toast.LENGTH_LONG).show();

        }
    }
    void disconnectUSB() {

        stopIoManager();
        if (sPort != null) {
            try {
                sPort.close();
                Toast.makeText(getApplicationContext(), "USB Device Disconnected  ", Toast.LENGTH_LONG).show();
                connect_btn.setVisibility(View.VISIBLE);
                btn_disconnect.setVisibility(View.GONE);
            } catch (IOException e) {
                // Ignore.
            }
            sPort = null;
        }
    }

    private void refreshDeviceList() {

        new AsyncTask<Void, Void, List<UsbSerialPort>>() {
            @Override
            protected List<UsbSerialPort> doInBackground(Void... params) {
                Log.d(TAG, "Refreshing device list ...");
                SystemClock.sleep(1000);

                final List<UsbSerialDriver> drivers =
                        UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
                for (final UsbSerialDriver driver : drivers) {
                    final List<UsbSerialPort> ports = driver.getPorts();
                    Log.d(TAG, String.format("+ %s: %s port%s",driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
                    result.addAll(ports);
                }

                return result;
            }

            @Override
            protected void onPostExecute(List<UsbSerialPort> result) {
                mEntries.clear();
                mEntries.addAll(result);
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "Done refreshing, " + mEntries.size() + " entries found.");
            }

        }.execute((Void) null);
    }



    private void showConsoleActivity(UsbSerialPort port) {
        show(this, port);
    }

    public class USBCommonConstants {
        public static final String ACTION_USB_PERMISSION = "cal_on.usbterminal.usb.USB_PERMISSION";
        public static final int USB_TIME_OUT = 1000;
    }

    class C01102 extends BroadcastReceiver {
        C01102() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice device;
            if (USBCommonConstants.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    device = (UsbDevice) intent.getParcelableExtra("device");
                    if (!intent.getBooleanExtra("permission", false)) {

                    } else if (device != null) {
                        UsbDeviceConnection connection = usbManager.openDevice(sPort.getDriver().getDevice());
                        if (connection == null) {
                            Toast.makeText(getApplicationContext(), "Opening device failed", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            sPort.open(connection);
                            sPort.setParameters(Integer.parseInt(baudrate.getSelectedItem().toString()), 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

                        } catch (IOException e) {
                            Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                            Toast.makeText(getApplicationContext(), "Error opening device: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            try {
                                sPort.close();
                            } catch (IOException e2) {
                                // Ignore.
                            }
                            sPort = null;
                            return;
                        }
                    }
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                device = (UsbDevice) intent.getParcelableExtra("device");
                if (device != null) {

                }
            } else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {

                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {


                    Toast.makeText(Coverage.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }



        }
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
            mExecutor.submit(mSerialIoManager);

        }
    }
    private void startIoManager1() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener1);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager2() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener2);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager4() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener3);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager5() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener4);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager6() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener5);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager7() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener6);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager8() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener7);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager9() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener8);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager10() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListenerkk);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager11() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListeneron);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager12() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListeneroff);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }


    private void onDeviceStateChange1() {
        stopIoManager();
        startIoManager1();
    }
    private void onDeviceStateChange2() {
        stopIoManager();
        startIoManager2();
    }
    private void onDeviceStateChange3() {
        stopIoManager();
        startIoManager4();
    }
    private void onDeviceStateChange4() {
        stopIoManager();
        startIoManager5();
    }
    private void onDeviceStateChange5() {

        stopIoManager();
        startIoManager6();
    }
    private void onDeviceStateChange6() {
        stopIoManager();
        startIoManager7();
    }
    private void onDeviceStateChange7() {
        stopIoManager();
        startIoManager8();
    }
    private void onDeviceStateChange8() {
        stopIoManager();
        startIoManager9();
    }
    private void onDeviceStateChange9() {
        stopIoManager();
        startIoManager10();
    }
    private void onDeviceStateChange10() {
        stopIoManager();
        startIoManager11();
    }
    private void onDeviceStateChange11() {
        stopIoManager();
        startIoManager12();
    }
    private void updateReceivedData(byte[] data) {
        String s = new String(data);
        totalData = totalData + s;
        mDumpTextView.append(s);
        mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());

    }


    private void updateReceivedData1(byte[] data) {
        String s = new String(data);
        String date = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
        totalData = totalData + s;
        char z1 = '\n';
        String string11 = Character.toString(z1);
        if(s.endsWith(string11)){

            mDumpTextView.append("    "+date);
        }
        mDumpTextView.append(s);
        mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
    }



    void show(Context context, UsbSerialPort port) {
        sPort = port;
    }

    public String exporttoCSV(final String totalData) throws IOException {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23);
            }
        }
        File mainFolder = new File(Environment.getExternalStorageDirectory()
                + "/Cal-ONTerminal");
        boolean mainVar = false;
        if (!mainFolder.exists())
            mainVar=mainFolder.mkdir();
        final File folder = new File(Environment.getExternalStorageDirectory()
                + "/Cal-ONTerminal/USB_terminal_files");

        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();

        System.out.println("" + var);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyMMdd_HHmmss");
        Date dt = new Date();
        final String fileFormatName = fmt.format(dt) + ".csv";
        final String filename = folder.toString() + "/" + fileFormatName;

        new Thread() {
            public void run() {
                try {

                    FileWriter fw = new FileWriter(filename);
                    fw.append(totalData);
                    fw.close();

                } catch (Exception e) {
                }
            }
        }.start();
        return "  [/Cal-ONTerminal/USB_terminal_files/" + fileFormatName + "]";
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Coverage.this);
        alertDialog.setTitle(Html.fromHtml("<font color='#3D9E06'>Exit From Application</font>"));
      //  alertDialog.setIcon(R.drawable.calon);
        alertDialog.setMessage("Are you sure you want Exit From Application ?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                exit = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        // a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);}}, 1);
                Toast.makeText(getApplicationContext(),"Application Closed",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                dialog.cancel();
            }
        });


        alertDialog.show();

    }

}
