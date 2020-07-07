package com.yiyun.rmj.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


public class MGsonConverterFactory extends Converter.Factory {
    private boolean isAes;
    private Gson gson;


    public static MGsonConverterFactory create(boolean isAes) {

        return new MGsonConverterFactory(isAes);

    }


    private MGsonConverterFactory(boolean isAes) {

        this.isAes = isAes;
        gson = new Gson();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Log.e("syq", "zhixinglaresponse");
        return new ResponseBodyConverter<>(type, true);
    }

    //    @Override
//    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
//
//    }
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        Log.e("syq", "zhixingla body");
        return new RequestBodyConverter<>(isAes, gson);
    }


//
//    @Override
//    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//
//    }


    /**
     * 自定义请求RequestBody
     */
    public static class RequestBodyConverter<T> implements Converter<T, RequestBody> {

        private static final MediaType MEDIA_TYPE = MediaType.parse("application/wxt;charset=UTF-8");
        private static final MediaType MEDIA_TYPE1 = MediaType.parse("application/json;charset=UTF-8");
        private boolean isAes;
        private Gson gson;


        public RequestBodyConverter(boolean isAes, Gson gson) {
            this.isAes = isAes;
            this.gson = gson;
        }

        @Override
        public RequestBody convert(T value) throws IOException {//T就是传入的参数
            Log.e("bczstr", value.toString());
            if (isAes) {//这里加上你自己的加密处理
                //    AES aes = new AES();
                String desParams = null;
                Log.e("bczstr", value.toString());

                try {
                    desParams = DESHelper.encrypt(value.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("syqnetwork1", e.getMessage().toString());
                }


                return RequestBody.create(MEDIA_TYPE, desParams);

            } else {
                return RequestBody.create(MEDIA_TYPE1, gson.toJson(value));
            }
        }

    }

    /**
     * 自定义响应ResponseBody
     */
    public class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private boolean isAes;
        private Type type;

        public ResponseBodyConverter(Type type, boolean isAes) {
            this.isAes = isAes;
            this.type = type;
        }

        /**
         * @param value
         * @return T
         * @throws IOException
         */
        @Override
        public T convert(ResponseBody value) throws IOException {

            //Log.e("syqnstring", value.string() + "\n" + isAes);
            try {
                if (isAes) {////这里加上你自己的解密方法
                    //byte[] b = value.bytes();
                    //  AES aes = new AES();
                    //byte[] a = aes.decrypt(b);
//                     Log.e("syqnstring", value.string());
                     String valueStr = value.string();
                    String str = DESHelper.decrypt(valueStr);
                    Log.e("bczstr", str);

                    String json = new String(str);
                    Log.e("json", json);
                    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
                    return new Gson().fromJson(json, type);


                } else {
                    String json = value.string();
                    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
                    return new Gson().fromJson(json, type);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("syqnetwork", e.getMessage().toString());
            } finally {
                //value.close();
            }
            return null;
        }

    }


}

