package cal_on.usbterminal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.usb.UsbDeviceConnection;
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
import android.widget.ListView;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cal_on.usbterminal.NewDatabase.DBHelperreports;
import cal_on.usbterminal.NewDatabase.Database;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;

public class Offilne extends AppCompatActivity {

    private static TextView areacode;
    private static TextView areacode1;
    Customerlistdb myDB;

    int Cust_no;
    private final String TAG = Offilne.class.getSimpleName();
    String getColor="#E6DB83";
    String color="#CCCCCC";
    SQLiteDatabase data1;
    private UsbManager mUsbManager;
    private ListView mListView;
    private static UsbSerialPort sPort = null;
    ProgressDialog pd;
    private static  TextView mDumpTextView;
    private ScrollView mScrollView;
    private BroadcastReceiver mUsbReceiver;
    //    private String totalData = "";
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    private UsbManager usbManager;
    private PendingIntent permissionIntent;
    private Spinner baudrate;
    TextView filePath,number,deviceid,offlinetrs,deviceidcount,idcout;
    Button saveclick,delete,clearkk,save,view;
    EditText iddata,copydata,dataentery;
    SQLiteDatabase datak;
    String manual="";
    String rfidcard="";
    TextToSpeech t1;
    String tagid="";
    String empty ;
    String area="";
    private Boolean exit = false;
    String con="";
    DBHelperreports mydbhelper;
    int bill_no;
    //    TextView consoleTextUSB;
    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    Offilne.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Offilne.this.updateReceivedData(data);
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

