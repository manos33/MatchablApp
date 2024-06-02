package com.example.matchabl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkHandler {
    private static final String ADDRESS = "http://192.168.1.142:8080";
    private static final String TAG = "NetworkHandler";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String AUTH_KEY = "Authorization";

    public static void signUp(Context context, String username, String password, final SignUpCallback callback) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(postData.toString(), JSON_MEDIA_TYPE);

        String url = ADDRESS +"/user/auth/signup";
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        Headers headers = response.headers();
                        String authToken = headers.get("Authorization");

                        // Save the Authorization token in SharedPreferences
                        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(AUTH_KEY, authToken);
                        editor.apply();

                        mainHandler.post(callback::onSuccess);
                    } else {
                        mainHandler.post(() -> callback.onFailure("This username already exists."));
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public static void createProfile(Context context, String name, String surname, String email, String phone, int age, final SignUpCallback callback) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);
            postData.put("surname", surname);
            postData.put("email", email);
            postData.put("phone", phone);
            postData.put("age", age);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(postData.toString(), JSON_MEDIA_TYPE);

        String url = ADDRESS +"/user/profile/create-profile";

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        mainHandler.post(callback::onSuccess);
                    } else {
                        mainHandler.post(() -> callback.onFailure("An error occurred."));
                    }
                } finally {
                    response.close();
                }
            }
        });
    }



    public static void login(Context context, String username, String password, final SignUpCallback callback) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(postData.toString(), JSON_MEDIA_TYPE);

        String url = ADDRESS + "/user/auth/login";
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        Headers headers = response.headers();
                        String authToken = headers.get("Authorization");

                        // Save the Authorization token in SharedPreferences
                        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(AUTH_KEY, authToken);
                        editor.apply();

                        mainHandler.post(callback::onSuccess);
                    } else {
                        mainHandler.post(() -> callback.onFailure("Invalid username or password."));
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public static void sendSportsPreferences(Context context, Set<String> sportsPreferences, final SignUpCallback callback) {
        JSONObject postData = new JSONObject();
        try {
            // Create a JSON array from the sports preferences
            postData.put("sports", new JSONArray(sportsPreferences));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(postData.toString(), JSON_MEDIA_TYPE);

        String url = ADDRESS + "/user/profile/add-favorite-sports";

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        mainHandler.post(callback::onSuccess);
                    } else {
                        mainHandler.post(() -> callback.onFailure("An error occurred."));
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public static void getFieldsBySportAndPage(Context context, String sport, int page, final GetFieldsCallback callback) {
        String url = ADDRESS + "/user/search/all/" + sport + "/" + page;

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get();

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray fieldsArray = jsonResponse.getJSONArray("facilities");
                        mainHandler.post(() -> callback.onSuccess(fieldsArray));
                    } else {
                        mainHandler.post(() -> callback.onFailure("An error occurred."));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                    mainHandler.post(() -> callback.onError(e.getMessage()));
                } finally {
                    response.close();
                }
            }
        });
    }







    public void getFieldDetails(Context context, String fieldId, FieldDetailsCallback callback) {
        String url = "http://192.168.1.142:8080/user/search/facility/" + fieldId;

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        if (authToken != null) {
            Log.d(TAG, "Authorization token: " + authToken);
        } else {
            Log.e(TAG, "Authorization token is null");
            callback.onFailure("Authorization token is missing");
            return;
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get();

        if (authToken != null) {
            requestBuilder.addHeader("Authorization",authToken);
        } else {
            callback.onFailure("Authorization token is missing");
            return;
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        Log.d(TAG, "Response received: " + jsonResponse);
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        callback.onSuccess(jsonObject);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                        callback.onError(e.getMessage());
                    } finally {
                        response.close();
                    }
                } else {
                    Log.e(TAG, "Response not successful: " + response.code() + " - " + response.message());
                    Log.e(TAG, "Response body: " + response.body().string());
                    callback.onFailure("An error occurred.");
                }
            }
        });
    }


    public static void getAvailableTimes(Context context, String fieldId, String sportType, int year, int month, int day, final AvailableTimesCallback callback) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("fieldId", fieldId);
            postData.put("sportType", sportType);
            postData.put("year", year);
            postData.put("month", month);
            postData.put("day", day);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(postData.toString(), JSON_MEDIA_TYPE);

        String url = ADDRESS + "/field/available-times";

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONArray availableTimes = new JSONArray(response.body().string());
                        mainHandler.post(() -> callback.onSuccess(availableTimes));
                    } else {
                        mainHandler.post(() -> callback.onFailure("An error occurred."));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                    mainHandler.post(() -> callback.onError(e.getMessage()));
                } finally {
                    response.close();
                }
            }
        });
    }


    public static void getAvailableFacilities(Context context, String sport, String date, @Nullable String startTime, @Nullable String endTime, final GetFieldsCallback callback) {
        String url;
        if (startTime != null && endTime != null) {
            url = ADDRESS + "/user/search/facilities/available/" + sport + "/" + date + "/" + startTime + "/" + endTime;
        } else {
            url = ADDRESS + "/user/search/facilities/available/" + sport + "/" + date;
        }

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get();

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONObject responseObject = new JSONObject(response.body().string());
                        JSONArray facilitiesArray = responseObject.getJSONArray("facilities");
                        mainHandler.post(() -> callback.onSuccess(facilitiesArray));
                    } else {
                        mainHandler.post(() -> callback.onFailure("An error occurred."));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                    mainHandler.post(() -> callback.onError(e.getMessage()));
                } finally {
                    response.close();
                }
            }
        });
    }




    public interface SignUpCallback {
        void onSuccess();
        void onFailure(String errorMessage);
        void onError(String errorMessage);
    }

    public interface GetFieldsCallback {
        void onSuccess(JSONArray fieldsArray);
        void onFailure(String errorMessage);
        void onError(String errorMessage);
    }

    public interface FieldDetailsCallback {
        void onSuccess(JSONObject fieldDetails);
        void onFailure(String errorMessage);
        void onError(String errorMessage);
    }

    public interface AvailableTimesCallback {
        void onSuccess(JSONArray times);
        void onFailure(String errorMessage);
        void onError(String errorMessage);
    }
}
