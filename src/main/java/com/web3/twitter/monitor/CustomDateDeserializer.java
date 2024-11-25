package com.web3.twitter.monitor;

import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.JSONReader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserializer implements ObjectReader<Date> {

    private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";

    @Override
    public Date readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        String dateStr = jsonReader.readString();
        if (dateStr == null) {
            return null;
        }
//        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
//        } catch (ParseException e) {
//            //throw new IOException("Date parsing error", e);
//        }
    }
}