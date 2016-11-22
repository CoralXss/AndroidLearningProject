package com.fangdd.android.volley.network;

import com.fangdd.android.volley.NetworkResponse;
import com.fangdd.android.volley.ParseError;
import com.fangdd.android.volley.Request;
import com.fangdd.android.volley.Response;
import com.fangdd.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by xss on 2016/11/11.
 * Create a defination about xml request
 */
public class XmlRequest extends Request<XmlPullParser> {
    private Response.Listener<XmlPullParser> mListener;

    public XmlRequest(int method, String url, Response.Listener<XmlPullParser> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    @Override
    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(parsed));

            return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(XmlPullParser response) {
        mListener.onResponse(response);
    }
}