                                if (first == second && first1 == second1 && first2 == second2 && first3 == second3 && first4 == second4) {

                                    try {
                                        int usbResult;
                                        String str1 = "K";
                                        String str2 = "T";
                                        String total=str1.concat(""+str2);
                                        byte[] bytesOut = total.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);

                                        delete.setBackgroundColor(Color.parseColor(color));
                                        //String kk = mDumpTextView.getText().toString();
                                        // String[] items = kk.split("\n");


                                        String toSpeak = "Displaying the Count";
                                        //   t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                                        //Toast.makeText(getApplicationContext(), "Count _OK", Toast.LENGTH_SHORT).show();


                                    } catch (Exception e) {
                                        String toSpeak = "Unable to display the count";
                                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Count _Not_OK", Toast.LENGTH_SHORT).show();

                                    }

                                }
                                else {
                                    deviceid.setText("");
                                    offlinetrs.setText("");
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
                    Offilne.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Offilne.this.updateReceivedData(data);
                            try {
                                String s = mDumpTextView.getText().toString();
                                number.setText(""+s);
                                String fiddaaa=number.getText().toString();
                                int usbResult;
                                String strt="*";



                                if (fiddaaa.endsWith("@")){
                                    try {

                                        byte[] bytesOut = strt.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);


                                        Toast.makeText(getApplicationContext(), "Data Cleared", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Unable to Data Cleared", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    // Toast.makeText(getApplicationContext(),"no",Toast.LENGTH_SHORT).show();
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
                    Offilne.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Offilne.this.updateReceivedData(data);
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
                                if (first == second && first1 == second1 && first2 == second2 && first3 == second3 && first4 == second4) {
                                    try {
                                        int usbResult;
                                        String str2 = "F";
                                        String str1 = "R";

                                        String total = str2.concat("" + str1);
                                        byte[] bytesOut = total.getBytes();
                                        usbResult = sPort.write(bytesOut, 1000);
                                        // Reset.setBackgroundColor(android.R.id.background);
                                        clearkk.setBackgroundColor(Color.parseColor(color));
                                        String toSpeak = "Device Successfully Configured in Rest Mode";
                                        //  t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Data Cleared SucessFuly", Toast.LENGTH_SHORT).show();


                                    } catch (Exception e) {
                                        String toSpeak = "Unable to Configured in Reset Mode  ";
                                        // t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                        Toast.makeText(getApplicationContext(), "Unable to  Cleared SucessFuly", Toast.LENGTH_SHORT).show();
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
    Button pastdata;
    TextView connect_btn;
    TextView btn_disconnect;
    String[] totalData;
    String url="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offilne);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo_new1);
        toolbar.setTitle("   Cal-On Offline");
        setSupportActionBar(toolbar);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mListView = (ListView) findViewById(R.id.deviceList);
        mDumpTextView = (TextView) findViewById(R.id.consoleTextusb);
        mScrollView = (ScrollView) findViewById(R.id.demoScrollerusb);
        this.usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        this.mUsbReceiver = new C01102();
        baudrate = (Spinner) findViewById(R.id.spinner_baudrate);
        filePath = (TextView) findViewById(R.id.tv_filepath);
        final TextView clear = (TextView) findViewById(R.id.tv_clear);
        deviceid=(TextView)findViewById(R.id.deviceid);
        offlinetrs=(TextView)findViewById(R.id.offlinetrs);
        saveclick=(Button)findViewById(R.id.Send);
        clearkk=(Button)findViewById(R.id.Sendk) ;
        number=(TextView)findViewById(R.id.number);
        deviceidcount=(TextView)findViewById(R.id.deviceidcount);
        dataentery=(EditText)findViewById(R.id.dataentery);
        // pastdata=(Button)findViewById(R.id.pastdata);
        connect_btn = (TextView) findViewById(R.id.btn_connect);
        btn_disconnect = (TextView) findViewById(R.id.btn_disconnect);
        save=(Button)findViewById(R.id.save);
        view=(Button)findViewById(R.id.view);
        idcout=(TextView)findViewById(R.id.idcout);
        myDB = new Customerlistdb(this);
        btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectUSB();
            }
        });
        mydbhelper = new DBHelperreports(this, Database.CAl_DATABASE_NAME, null, Database.CAL_DATABASE_VERSION);
        datak = mydbhelper.getWritableDatabase();
        Cursor c_count = datak.rawQuery("select * from " + Database.BI_Cust_TABLE_NAME, null);
        Cust_no = c_count.getCount();
        Cust_no++;
        idcout.setText(Cust_no + "");
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


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mDumpTextView.length()==0){
                    Toast.makeText(getApplicationContext(),"Please Get Data",Toast.LENGTH_SHORT).show();
                }
                else {
                    mydbhelper = new DBHelperreports(Offilne.this, Database.CAl_DATABASE_NAME, null, Database.CAL_DATABASE_VERSION);
                    datak = mydbhelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(Database.BI_Cust_BILL_NO, Cust_no + "");
                    long r = datak.insertOrThrow(Database.BI_Cust_TABLE_NAME, null, cv);
                    String datadb=mDumpTextView.getText().toString();
                    String deviceiddb=deviceid.getText().toString();
                    String offlinedb=offlinetrs.getText().toString();
                    data1 = openOrCreateDatabase("Custdatabase", 0, null);
                    String table = "create table if not exists Custdetails(Eid Integer primary key autoincrement," +"Custname varchar(20),Custarea varchar2(20),Custnum varchar2(20))";
                    data1.execSQL(table);
                    ContentValues values = new ContentValues();
                    values.put("Custname", datadb);
                    values.put("Custarea",deviceiddb);
                    values.put("Custnum",offlinedb);
                    data1.insert("Custdetails", null, values);
                    Toast.makeText(getApplicationContext(),"Data Stored Sucessfuly",Toast.LENGTH_SHORT).show();
                    saveclick.setVisibility(View.VISIBLE);
                    mydbhelper = new DBHelperreports(Offilne.this, Database.CAl_DATABASE_NAME, null, Database.CAL_DATABASE_VERSION);
                    datak = mydbhelper.getWritableDatabase();
                    Cursor c_count = datak.rawQuery("select * from " + Database.BI_Cust_TABLE_NAME, null);
                    Cust_no = c_count.getCount();
                    Cust_no++;
                    idcout.setText(Cust_no + "");

                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataentery.length() == 0){

                    Toast.makeText(getApplicationContext(),"Please Enter the Id ", Toast.LENGTH_SHORT).show();

                }
                else {
                    int Empid = Integer.parseInt(dataentery.getText().toString());


                    datak = openOrCreateDatabase("Custdatabase", 0, null);


                    String table = "create table if not exists Custdetails(Eid Integer primary key autoincrement," +
                            "Custname varchar(20),Custarea varchar2(20),Custnum varchar2(20))";
                    datak.execSQL(table);

                    String where = "Eid=" + Empid;

                    Cursor c = datak.query("Custdetails", null, where, null, null, null, null);


                    while (c.moveToNext())

                    {

                        mDumpTextView.setText(c.getString(1));
                        offlinetrs.setText(c.getString(3));
                        saveclick.setVisibility(View.VISIBLE);
                        deviceid.setText(c.getString(2));


                    }
                }

            }
        });
    /*    pastdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDumpTextView.setText(copydata.getText());
            }
        });*/

       /* saveclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveclick.setBackgroundColor(Color.parseColor(getColor));

                if (mDumpTextView.length()==0) {
                      Toast.makeText(getApplicationContext(),"Please Get the Offline data",Toast.LENGTH_SHORT).show();
                } else {
try{


                    int j = 0;

                    String kk = mDumpTextView.getText().toString();
                    String[] items = kk.split("\n");
                    url = items[3];
                     totalData=new String[items.length-6];
                     int counter =0;
                    for (j = 5; j < items.length - 1; j++) {
                        totalData[counter]=items[j];
                        counter++;
//                        try {
//
//                            Thread.sleep(1000);
//
//
//                        } catch (InterruptedException e) {
//                            // System.out.println(e);
//                        }

                    }
                    getNextURL();
                }
                catch (Exception e){
    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                }
                }
            }

        });*/
        saveclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDumpTextView.length() == 0) {
                    Toast.makeText(getApplicationContext(),"Please Get the Offline data",Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        //  AsyncT asyncT = new AsyncT();
                        //asyncT.execute();


                        //Do something after 100ms

                        AsyncHttpClient client = new AsyncHttpClient();
                        int j = 0;
                        StringBuilder sbPostData = null;
                        String kk = mDumpTextView.getText().toString();
                        String[] items = kk.split("\n");


                        String url = "http://rtms.cal-on.com/api/v1/coverage?msg=/";
                        //String ffd=items[5];
                        // Toast.makeText(getApplicationContext(), "Data " + con, Toast.LENGTH_SHORT).show();
                        for (j = 5; j < items.length - 1; j++) {
                            String Astr = items[j];
                            StringBuilder Sstr = new StringBuilder(items[j]);
                            Sstr.insert(24, '0');
                            Sstr.insert(25, '0');
                            Sstr.insert(26, '0');
                            Sstr.insert(27, '0');
                            Sstr.insert(28, '0');
                            Sstr.insert(29, '0');
                            Sstr.insert(31, '2');
                            Sstr.insert(32, '0');


                            sbPostData = new StringBuilder(url);
                            //sbPostData.append(con);
                            sbPostData.append(items[4]);
                            sbPostData.append(Sstr);

                            client.post("" + sbPostData.toString(), new AsyncHttpResponseHandler() {

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                                    Toast.makeText(Offilne.this, "SuccessFully Completed " + i, Toast.LENGTH_SHORT).show();
                                    saveclick.setVisibility(View.INVISIBLE);


                                }

                                @Override
                                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                                    Toast.makeText(Offilne.this, "NetWork Fail" + i, Toast.LENGTH_SHORT).show();
                                }


                                @Override
                                public void onRetry(int retryNo) {

                                    Toast.makeText(Offilne.this, "Please On The Data Connection" + retryNo, Toast.LENGTH_SHORT).show();
                                }

                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // System.out.println(e);
                            }
                            String conv = offlinetrs.getText().toString();
                            int foo = Integer.parseInt(conv);
                            foo--;
                            offlinetrs.setText(String.valueOf(foo));

                        }
                    }
                    catch (Exception e){

                    }
                }

            }

        });
        delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange();
                offlinetrs.setVisibility(View.VISIBLE);
                deviceid.setVisibility(View.VISIBLE);
                delete.setBackgroundColor(Color.parseColor(getColor));
                saveclick.setBackgroundColor(Color.parseColor(color));
                clearkk.setBackgroundColor(Color.parseColor(color));
                mDumpTextView.setText("");
            }
        });
        clearkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        Offilne.this).create();
                alertDialog.setTitle("Alert Message");
                alertDialog.setMessage("You Want To Clear Data");
                //  alertDialog.setIcon(R.drawable.wrong);
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        offlinetrs.setVisibility(View.INVISIBLE);
                        deviceid.setVisibility(View.INVISIBLE);

                        onDeviceStateChange1();
                        clearkk.setBackgroundColor(Color.parseColor(getColor));
                        saveclick.setBackgroundColor(Color.parseColor(color));
                        delete.setBackgroundColor(Color.parseColor(color));
                        offlinetrs.setText("");
                        deviceid.setText("");
                        mDumpTextView.setText("");
                    }
                });

                alertDialog.show();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDumpTextView.setText("");
                offlinetrs.setText("");
                saveclick.setVisibility(View.INVISIBLE);
                deviceid.setText("");
            }
        });
    }
    int counterForURLCalling = 0;
    public void getNextURL(){
        if(counterForURLCalling<totalData.length){
            postData(url+totalData[counterForURLCalling]);
            counterForURLCalling++;
        }
        else {
            Log.i("Data Posting ","All Done");
        }

    }
    public void postData(String url){
//        StringBuilder sbPostData = null;
//        sbPostData = new StringBuilder(url);
//
//        sbPostData.append(items[j]);
        //sbPostData.append(items[5]);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {

                Toast.makeText(Offilne.this, "SuccessFully Completed " + i, Toast.LENGTH_SHORT).show();
                saveclick.setBackgroundColor(Color.parseColor(color));
                //offlinetrs.setText("");
                delete.setBackgroundColor(Color.parseColor(color));
//                String conv = offlinetrs.getText().toString();
//                int foo = Integer.parseInt(conv);
//                foo--;
//                offlinetrs.setText(String.valueOf(foo));
                getNextURL();
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(Offilne.this, "NetWork Fail" + i, Toast.LENGTH_SHORT).show();
                String conv = offlinetrs.getText().toString();
                int foo = Integer.parseInt(conv);
                foo--;
                offlinetrs.setText(String.valueOf(foo));
                getNextURL();
            }


            @Override
            public void onRetry(int retryNo) {

                Toast.makeText(Offilne.this, "Please On The Data Connection" + retryNo, Toast.LENGTH_SHORT).show();
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
//        if (this.mUsbReceiver != null)
//            unregisterReceiver(this.mUsbReceiver);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.offline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tagid :
            {
                Intent i=new Intent(getApplicationContext(),USBTerminal.class);
                startActivity(i);

                return true;
            }
            case R.id.coverage :
            {
                Intent i=new Intent(getApplicationContext(),Coverage.class);
                startActivity(i);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    void showStatus(TextView theTextView, String theLabel, boolean theValue) {
        String msg = theLabel + ": " + (theValue ? "enabled" : "disabled") + "\n";
        theTextView.append(msg);
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
            // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
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
                    // Ignore.
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                } else {


                    Toast.makeText(Offilne.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener7);
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

    private void updateReceivedData(byte[] data) {
        String s = new String(data);
        //totalData = totalData + s;
        mDumpTextView.append(s);
        mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
        int i = 0;
        String kff = mDumpTextView.getText().toString();
        String kk = mDumpTextView.getText().toString();

        String[] items = kk.split("\n");

        try {

            if (items.length > 4) {
                offlinetrs.setText("" + (items.length - 6));
                deviceid.setText("" + items[4]);
                deviceidcount.setText(""+items[2]);
                String conv = deviceidcount.getText().toString();
                int foo = Integer.parseInt(conv);


                try {
                    if ((items.length - 4) == foo) {

                        //   onDeviceStateChange1();

                        //  Toast.makeText(getApplicationContext(), "Reciving Data ", Toast.LENGTH_SHORT).show();


                    } else {

                    }
                } catch (Exception e) {

                }
            }

        }
        catch (Exception e) {

        }
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


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23
                );
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

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS,  Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.READ_SMS,Manifest.permission.BLUETOOTH,Manifest.permission.CALL_PHONE,Manifest.permission.READ_PHONE_STATE}, 101);
    }
    class AsyncT extends AsyncTask<String, Void, String> {
        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Offilne.this);
            String load = "loading...";
            pd.setMessage(load);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json_string = "";
            int i = 0;
            StringBuilder sbPostData = null;
            String kk = mDumpTextView.getText().toString();
            String[] items = kk.split("\n");
            // String url = items[2];
            // String URL="";
            try {
                URL url;
                HttpURLConnection urlConn;
                DataOutputStream printout;
                url = new URL(items[2]);

                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("", items[4]);
                //  jsonParam.put("rfidino", rfidcard);

                printout = new DataOutputStream(urlConn.getOutputStream());
                String str = jsonParam.toString();
                byte[] data = str.getBytes("UTF-8");
                printout.write(data);
                printout.flush();
                printout.close();
                status = urlConn.getResponseCode();
                Log.e("status", status + "");
                String mesg = urlConn.getResponseMessage();
                Log.e("statusmesg", mesg + "");
                InputStream is;
                if (status != HttpURLConnection.HTTP_OK) {
                    is = urlConn.getErrorStream();
                } else {
                    is = urlConn.getInputStream();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("eroor", e.getMessage().toString());
            }
            return json_string;
        }

        protected void onPostExecute(String result) {
            try {
                pd.dismiss();
                if (status == 200) {

                    Toast.makeText(Offilne.this, "Success"+status, Toast.LENGTH_SHORT).show();
                   /* mDumpTextView.setText("");
                    tagid = iddata.getText().toString();
                    int value_int = Integer.parseInt(tagid);
                    int incremento = value_int +1 ;
                    iddata.setText(String.valueOf(incremento));
                    String sno=number.getText().toString();*/
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            Offilne.this).create();
                    alertDialog.setTitle("Alert Message");
                    alertDialog.setMessage("Some Thing Went Wrong");
                    alertDialog.setIcon(R.drawable.wrong);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    alertDialog.show();
                    String toSpeak = "Some Thing Went Wrong";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }

            } catch (Exception Ex) {
                Log.e("exe", Ex.getMessage().toString());

            }

        }
    }
    class Delete  extends AsyncTask<Void, Void, String> {
        int status;

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";

            try {
                URL url = new URL(" http://rtms.laurelsolutions.net:2017/api/tagmaster?rfidino="+rfidcard);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                status = conn.getResponseCode();
                if (status == 200) {
                    InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    response = br.readLine();

                } else if (status == 204) {
                    // Toast.makeText(Offilne.this, "There is no Saved Gates", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("this", e.getMessage().toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("status", status + "");
            if (status == 200) {
                Toast.makeText(getApplicationContext(),"Sucessfully Deleted",Toast.LENGTH_SHORT).show();
                mDumpTextView.setText("");
                iddata.setText("");

            }
            else if (status == 204) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        Offilne.this).create();
                alertDialog.setTitle("Alert Deletion");
                alertDialog.setMessage("There is no Saved Data");
                alertDialog.setIcon(R.drawable.wrong);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                String toSpeak = "There is no Saved data";
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


            }
        }
    }
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Offilne.this);
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
