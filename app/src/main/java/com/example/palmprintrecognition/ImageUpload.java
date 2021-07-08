package com.example.palmprintrecognition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Message;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ImageUpload{

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private static final OkHttpClient client = new OkHttpClient();

    public static void run(File f,Context context,String from) throws Exception {
        final File file=f;
        new Thread() {
            @Override
            public void run() {
                //子线程需要做的工作
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file","test.jpg",
                                RequestBody.create(MEDIA_TYPE_JPG, file))
                        .build();
                //设置为自己的ip地址
                Request request = new Request.Builder()
                        .url("http://192.168.1.103:5000/"+from)
                        .post(requestBody)
                        .build();
                try(Response response = client.newCall(request).execute()){
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    String ans = response.body().string();
                    System.out.println(ans);

                    Activity activity = (Activity)context;
                    if (from.equals("register")) {
                        if (ans.equals("注册成功")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(context)
                                            .setTitle("注册结果")
                                            .setMessage("注册成功")
                                            .setPositiveButton("确定", null)
                                            .show();
                                }
                            });
                        }
                        else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(context)
                                            .setTitle("注册结果")
                                            .setMessage("注册失败")
                                            .setPositiveButton("确定", null)
                                            .show();
                                }
                            });
                        }
                    }
                    else {
                        if (ans.equals("匹配")) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(context)
                                            .setTitle("匹配结果")
                                            .setMessage("匹配")
                                            .setPositiveButton("确定", null)
                                            .show();
                                }
                            });
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(context)
                                            .setTitle("匹配结果")
                                            .setMessage("不匹配")
                                            .setPositiveButton("确定", null)
                                            .show();
                                }
                            });
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
