package net.bombdash.core.other;

import net.bombdash.core.site.lang.Locale;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static Map<String, Object> getMap(Object... args) {
        if (args.length % 2 != 0)
            throw new IllegalArgumentException("Args can be n%2=0");
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < args.length; i = i + 2) {
            map.put(args[i].toString(), args[i + 1]);
        }
        return map;
    }

    public static int colorToRgb(double[] color) {
        Color colorObj = new Color((int) color[0] * 255, (int) color[1] * 255, (int) color[2] * 255);
        return colorObj.getRGB();
    }

    private static Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");

    public static boolean isValidEmailAddress(String email) {
        Matcher m = emailPattern.matcher(email);
        return m.matches();
    }

    public static boolean parseBoolean(int setInt) {
        return setInt == 1;
    }

    public static Double[] rgbToColor(int colorInt) {
        Double[] colors = new Double[3];
        Color color = new Color(colorInt, false);
        colors[0] = (double) color.getRed() / 255D;
        colors[1] = (double) color.getGreen() / 255D;
        colors[2] = (double) color.getBlue() / 255D;
        return colors;
    }

    public static void printResultSet(ResultSet set) throws SQLException {
        ResultSetMetaData rsmd = set.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnsNumber; i++) {
            System.out.print(rsmd.getColumnName(i) + "    ");
        }
        System.out.println();
        while (set.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                String columnValue = set.getString(i);
                System.out.print(columnValue + "    ");
            }
            System.out.println();
            System.out.println("--------------------------------------");
        }
        set.beforeFirst();
    }

    public static String getRandomString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();


    }

    public static String[] stackToString(StackTraceElement[] trace) {
        return Arrays.stream(trace)
                .map(stack -> stack.getLineNumber() + ": " + stack.getClass() + " on class " + stack.getClassName())
                .toArray(String[]::new);
    }

    public static String traceToString(Exception ex) {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public static String getPage(HttpServletRequest request, int page) {
        return getPage(request, String.valueOf(page));
    }

    public static String getPage(HttpServletRequest request, String page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(request.getServletPath());
        builder.query(request.getQueryString());
        if (!page.equals(""))
            builder.replaceQueryParam("page", page);
        else
            builder.replaceQueryParam("page");
        return builder.toUriString();
    }

    public static int getCurrentPage(HttpServletRequest request) {
        String strPage = request.getParameter("page");
        int page;
        try {
            page = Integer.parseInt(strPage);
        } catch (Exception ex) {
            page = 1;
        }
        return page;
    }

    public static String getFormattedDate(long timestamp) {
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date(timestamp * 1000);
        return sf.format(date);
    }

    public static String getLeftTime(Locale locale, long second) {
        double day = second / 86400D;
        double hour = (second % 86400D) / 3600D;
        double min = (second % 3600D) / 60D;
        double sec = second % 60;
        StringBuilder builder = new StringBuilder();
        if (day >= 1)
            builder.append(" ").append((int) Math.floor(day)).append(" ").append(pluralForm(locale, (int) Math.floor(day),
                    "plurar_day1",
                    "plurar_day2",
                    "plurar_day5"));
        if (hour >= 1)
            builder.append(" ").append((int) Math.floor(hour)).append(" ").append(pluralForm(locale, (int) Math.floor(hour),
                    "plurar_hour1",
                    "plurar_hour2",
                    "plurar_hour5"));
        if (min >= 1)
            builder.append(" ").append((int) Math.floor(min)).append(" ").append(pluralForm(locale, (int) Math.floor(min),
                    "plurar_minute1",
                    "plurar_minute2",
                    "plurar_minute5"));
        if (sec >= 1)
            builder.append(" ").append((int) Math.floor(sec)).append(" ").append(pluralForm(locale, (int) Math.floor(sec),
                    "plurar_second1",
                    "plurar_second2",
                    "plurar_second5"));
        return builder.toString();
    }

    public static String pluralForm(Locale locale, int n, String form1, String form2, String form5) {
        n = Math.abs(n) % 100;
        int n1 = n % 10;
        if (n > 10 && n < 20) return locale.__(form5).toString();
        if (n1 > 1 && n1 < 5) return locale.__(form2).toString();
        if (n1 == 1) return locale.__(form1).toString();
        return locale.__(form5).toString();
    }

    public static int getSetSize(SqlRowSet set) {
        try {
            int size;
            set.last();
            size = set.getRow();
            set.beforeFirst();
            return size;
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String rgbToHex(String rgbStr) {
        int rgb;
        try {
            rgb = Integer.parseInt(rgbStr);
        } catch (NumberFormatException ex) {
            rgb = 0;
        }
        return rgbToHex(rgb);
    }

    public static String rgbToHex(int rgb) {

        return "#" + Integer.toHexString(rgb).substring(2);
    }
}
