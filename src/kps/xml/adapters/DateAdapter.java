package kps.xml.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {
    public static final String DATE_FORMAT = "h:mm:ssa dd/MM/yyyy";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public String marshal(Date v) throws Exception {
        synchronized (dateFormat) {
            if(v == null) {
                v = new Date();
            }
            return dateFormat.format(v);
        }
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        synchronized (dateFormat) {
            if(v == null) {
                v = dateFormat.format(new Date());
            }
            return dateFormat.parse(v);
        }
    }

}