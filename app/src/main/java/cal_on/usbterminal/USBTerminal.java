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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cal_on.usbterminal.NewDatabase.DBHelperreports;
import cal_on.usbterminal.NewDatabase.Database;

public class USBTerminal extends AppCompatActivity {

    private final String TAG = USBTerminal.class.getSimpleName();

    private UsbManager mUsbManager;
    private ListView mListView;
    private static UsbSerialPort sPort = null;
    ProgressDialog pd;
    private EditText mDumpTextView;
    private ScrollView mScrollView;
    private BroadcastReceiver mUsbReceiver;
    private String totalData = "";
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    private UsbManager usbManager;
    private PendingIntent permissionIntent;
    private Spinner baudrate;
    TextView filePath,number;
    Button saveclick,delete;
    EditText iddata,areacode;
    SQLiteDatabase data;
    String manual="";
    String rfidcard="";
    TextToSpeech t1;
    private Boolean exit = false;
    String tagid="";
    String area="";
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
                    USBTerminal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            USBTerminal.this.updateReceivedData(data);
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
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo_new1);
        toolbar.setTitle("   Cal-On Tag Id");
        setSupportActionBar(toolbar);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mListView = (ListView) findViewById(R.id.deviceList);
        mDumpTextView = (EditText) findViewById(R.id.consoleTextusb);
        mScrollView = (ScrollView) findViewById(R.id.demoScrollerusb);
        this.usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        this.mUsbReceiver = new C01102();
        baudrate = (Spinner) findViewById(R.id.spinner_baudrate);
        filePath = (TextView) findViewById(R.id.tv_filepath);
       TextView clear = (TextView) findViewById(R.id.tv_clear);
        //TextView save = (TextView) findViewById(R.id.tv_save_usb);
        saveclick=(Button)findViewById(R.id.Send);
        iddata=(EditText)findViewById(R.id.Enter);
        number=(TextView)findViewById(R.id.number);
        areacode=(EditText)findViewById(R.id.areacode) ;
//        consoleTextUSB=(TextView) findViewById(R.id.consoleTextusb);
        connect_btn = (TextView) findViewById(R.id.btn_connect);
        btn_disconnect = (TextView) findViewById(R.id.btn_disconnect);
        mydbhelper = new DBHelperreports(USBTerminal.this, Database.CAl_DATABASE_NAME, null, Database.CAL_DATABASE_VERSION);

        data = mydbhelper.getWritableDatabase();

        Cursor c_count = data.rawQuery("select * from " + Database.BI_TABLE_NAME, null);
        bill_no = c_count.getCount();
        bill_no++;
        number.setText(bill_no + "");
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

        saveclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDumpTextView.length()==0 || iddata.length()==0 || areacode.length()==0){
                    Toast.makeText(getApplication(),"Please Enter the Data",Toast.LENGTH_SHORT).show();
                    String toSpeak = "Please Enter the Data";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                }
                else if (mDumpTextView.length()!=24 || iddata.length()!=5 || areacode.length()!=3) {
                    Toast.makeText(getApplication(),"Please Enter the Correct Data",Toast.LENGTH_SHORT).show();
                    String toSpeak = "Please Enter the Correct Data";
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }

                else {
                    area=areacode.getText().toString();
                    tagid = iddata.getText().toString();
                    rfidcard=mDumpTextView.getText().toString();
                    con=area.concat(tagid);
                    AsyncT asyncT = new AsyncT();
                    asyncT.execute();
                }
            }
        });
        delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDumpTextView.length()==0 && iddata.length()==0){
                    Toast.makeText(getApplication(),"Please Enter the Data",Toast.LENGTH_SHORT).show();
                }
                else  if(mDumpTextView.length() !=0 && iddata.length()==0){
                    Toast.makeText(getApplication(),"Please Enter the Id",Toast.LENGTH_SHORT).show();
                }
                else  if(mDumpTextView.length() ==0 && iddata.length()!=0){
                    Toast.makeText(getApplication(),"Please Get the Id",Toast.LENGTH_SHORT).show();
                }
                else {
                    Delete asynck = new Delete();
                    asynck.execute();
                }
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
//        if (this.mUsbReceiver != null)
//            unregisterReceiver(this.mUsbReceiver);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.weighment:
                Intent intent = new Intent(USBTerminal.this, Weighment.class);
                startActivity(intent);
                return true;
            case R.id.coverage:
                Intent intent1=new Intent(USBTerminal.this,Coverage.class);
                startActivity(intent1);
                return true;
            case R.id.offline:
                Intent intent2=new Intent(USBTerminal.this,Offilne.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
            onDeviceStateChange();
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


                    Toast.makeText(USBTerminal.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
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
    public class FileOpen {

        public  void openFile(Context context, File url) throws IOException {
            // Create URI
            File file=url;
            Uri uri = Uri.fromFile(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Check what kind of file you are trying to open, by comparing the url with extensions.
            // When the if condition is matched, plugin sets the correct intent (mime) type,
            // so Android knew what application to use to open the file
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if(url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if(url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if(url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if(url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                //if you want you can also define the intent type for any other file

                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
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

            pd = new ProgressDialog(USBTerminal.this);
            String load = "loading...";
            pd.setMessage(load);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json_string = "";
            // String URL="";
            try {
                URL url;
                HttpURLConnection urlConn;
                DataOutputStream printout;
                url = new URL("http://rtms.laurelsolutions.net:2017/api/tagmaster");

                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("rfidcardno", con);
                jsonParam.put("rfidino", rfidcard);

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

                    Toast.makeText(USBTerminal.this, "Success", Toast.LENGTH_SHORT).show();
                    mDumpTextView.setText("");
                    tagid = iddata.getText().toString();
                    int value_int = Integer.parseInt(tagid);
                    int incremento = value_int +1 ;
                    iddata.setText(String.valueOf(incremento));
                    String sno=number.getText().toString();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            USBTerminal.this).create();
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
                   // Toast.makeText(USBTerminal.this, "There is no Saved Gates", Toast.LENGTH_SHORT).show();
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
                        USBTerminal.this).create();
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(USBTerminal.this);
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
