package com.chooloo.www.callmanager.proof.aitech.network.retrofit;


import android.content.Context;

import com.chooloo.www.callmanager.proof.aitech.listener.CallLoginListener;
import com.chooloo.www.callmanager.proof.aitech.listener.ForgotListener;
import com.chooloo.www.callmanager.proof.aitech.listener.LoginListener;
import com.chooloo.www.callmanager.proof.aitech.listener.SignUpListener;
import com.chooloo.www.callmanager.proof.aitech.listener.SyncListener;
import com.chooloo.www.callmanager.proof.aitech.model.ForgotModel;
import com.chooloo.www.callmanager.proof.aitech.model.LoginModel;
import com.chooloo.www.callmanager.proof.aitech.model.SignUpModel;
import com.chooloo.www.callmanager.proof.aitech.model.SyncModel;
import com.chooloo.www.callmanager.proof.aitech.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.chooloo.www.callmanager.proof.aitech.util.Constants.USER_NAME;

public class RetrofitCall {
    Constants constants;

    public RetrofitCall() {
        constants = new Constants();
    }

    public void getLoginFromUser(HashMap<String, String> map, Context context, LoginListener loginListener) {
        final ApiInterface apiInterface = new ApiClient().getClient(context, Constants.BASE_URL).create(ApiInterface.class);
        Call<LoginModel> loginCall = apiInterface.getUserLogin(map);
        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()) {
                    Constants.USER_AUTH_TOKEN = response.body().getSuccess().getToken();
                    USER_NAME = response.body().getSuccess().getUser().getName();
                    loginListener.onSuccess();
                } else {
                    Constants.USER_AUTH_TOKEN = "";
                    loginListener.onFailure("Unauthorised");
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                loginListener.onFailure("Something went wrong. Try again later");
            }
        });
    }


    public void getSignUpFromUser(HashMap<String, String> map, Context context, SignUpListener loginListener) {
        final ApiInterface apiInterface = new ApiClient().getClient(context, Constants.BASE_URL).create(ApiInterface.class);
        Call<SignUpModel> signUpCall = apiInterface.getSignUpLogin(map);
        signUpCall.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                if (response.isSuccessful()) {
                    loginListener.onSuccess();
                } else {
                    loginListener.onFailure("Unauthorised");
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                loginListener.onFailure("Something went wrong. Try again later");
            }
        });
    }


    public void getForgotUser(HashMap<String, String> map, Context context, ForgotListener forgotListener) {
        final ApiInterface apiInterface = new ApiClient().getClient(context, Constants.BASE_URL).create(ApiInterface.class);
        Call<ForgotModel> forgotCall = apiInterface.getForgotUser(map);
        forgotCall.enqueue(new Callback<ForgotModel>() {
            @Override
            public void onResponse(Call<ForgotModel> call, Response<ForgotModel> response) {
                if (response.isSuccessful()) {
                    forgotListener.onSuccess(response.body().getSuccess().getMessage());
                } else {
                    forgotListener.onFailure("Not a valid user. Please Register");
                }
            }

            @Override
            public void onFailure(Call<ForgotModel> call, Throwable t) {
                forgotListener.onFailure("Something went wrong. Try again later");
            }
        });
    }

    public void uploadSyncLog(HashMap<String, String> map, Context context, SyncListener syncListener) {
         final ApiInterface apiInterface = new ApiClient().getClient(context, Constants.BASE_URL).create(ApiInterface.class);
        Call<SyncModel> syncCall = apiInterface.sendSyncLog(map, "Bearer " + Constants.USER_AUTH_TOKEN);
        syncCall.enqueue(new Callback<SyncModel>() {
            @Override
            public void onResponse(Call<SyncModel> call, Response<SyncModel> response) {
                if (response.isSuccessful()) {
                    syncListener.onSuccess(response.body().getSuccess().getMessage());
                } else {
                    syncListener.onFailure("Unauthenticated.");
                }
            }

            @Override
            public void onFailure(Call<SyncModel> call, Throwable t) {
                syncListener.onFailure("Something went wrong. Try again later");
            }
        });
    }

    public void getCallProofCred(String email, String password, String timeZone, String token, String phone,
                                 Context context, String url, CallLoginListener callLoginListener) {
        final ApiInterface apiInterface = new ApiClient().getClient(context, Constants.BASE_URL).create(ApiInterface.class);
        Call<String> forgotCall = apiInterface.getCallProofLogin(email, password, timeZone, token, phone, url);
        forgotCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String results = response.body();
                    String[] loginArray = results.split(Pattern.quote("||"));
                    if (loginArray[0].equals("1")) {
                        Constants.CALL_PROOF_USER_ID = loginArray[2];
                        callLoginListener.onSuccess("Connected to CALL-PROOF");
                    } else {
                        try {
                            if (Integer.valueOf(results) == -1) {
                                callLoginListener.onError("There was an error with your E-Mail/Password combination. Please try again.");
                            } else if (Integer.valueOf(results) == -2) {
                                callLoginListener.onError("Your account is disabled. Please contact admin.");
                            }
                        } catch (Exception e) {
                            callLoginListener.onError(e.toString());
                        }
                    }
                } else {
                    callLoginListener.onError("Unauthorised");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callLoginListener.onError("Something went wrong. Try again later");
            }
        });
    }

    public void uploadSyncLogFile(Map<String, RequestBody> map, MultipartBody.Part file, Context context, SyncListener syncListener) {
        final ApiInterface apiInterface = new ApiClient().getClient(context, Constants.BASE_URL).create(ApiInterface.class);
        Call<SyncModel> syncCall = apiInterface.sendSyncLogFile(map, file,"Bearer " + Constants.USER_AUTH_TOKEN);
        syncCall.enqueue(new Callback<SyncModel>() {
            @Override
            public void onResponse(Call<SyncModel> call, Response<SyncModel> response) {
                if (response.isSuccessful()) {
                    syncListener.onSuccess(response.body().getSuccess().getMessage());
                } else {
                    syncListener.onFailure("Sync Failed.");
                }
            }

            @Override
            public void onFailure(Call<SyncModel> call, Throwable t) {
                syncListener.onFailure("Something went wrong. Try again later");
            }
        });
    }

}