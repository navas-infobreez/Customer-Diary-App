package com.plannet.apps.diarybook.utils;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.plannet.apps.diarybook.AppController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.lang.Character;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    String TAG="CommonUtils";
    public static final String DEFAULT_DATE_FORMAT ="dd-MM-yyyy";
    public static final String DEFAULT_DATETIME_FORMAT ="ddMMyyyy-HHmmss";
    public static final String DATETIME_PRINT_FORMAT ="dd/MM/yyyy HH:mm:ss";
    public static final String DATETIME_TWELVE_HOUR_FORMAT ="dd/MM/yyyy hh:mm:ss aa";
    public static  String datePrintFormat="dd/MM/yyyy";
    public static  String shortDateFormat="dd/MM/yy"; // only for display purpose

    public static String getDefaultDateFormat() {
        return DEFAULT_DATE_FORMAT;
    }


    public static String getCurrentDateAndTime() {
        SimpleDateFormat dateformat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        String dateString = dateformat.format(new Date());
        return dateString;
    }

    public static String getDateTimePrintFormat(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat(DATETIME_PRINT_FORMAT);
        String dateString = dateformat.format(date);
        return dateString;
    }

    public static long getDateTimePrintFormat(String updatedSince) {
        if(updatedSince == null)
            return 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PRINT_FORMAT);
        try {
            Date date = simpleDateFormat.parse(updatedSince);
            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTwelveHourTimeFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATETIME_PRINT_FORMAT);
        String dateString = dateFormat.format(date);
        DateFormat outputformat = new SimpleDateFormat(DATETIME_TWELVE_HOUR_FORMAT);
        String output = null;
        try {
            Date date1 = dateFormat.parse(dateString);
            //old date format to new date format
            output = outputformat.format(date1);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static long getDateToLong(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_TWELVE_HOUR_FORMAT);
        try {
            Date sampleDate = simpleDateFormat.parse(String.valueOf(date));
//            Timestamp timestamp = new Timestamp(sampleDate.getTime());
            date = String.valueOf(sampleDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.parseLong(date);
    }



    public static String getDate(Object date){
        if(date == null)
            return null;
        SimpleDateFormat dateformat = new SimpleDateFormat(getDefaultDateFormat());
        String dateString = dateformat.format(date);
        return dateString;
    }

    public static Date getDate(String date,String format){
        if(date == null)
            return null;
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        Date dateObject = null;
        try {
            dateObject = dateformat.parse(date);
        } catch (ParseException e) {
            Log.d("CommonUtils",e.getMessage());
        }
        return dateObject;
    }
    public static long getDate(String date){
        if(date == null)
            return 0;
        SimpleDateFormat dateformat = new SimpleDateFormat(getDefaultDateFormat());
        try {
            Date d = dateformat.parse(date);
            long milliseconds = d.getTime();
            return milliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getPrintDate(Date date){
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat(datePrintFormat);
            String dateString = dateformat.format(date);
            return dateString;
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(date);
        return time;
    }

    public static String changeDateFormat(String currentFormat,String requiredFormat,String dateString){
        String result="";
        if (dateString!=null||!dateString.isEmpty()){
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date=null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static String getDateTimeInSystemFormat(Context context,Date date){
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat(shortDateFormat);
        String dateString=dateFormat.format(date);
        SimpleDateFormat time24 = new SimpleDateFormat("kk:mm");
        SimpleDateFormat timeFormat12 =new SimpleDateFormat("hh:mm a");
        long timeInMillis = System.currentTimeMillis();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(timeInMillis);

        String  timeString="";

        if (android.text.format.DateFormat.is24HourFormat(context)) {

            timeString=time24.format(date);
        }
        else
        {
            timeString = timeFormat12.format(date);
        }

        String  result =  dateString +"-"+ timeString;

        return result;
    }


    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public static Date parseDate(String dateString)
    {
        if(dateString == null || dateString.isEmpty())
            return null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(getDefaultDateFormat());
        try {

            return simpleDateFormat.parse(dateString);

        } catch (ParseException e) {
            Log.e("CommonUtils", "Invalid Date. " + e.getMessage(), e);
        }
        return null;
    }
    public static long getDaysBetweenDates(Date d1, Date d2){
        return TimeUnit.MILLISECONDS.toDays(d1.getTime() - d2.getTime());
    }
    /**
     * Pad the String to the given length and append to the ByteBuffer
     * @param str    String input
     * @param maxLength    Max length of padding
     * @return  Padded / Truncated String
     */
    public static String padString(String str, int maxLength) {
        StringBuilder buffer = new StringBuilder();
        int length = str != null?str.length():0;
        if(length > 0) {
            char[] strBuff = str.toCharArray();
            if(length >= maxLength) {
                length = maxLength-1;
                strBuff[length-1] = '*'; // Add a * to indicate truncation
            }
            buffer.append(strBuff, 0, length);  // Max maxLength-1
        }
        for(int i=length; i< maxLength; i++) buffer.append(' '); // Pad the name with (atleast one) NULL
        return buffer.toString();
    }

    /** Format BigDecimal as Human Readable String. If max length is higher than converted length,
     * extra space is prepended.
     * @param bd
     * @param maxLength 0 - to skip padding
     * @return
     */
    public static String padBigDecimal(BigDecimal bd, int maxLength) {
        if(bd == null)
            return "";
        StringBuilder buffer = new StringBuilder();
        DecimalFormat df;
        String pattern = "#,##,##0";
        if(bd.scale() > 0)
            pattern += ".";
        for(int i = 0; i < bd.scale(); i++) // Final pattern will be "#,##,##0.000" for 3 decimal;
            pattern += "0";
        df = new DecimalFormat(pattern);
        String str = df.format(bd);
        if(maxLength == 0)
            return str;
        else
            return prePadString(str, maxLength);
    }

    public static String prePadString(String str, int maxLength) {
        StringBuilder buffer = new StringBuilder();
        int length = str != null?str.length():0;
        if(length < maxLength) { // PAD ON LEFT SIDE
            buffer.append(padString("", maxLength-length));  // Max maxLength-1
        }
        buffer.append(str);
        return buffer.toString();
    }

    public static  String getCenterAligned(String string)
    {

        int remainingSpace=48- string.length();
        int indent=0;
        if(remainingSpace>0)
        {

            indent = remainingSpace / 2;
        }
        String indents=CommonUtils.padString("",indent);
        string=String.format("%s%s%s",indents, string ,indents);
        return  string;
    }

    public static int toInt(String value)
    {
        int data;
        if(value == null)
            return 0;
        try
        {
            data=Integer.parseInt(value);
        }
        catch (Exception e)
        {
            Log.w("toInt", e.getMessage(), e);
            data=0;
        }
        return data ;
    }

    public static long toLong(String value)
    {
        long data;
        if(value == null)
            return 0;
        try
        {
            data=Long.parseLong(value);
        }
        catch (Exception e)
        {
            Log.w("toInt", e.getMessage(), e);
            data=0;
        }
        return data ;
    }

    public static String toString(int value)
    {

        return String.valueOf(value);
    }

    public static BigDecimal toBigDecimal(String data) {
        if (data != null && data instanceof String) {
            BigDecimal decimalValue;
            try {
                decimalValue = new BigDecimal((String) data);
            } catch (NumberFormatException ex) {
                decimalValue = BigDecimal.ZERO;
            }
            return decimalValue;
        }
        return BigDecimal.ZERO;
    }

    public static String fromHtml(String text) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            text = Html.fromHtml(new String(text), Html.FROM_HTML_MODE_LEGACY).toString();
        else
            text = Html.fromHtml(new String(text)).toString();
        text = text.replaceAll("<!--.*-->", ""); // Remove HTML comments, if any
        return text;
    }

    public static boolean isIntegerValue(BigDecimal bd) {
        return bd != null && (bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0);
    }

    public static Object quoteIfString(Object value) {
        if(value != null && value instanceof String) {
            return quoteString((String) value);
        }
        return value;
    }
    public static String quoteString(String value) {
        if(value == null)
            return null;
        if(!value.startsWith("\"") && !value.startsWith("'")) {
            value = "'" + value + "'";
        }
        return value;
    }


    public static List<?> exclude(List<?> list, Object item) {
        ArrayList newList = new ArrayList<>(list);
        newList.remove(item);
        return newList;
    }

    public static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity)cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)cont).getBaseContext());

        return null;
    }


    public static Bitmap getResizedBitmap(Bitmap bm) {
        int newHeight = 120; int newWidth = 160;
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    public static Bitmap getResizedBitmapLogo(Bitmap bm,int widthSize) {
        int newHeight = 0;
        int maxHeight;
        if (widthSize>32){
            maxHeight=200;
        }else {
            maxHeight=150;
        }

        int newWidth = widthSize*12;
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (height>maxHeight){
            newHeight=maxHeight;
        }else {
            newHeight=height;
        }
        if (width<newWidth){
            newWidth=width;
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //float scale = Math.min(((float)newHeight / bm.getWidth()), ((float)newWidth / bm.getHeight()));
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth,scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
        //Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);

        return resizedBitmap;
    }

    public static void hideKeyboard(final Activity activity) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = activity.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(activity);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

    }
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getRootView().getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static boolean isRtl(String string) {
        if (string == null) {
            return false;
        }

        for (int i = 0, n = string.length(); i < n; ++i) {
            byte d = Character.getDirectionality(string.charAt(i));

            switch (d) {
                case Character.DIRECTIONALITY_RIGHT_TO_LEFT:
                case Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC:
                case Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING:
                case Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE:
                    return true;

                case Character.DIRECTIONALITY_LEFT_TO_RIGHT:
                case Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING:
                case Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE:
                    return false;
            }
        }

        return false;
    }

    public static boolean isAscii(String string) {
        StringCharacterIterator iter = new StringCharacterIterator(string);
        for(int i = iter.getBeginIndex(); i < iter.getEndIndex(); iter.next(), i++) {
            char c = iter.current(); // Current is initially '0'. Next will increment it. Hence use current
            if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.BASIC_LATIN) {
                return false;
            }
        }
        return true;
    }

    private static final Pattern localeMatcher = Pattern.compile
            ("^([^_]*)(_([^_]*)(_#(.*))?)?$");

    public static Locale getLocale(String value) {
        Matcher matcher = localeMatcher.matcher(value.replace('-', '_'));
        return matcher.find()
                ? TextUtils.isEmpty(matcher.group(5))
                ? TextUtils.isEmpty(matcher.group(3))
                ? TextUtils.isEmpty(matcher.group(1))
                ? null
                : new Locale(matcher.group(1))
                : new Locale(matcher.group(1), matcher.group(3))
                : new Locale(matcher.group(1), matcher.group(3), matcher.group(5))
                : null;
    }


    public static String arrayToString(List<Integer> categoryFilter) {
        if(categoryFilter != null && categoryFilter.size() > 0) {
            String categoryFilterString = categoryFilter.toString();
            categoryFilterString = categoryFilterString.substring(1, categoryFilterString.length()-1); // strip []
            return categoryFilterString;
        }
        return "";
    }
    public static String arrayToString(Integer[] categoryFilter) {
        if(categoryFilter != null && categoryFilter.length > 0) {
            String categoryFilterString = Arrays.toString(categoryFilter);
            categoryFilterString = categoryFilterString.substring(1, categoryFilterString.length()-1); // strip []
            return categoryFilterString;
        }
        return "";
    }
}
