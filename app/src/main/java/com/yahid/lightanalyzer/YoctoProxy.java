package com.yahid.lightanalyzer;

import android.util.Log;

import com.yoctopuce.YoctoAPI.YAPI;
import com.yoctopuce.YoctoAPI.YAPI_Exception;
import com.yoctopuce.YoctoAPI.YLightSensor;

public class YoctoProxy {

    private static YoctoProxy instance = null;

    public static final Boolean DEBUG_MODE = true;

    private double maxValue;
    private double minValue;
    YLightSensor sensor;

    private YoctoProxy(Object osContext){
        this.maxValue = 0;
        this.minValue = 0;
        if (!DEBUG_MODE)
        {
            try {
                // Pass the application Context to the Yoctopuce Library
                YAPI.EnableUSBHost(osContext);
                YAPI.RegisterHub("usb");
            } catch (YAPI_Exception e) {
                Log.e("Yocto",e.getLocalizedMessage());
            }

            sensor = YLightSensor.FirstLightSensor();
            if (sensor == null) {
                System.out.println("No module connected (check USB cable)");
            }
        }

    }

    public static YoctoProxy getInstance(Object osContext){
        if(instance == null)
        {
            instance = new YoctoProxy(osContext);
        }
        return instance;
    }

    public static void init(Object osContext) {
        YoctoProxy.getInstance(osContext);
    }

    public double readCurrentValue(){
        if (!DEBUG_MODE)
        {
            try {
                double value = this.sensor.get_currentValue();
                if (value > this.maxValue) {
                    this.maxValue = value;
                }

                else if (value < this.minValue) {
                    this.minValue = value;
                }

                return value;
            }

            catch (YAPI_Exception ex) {
                System.out.println(ex.getMessage());
                return -1.0;
            }
        }
        else return 99.99;
    }

}
