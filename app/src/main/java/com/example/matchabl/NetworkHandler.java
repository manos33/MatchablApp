package com.example.matchabl;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
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
    private static final String ADDRESS = "http://10.26.80.191:8080"; // We changed this one with the Server's IP and port
    private static final String TAG = "NetworkHandler";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String AUTH_KEY = "Authorization";
    private static final String SPORTS_PREF = "sports";




    public static void signUp(Context context, String username, String password, final SignUpCallback callback) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(postData.toString(), JSON_MEDIA_TYPE);

        String url = ADDRESS + "/user/auth/signup";
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

        String url = ADDRESS + "/user/profile/create-profile";

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

                        // Parse the response body to get user information
                        String responseBody = response.body().string();
                        JSONObject userProfileObject = new JSONObject(responseBody);
                        JSONArray userInfoArray = userProfileObject.getJSONArray("profile");
                        JSONObject userInfo = userInfoArray.getJSONObject(0);

                        editor.putString("username", userInfo.getString("username"));
                        editor.putString("name", userInfo.getString("name"));
                        editor.putString("surname", userInfo.getString("surname"));
                        editor.putString("email", userInfo.getString("email"));
                        editor.putString("phone_number", userInfo.getString("phone_number"));
                        editor.putInt("age", userInfo.getInt("age"));

                        // Save the profile image as a Base64 string if it exists
                        if (!userInfo.isNull("profile_image")) {
                            JSONObject profileImage = userInfo.getJSONObject("profile_image");
                            JSONArray dataArray = profileImage.getJSONArray("data");
                            byte[] imageBytes = new byte[dataArray.length()];
                            for (int i = 0; i < dataArray.length(); i++) {
                                imageBytes[i] = (byte) dataArray.getInt(i);
                            }
                            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            editor.putString("profile_image", base64Image);
                        } else {
                            editor.putString("profile_image", null);
                        }

                        // Save sports if they exist
                        if (userProfileObject.has("sports")) {
                            JSONArray sportsArray = userProfileObject.getJSONArray("sports");
                            Set<String> sportsSet = new HashSet<>();
                            for (int i = 0; i < sportsArray.length(); i++) {
                                sportsSet.add(sportsArray.getJSONObject(i).getString("SportName"));
                            }
                            editor.putStringSet("sports", sportsSet);
                            Log.d(TAG, "Sports saved: " + sportsSet.toString()); // Log sports
                        } else {
                            editor.putStringSet("sports", new HashSet<>());
                        }

                        editor.apply();

                        mainHandler.post(callback::onSuccess);
                    } else {
                        mainHandler.post(() -> callback.onFailure("Invalid username or password."));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mainHandler.post(() -> callback.onError("JSON parsing error: " + e.getMessage()));
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
        Log.d(TAG, "Request URL: " + url);

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        if (authToken != null) {
            Log.d(TAG, "Authorization token: " + authToken);
        } else {
            Log.e(TAG, "Authorization token is null");
        }

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
                Log.e(TAG, "Network call failed: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String jsonResponseString = response.body().string();
                        Log.d(TAG, "Response received: " + jsonResponseString);
                        JSONObject jsonResponse = new JSONObject(jsonResponseString);
                        JSONArray fieldsArray = jsonResponse.getJSONArray("facilities");
                        mainHandler.post(() -> callback.onSuccess(fieldsArray));
                    } else {
                        Log.e(TAG, "Response not successful: " + response.code() + " - " + response.message());
                        Log.e(TAG, "Response body: " + response.body().string());
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

    public static void getFieldDetails(Context context, String fieldId, FieldDetailsCallback callback) {
        String url = ADDRESS + "/user/search/facility/" + fieldId;

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
            requestBuilder.addHeader("Authorization", authToken);
        } else {
            callback.onFailure("Authorization token is missing");
            return;
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onFailure(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        Log.d(TAG, "Response received: " + jsonResponse);
                        JSONObject jsonObject = new JSONObject(jsonResponse);

                        // Extract facility sports details
                        JSONArray facilitySportsArray = jsonObject.getJSONObject("facility").getJSONArray("facility_sports");

                        mainHandler.post(() -> callback.onSuccess(jsonObject, facilitySportsArray));
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                        mainHandler.post(() -> callback.onError(e.getMessage()));
                    } finally {
                        response.close();
                    }
                } else {
                    Log.e(TAG, "Response not successful: " + response.code() + " - " + response.message());
                    Log.e(TAG, "Response body: " + response.body().string());
                    mainHandler.post(() -> callback.onFailure("An error occurred."));
                }
            }
        });
    }

    public static void getAvailableTimes(Context context, String fieldId, String sportName, String sportType, String date, final AvailableTimesCallback callback) {
        String url = ADDRESS + "/user/search/facility/" + fieldId + "/availability/" + sportName + "/" + sportType + "/" + date;


        Log.d(TAG, "Request URL: " + url);

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get();


        if (authToken != null) {
            requestBuilder.addHeader("Authorization", authToken);
            Log.d(TAG, "Authorization token: " + authToken);
        } else {
            Log.e(TAG, "Authorization token is missing");
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
                        String responseBody = response.body().string();
                        Log.d(TAG, "Server response: " + responseBody);
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray availabilityArray = jsonResponse.getJSONArray("availability");

                        // Καταγραφή όλων των στοιχείων για έλεγχο
                        for (int i = 0; i < availabilityArray.length(); i++) {
                            JSONObject availability = availabilityArray.getJSONObject(i);
                            int id = availability.getInt("id");
                            String fieldName = availability.getString("field_name");
                            JSONArray availableHours = availability.getJSONArray("available_hours");

                            Log.d(TAG, "Field ID: " + id);
                            Log.d(TAG, "Field Name: " + fieldName);
                            Log.d(TAG, "Available Hours: " + availableHours.toString());
                        }

                        mainHandler.post(() -> callback.onSuccess(availabilityArray));
                    } else {
                        Log.e(TAG, "Response not successful: " + response.code() + " - " + response.message());
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

        Log.d(TAG, "Request URL: " + url);

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        if (authToken != null) {
            Log.d(TAG, "Authorization token: " + authToken);
        } else {
            Log.e(TAG, "Authorization token is null");
        }

        OkHttpClient client = new OkHttpClient(); // Ensure OkHttpClient is initialized

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
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONObject responseObject = new JSONObject(response.body().string());
                        JSONArray facilitiesArray = responseObject.getJSONArray("facilities");
                        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(facilitiesArray));
                    } else {
                        Log.e(TAG, "Response not successful: " + response.code() + " - " + response.message());
                        new Handler(Looper.getMainLooper()).post(() -> callback.onFailure("An error occurred."));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                    new Handler(Looper.getMainLooper()).post(() -> callback.onError(e.getMessage()));
                } finally {
                    response.close();
                }
            }
        });
    }



    public static void bookTimeSlots(Context context, int fieldId, int sportId, String date, String startTime, String endTime, final BookingCallback callback) {
        String url = ADDRESS + "/user/reservations";

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        if (authToken == null) {
            Log.e(TAG, "Authorization token is missing");
            mainHandler.post(() -> callback.onFailure("Authorization token is missing"));
            return;
        }

        // Create the JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("field_id", fieldId);
            jsonBody.put("sport_id", sportId);
            jsonBody.put("date", date);
            jsonBody.put("start_time", startTime);
            jsonBody.put("end_time", endTime);
            Log.d(TAG, "Request body: " + jsonBody.toString()); // Log request body
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON body: " + e.getMessage());
            mainHandler.post(() -> callback.onError(e.getMessage()));
            return;
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", authToken)
                .build();

        Log.d(TAG, "Request URL: " + url); // Log request URL

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "An error occurred: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    mainHandler.post(() -> callback.onSuccess());
                } else {
                    Log.e(TAG, "Response not successful: " + response.code() + " - " + response.message());
                    mainHandler.post(() -> callback.onFailure("An error occurred."));
                }
            }
        });
    }


    public static void uploadProfileImage(Context context, String imagePath, final UploadCallback callback) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("image_path", imagePath);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(postData.toString(), JSON_MEDIA_TYPE);

        String url = ADDRESS + "/user/profile/upload-image";

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
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        mainHandler.post(callback::onSuccess);
                    } else {
                        Log.e(TAG, "Response not successful: " + response.code() + " - " + response.message());
                        callback.onFailure("An error occurred.");
                    }
                } finally {
                    response.close();
                }
            }
        });
    }

    public static void fetchProfileImage(Context context, final FetchProfileImageCallback callback) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        Request.Builder requestBuilder = new Request.Builder()
                .url(ADDRESS + "/user/profile/image")
                .get();

        if (authToken != null) {
            requestBuilder.addHeader("Authorization", authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "Error fetching profile image: " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String base64Image = response.body().string();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("profile_image", base64Image);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(base64Image));
                } else {
                    Log.e(TAG, "Error fetching profile image: " + response.message());
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(response.message()));
                }
            }
        });
    }


    public void getRecommendedFields(Context context, final RecommendedFieldsCallback callback) {
        String url = ADDRESS + "/user/search/recommended";
        Log.d(TAG, "Requesting recommended fields from URL: " + url);

        // Retrieve the Authorization token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(AUTH_KEY, null);

        Request.Builder requestBuilder = new Request.Builder().url(url);

        if (authToken != null) {
            Log.d(TAG, "Authorization token: " + authToken);
            requestBuilder.addHeader("Authorization", authToken);
        } else {
            Log.e(TAG, "Authorization token is missing");
            callback.onFailure("Authorization token is missing");
            return;
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Request not successful: " + response.message());
                    callback.onError(response.message());
                    return;
                }
                try {
                    String jsonData = response.body().string();
                    Log.d(TAG, "Response data: " + jsonData);
                    JSONObject jsonResponse = new JSONObject(jsonData);
                    JSONArray facilitiesArray = jsonResponse.getJSONArray("facilities");
                    callback.onSuccess(facilitiesArray);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    callback.onError(e.getMessage());
                }
            }
        });
    }





    public interface RecommendedFieldsCallback {
        void onSuccess(JSONArray recommendedFieldsArray);
        void onFailure(String errorMessage);
        void onError(String error);
    }





    public interface FetchProfileImageCallback {
        void onSuccess(String base64Image);
        void onFailure(String errorMessage);
        void onError(String errorMessage);
    }


    // Interface for upload callbacks
    public interface UploadCallback {
        void onSuccess();
        void onFailure(String errorMessage);
        void onError(String errorMessage);
    }

    private static void postToMainThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public interface BookingCallback {
        void onSuccess();
        void onFailure(String errorMessage);
        void onError(String errorMessage);
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
        void onSuccess(JSONObject fieldDetails, JSONArray facilitySports);
        void onFailure(String errorMessage);
        void onError(String error);
    }

    public interface AvailableTimesCallback {
        void onSuccess(JSONArray times);
        void onFailure(String errorMessage);
        void onError(String errorMessage);
    }
}
