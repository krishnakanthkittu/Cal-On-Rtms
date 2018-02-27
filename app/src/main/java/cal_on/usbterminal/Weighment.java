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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
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

import org.w3c.dom.Text;

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

public class Weighment extends AppCompatActivity {

    private final String TAG = USBTerminal.class.getSimpleName();
   // BTConnection uConn;
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
    private Boolean exit = false;
    UsbEndpoint endpointOut = null;
    EditText deviceid,port,ip;
    TextView cw_url,rtms,calon;



    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {
                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");

                }
                @Override
                public void onNewData(final byte[] data) {
                    Weighment.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Weighment.this.updateReceivedData(data);
                            {
                                try{
                                    String s=  mDumpTextView.getText().toString();
                                    char first = s.charAt(0);
                                    char first1 = s.charAt(1);
                                    char first2 = s.charAt(2);
                                    char str=s.charAt(0);
                                    char strk=s.charAt(1);
                                    char strl=s.charAt(2);
                                    char strm=s.charAt(3);
                                    char strn=s.charAt(4);
                                    String ktr="$";
                                    char str1=ktr.charAt(0);
                                    String ktr1="S";
                                    char strf=ktr1.charAt(0);
                                    String ktr2="E";
                                    char strg=ktr2.charAt(0);
                                    String ktr3="T";
                                    char strh=ktr3.charAt(0);
                                    String ktr4="#";
                                    char stri=ktr4.charAt(0);
                                    String kK="$";
                                    char secondK=kK.charAt(0);
                                    String k1K="T";
                                    char second1K=k1K.charAt(0);
                                    String k2K="#";
                                    char second2K=k2K.charAt(0);

                                    String k = "$";
                                    char second = k.charAt(0);
                                    String k1 = "T";
                                    char second1 = k1.charAt(0);
                                    String k2 = "#";
                                    char second2 = k2.charAt(0);


                                    if (mDumpTextView.length()==5){

                                        if (str==str1 && strk==strf && strl==strg && strm==strh &&strn==stri ) {
                                            int usbResult;
                                            char z = '\n';
                                            String string1 = Character.toString(z);
                                            String stro = "*";
                                            String strp = "2";
                                            String strq = "5";
                                            String strr = "#";
                                            String con = string1.concat(stro);
                                            String con1 = strp.concat(strq);
                                            String co = con.concat(con1);
                                            String co1 = strr.concat(string1);
                                            String cof = co.concat(co1);
                                            byte[] bytesOut = cof.getBytes();
                                            usbResult = sPort.write(bytesOut, 1000);
                                            cw_url.setBackgroundColor(android.R.id.background);
                                            String toSpeak="Device Is In Settings mode ";
                                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


                                        }

                                    }
                                    else if(mDumpTextView.length()==8){
                                        char first111 = s.charAt(5);
                                        char first11 = s.charAt(6);
                                        char first21 = s.charAt(7);
                                        String k111 = "$";
                                        char second111 = k111.charAt(0);
                                        String k11 = "T";
                                        char second11 = k11.charAt(0);
                                        String k21 = "#";
                                        char second21 = k21.charAt(0);
                                        String kf = "$";
                                        char secondf = kf.charAt(0);
                                        String k1f = "R";
                                        char second1f = k1f.charAt(0);
                                        String k2f = "#";
                                        char second2f = k2f.charAt(0);



                                        if (first111 == second111 && first11 == second11 && first21 == second21) {
                                            try {
                                                String sk=deviceid.getText().toString();

                                                // StringBuilder myName = new StringBuilder("http://35.154.195.48:3000/api/users/postData?msg=12345CDev/TEST1/");
                                                StringBuilder myName = new StringBuilder("http://35.154.195.48:3000/api/users/postData?msg=1234/WDCRLTEST/");

                                                char first1k=sk.charAt(0);
                                                char first2k=sk.charAt(1);
                                                char first3k=sk.charAt(2);
                                                char first4k=sk.charAt(3);
                                                char first5k=sk.charAt(4);
                                                char first6k=sk.charAt(5);
                                                char first7k=sk.charAt(6);
                                                myName.setCharAt(56, first1k);
                                                myName.setCharAt(57, first2k);
                                                myName.setCharAt(58, first3k);
                                                myName.setCharAt(59, first4k);
                                                myName.setCharAt(60,first5k);
                                                myName.setCharAt(61,first6k);
                                                myName.setCharAt(62,first7k);

                                                int usbResultf;
                                                char zf = '\n';
                                                String string1f = Character.toString(zf);

                                                String total = string1f.concat(String.valueOf(myName));
                                                String totalf = total.concat(string1f);
                                                byte[] bytesOutf = totalf.getBytes();
                                                usbResultf = sPort.write(bytesOutf, 1000);

                                                String toSpeak1 = "Device Successfully Configured in Cal on  Mode ";
                                                t1.speak(toSpeak1, TextToSpeech.QUEUE_FLUSH, null);
                                                Toast.makeText(getApplicationContext(), "Cal-On_OK" + myName, Toast.LENGTH_SHORT).show();


                                            } catch (Exception e) {
                                                String toSpeak = "Unable to Configured in Cal-On Weighment Mode  ";
                                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                                Toast.makeText(getApplicationContext(), "Cal-On_NOT_OK", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        else if(first111 == secondf && first11 == second1f && first21 == second2f){
                                            try {

                                                int usbResult1;
                                                char z1 = '\n';
                                                String string11 = Character.toString(z1);
                                                String sk = deviceid.getText().toString();
                                                StringBuilder myName = new StringBuilder("http://rtmsapi.laurelsolutions.net:5001/weighment?msg=/WDEV00001/");
                                                char second12 = sk.charAt(0);
                                                char second5 = sk.charAt(1);
                                                char second6 = sk.charAt(2);
                                                char second7 = sk.charAt(3);
                                                char second8 = sk.charAt(4);
                                                char first6k=sk.charAt(5);
                                                char first7k=sk.charAt(6);
                                                myName.setCharAt(57, second12);
                                                myName.setCharAt(58, second5);
                                                myName.setCharAt(59, second6);
                                                myName.setCharAt(60, second7);
                                                myName.setCharAt(61,second8);
                                                myName.setCharAt(62,first6k);
                                                myName.setCharAt(63,first7k);

                                                String conz = string11.concat(String.valueOf(myName));
                                                String conf = conz.concat(string11);
                                                byte[] bytesOut1 = conf.getBytes();
                                                usbResult1 = sPort.write(bytesOut1, 1000);
                                                String toSpeak1 = "Device Successfully Configured in RTMS mode ";
                                                t1.speak(toSpeak1, TextToSpeech.QUEUE_FLUSH, null);
                                                Toast.makeText(getApplicationContext(), "RTMS_OK" + myName, Toast.LENGTH_SHORT).show();

                                            } catch (Exception e) {
                                                String toSpeak = "Unable to Configured in  RTMS Coverage Mode ";
                                                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                                Toast.makeText(getApplicationContext(), "RTMS-Coverage_NOT_OK", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }
                                }
                                catch (Exception e){

                                }
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
    RadioButton deviceidu,ipaddres;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weighment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo_new1);
        toolbar.setTitle("   Cal-On-Url-Weighment");
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
        deviceid=(EditText)findViewById(R.id.device);
        cw_url=(TextView) findViewById(R.id.cw_url);
        //  calon=(TextView)findViewById(R.id.cc_url);
        // rtms=(TextView)findViewById(R.id.ct_url);

        deviceidu=(RadioButton)findViewById(R.id.radiohouse);
        ipaddres=(RadioButton)findViewById(R.id.radiocommercial);
        port=(EditText)findViewById(R.id.portid);
        ip=(EditText)findViewById(R.id.ipaddress);
        this.usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        this.mUsbReceiver = new C01102();
        baudrate = (Spinner) findViewById(R.id.spinner_baudrate);
        filePath = (TextView) findViewById(R.id.tv_filepath);
        TextView clear = (TextView) findViewById(R.id.tv_clear);
        TextView save = (TextView) findViewById(R.id.tv_save_usb);
//        consoleTextUSB=(TextView) findViewById(R.id.consoleTextusb);
        connect_btn = (TextView) findViewById(R.id.btn_connect);

        btn_disconnect = (TextView) findViewById(R.id.btn_disconnect);
        deviceidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceid.setVisibility(View.VISIBLE);
                port.setVisibility(View.INVISIBLE);
                ip.setVisibility(View.INVISIBLE);
                mDumpTextView.setText("");
                port.setText("");
                ip.setText("");
            }
        });
        ipaddres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceid.setVisibility(View.INVISIBLE);
                port.setVisibility(View.VISIBLE);
                ip.setVisibility(View.VISIBLE);
                deviceid.setText("");
                mDumpTextView.setText("");
            }
        });
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });
        ip.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(ip.getText().toString().length()==10)
                {
                    port.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
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
        cw_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (deviceid.length()==0 && port.length()==0 && ip.length()==0){
                        cw_url.setBackgroundColor(android.R.id.background);

                        Toast.makeText(getApplication(),"Please Enter Device ID Or Port ID",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        onDeviceStateChange();
                        cw_url.setBackgroundColor(Color.GREEN);
                        //calon.setBackgroundColor(android.R.id.background);
                        // rtms.setBackgroundColor(android.R.id.background);
                        // rw_url.setBackgroundColor(android.R.id.background);
                        // rc_url.setBackgroundColor(android.R.id.background);
                        //  rt_url.setBackgroundColor(android.R.id.background);
                        mDumpTextView.setText("");

                    }
                }
                catch (Exception e) {

                }
            }
        });

   /*     calon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceid.length()==0 && port.length()==0 && ip.length()==0){
                    cw_url.setBackgroundColor(android.R.id.background);
                    rtms.setBackgroundColor(android.R.id.background);
                    Toast.makeText(getApplication(),"Please Enter Device ID Or Port ID",Toast.LENGTH_SHORT).show();
                }
                else {
                    onDeviceStateChange3();
                    calon.setBackgroundColor(Color.GREEN);
                    cw_url.setBackgroundColor(android.R.id.background);
                    rtms.setBackgroundColor(android.R.id.background);
                    mDumpTextView.setText("");
                }
            }
        });*/
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
      /*  rtms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceid.length()==0 && port.length()==0 && ip.length()==0){
                    Toast.makeText(getApplication(),"Please Enter Device ID Or Port ID",Toast.LENGTH_SHORT).show();
                    cw_url.setBackgroundColor(android.R.id.background);
                    calon.setBackgroundColor(android.R.id.background);
                }
                else {
                    onDeviceStateChange4();
                    rtms.setBackgroundColor(Color.GREEN);
                    cw_url.setBackgroundColor(android.R.id.background);
                    calon.setBackgroundColor(android.R.id.background);
                    mDumpTextView.setText("");
                }
            }
        });*/
       /* rt_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeviceStateChange5();
                rt_url.setBackgroundColor(Color.GREEN);
                cw_url.setBackgroundColor(android.R.id.background);
                cc_url.setBackgroundColor(android.R.id.background);
                ct_url.setBackgroundColor(android.R.id.background);
                rw_url.setBackgroundColor(android.R.id.background);
                rc_url.setBackgroundColor(android.R.id.background);
                mDumpTextView.setText("");
            }
        });*/
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

        getMenuInflater().inflate(R.menu.weighment, menu);
        return true;
    }
    void showStatus(TextView theTextView, String theLabel, boolean theValue) {
        String msg = theLabel + ": " + (theValue ? "enabled" : "disabled") + "\n";
        theTextView.append(msg);
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
            case R.id.offline :
            {
                Intent i=new Intent(getApplicationContext(),Offilne.class);
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


                    Toast.makeText(Weighment.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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
    private void startIoManagerk() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
            mExecutor.submit(mSerialIoManager);

        }
    }

    private void startIoManager4() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            //mSerialIoManager = new SerialInputOutputManager(sPort, mListener3);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager5() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            // mSerialIoManager = new SerialInputOutputManager(sPort, mListener4);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void startIoManager6() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            //   mSerialIoManager = new SerialInputOutputManager(sPort, mListener5);
            mExecutor.submit(mSerialIoManager);
        }
    }
    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }


    private void onDeviceStateChange1() {
        stopIoManager();

    }
    private void onDeviceStateChange2() {
        stopIoManager();

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
    private void updateReceivedData(byte[] data) {
        String s = new String(data);
        totalData = totalData + s;
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

    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Weighment.this);
        alertDialog.setTitle(Html.fromHtml("<font color='#3D9E06'>Exit From Application</font>"));
     //   alertDialog.setIcon(R.drawable.calon);
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
}
