package com.wheelchef.wheelchef.dbconnection;

/**
 * Created by lyk on 10/18/2015.
 */

import android.app.Activity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class DBConnector {

    private static final String SOAP_ACTION = "http://ws.android.com/sayHello";
    private static final String METHOD_NAME = "sayHello";
    private static final String NAMESPACE = "http://ws.android.com/";
    private static final String URL = "http://175.157.229.119:8080/AndroidWSTest/services/PrintMsg?wsdl";

    private Activity activity;

    public DBConnector(final Activity activity) {
        this.activity = activity;

        Thread networkThread = new Thread() {
            @Override
            public void run() {
                try {
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE ht = new HttpTransportSE(URL);
                    ht.call(SOAP_ACTION, envelope);
                    final  SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                    final String str = response.toString();

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        networkThread.start();
    }
}
