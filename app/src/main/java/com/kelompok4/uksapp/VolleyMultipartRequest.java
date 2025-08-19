package com.kelompok4.uksapp;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {
    private static final String TAG = "VolleyMultipartRequest";

    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mHeaders;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return null;
    }

    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            Log.d(TAG, "Response status code: " + response.statusCode);
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            Log.e(TAG, "Error parsing network response: " + e.getMessage());
            return Response.error(new VolleyError(e.getMessage()));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        Log.e(TAG, "Error in request: " + error.toString());
        mErrorListener.onErrorResponse(error);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + getBoundary();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // Write text parameters
            if (getParams() != null && getParams().size() > 0) {
                Log.d(TAG, "Adding text parameters: " + getParams().toString());
                textParse(dos, getParams());
            }

            // Write file parameters
            if (getByteData() != null && getByteData().size() > 0) {
                Log.d(TAG, "Adding file parameters");
                dataParse(dos, getByteData());
            }

            dos.writeBytes("\r\n--" + getBoundary() + "--\r\n");
            return bos.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, "IOException writing to ByteArrayOutputStream: " + e.getMessage());
        }
        return null;
    }

    private void textParse(DataOutputStream dos, Map<String, String> params) throws IOException {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            dos.writeBytes("--" + getBoundary() + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            dos.writeBytes(entry.getValue() + "\r\n");
            Log.d(TAG, "Added text param: " + entry.getKey() + " = " + entry.getValue());
        }
    }

    private void dataParse(DataOutputStream dos, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            dos.writeBytes("--" + getBoundary() + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"" +
                    entry.getKey() + "\"; filename=\"" + entry.getValue().getFileName() + "\"\r\n");
            dos.writeBytes("Content-Type: " + entry.getValue().getType() + "\r\n\r\n");

            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(entry.getValue().getContent());
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            dos.writeBytes("\r\n");

            Log.d(TAG, "Added file param: " + entry.getKey() + " = " + entry.getValue().getFileName());
        }
    }

    private String getBoundary() {
        return "apiclient-" + System.currentTimeMillis();
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
            this.type = "application/octet-stream";
        }

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
