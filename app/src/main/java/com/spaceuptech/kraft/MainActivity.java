package com.spaceuptech.kraft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.tv.TvInputService;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.data.LoginResponse;
import com.spaceuptech.kraft.events.EventsFragment;
import com.spaceuptech.kraft.login.LoginActivity;
import com.spaceuptech.kraft.posts.HomeFragment;
import com.spaceuptech.kraft.posts.PostActivity;
import com.spaceuptech.kraft.profile.ProfileFragment;
import com.spaceuptech.kraft.signup.SignUpActivity;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static String[] colorList = {"#1976D2", "#3F51B5", "#2196F3", "#E91E63", "#009688","#4CAF50", "#FFC107", "#FF5722", "#795548", "#607D8B" };
    private static final String TAG = "Main Activity";
    private Gson gson = new Gson();
    private TabLayout tabLayout;

    private boolean masterLoginDone = false;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverSilentLogin, new IntentFilter(DataService.REQUEST_SILENT_LOGIN));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(DataService.REQUEST_LOGIN));

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutMain);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerMain);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        silentLogin();
    }



    public void silentLogin() {
        OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (pendingResult.isDone()) {
            GoogleSignInResult result = pendingResult.get();
            handleGoogleSignInResult(result);
            return;
        }

        pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
            @Override
            public void onResult(@NonNull GoogleSignInResult result) {
                handleGoogleSignInResult(result);
            }
        });
    }

    public void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            String idToken = acct.getIdToken();

            JsonObject json = new JsonObject();
            json.addProperty("idToken", idToken);
            json.addProperty("auth", "google");
            ClientApi.call(this, DataService.ENGINE_LOGIN, "login", new Gson().toJson(json).getBytes());
        } else {
            // Signed out, show unauthenticated UI.
            Snackbar.make(findViewById(R.id.activityLoginLayout), "Could not sign in", Snackbar.LENGTH_LONG).show();
        }
    }

    private BroadcastReceiver broadcastReceiverSilentLogin = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (masterLoginDone) silentLogin();
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverSilentLogin);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
    private void setUpTabIcons(){
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_events);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person);
    }

    public void createPost(View view) {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] array = intent.getByteArrayExtra("args");
            LoginResponse response = new Gson().fromJson(new String(array), LoginResponse.class);
            DataService.SESSION_ID = response.sessionId;

            Log.d(TAG, "Session received - " + DataService.SESSION_ID);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("sessionId", DataService.SESSION_ID);
            jsonObject.addProperty("refresh", true);
            ClientApi.call(getApplicationContext(), DataService.ENGINE_POST, DataService.REQUEST_POSTS, gson.toJson(jsonObject).getBytes());
            ClientApi.call(getApplicationContext(), DataService.ENGINE_EVENT, DataService.REQUEST_EVENTS, gson.toJson(jsonObject).getBytes());

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_user), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name", response.user.name);
            editor.putString("email", response.user.email);
            editor.putString("img", response.user.img);
            editor.putString("userId", response.user.userId);
            editor.commit();


            masterLoginDone = true;
        }
    };

    class Pager extends FragmentStatePagerAdapter {
        private int tabCount;

        Pager(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount= tabCount;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new EventsFragment();
                case 2 :
                    return new ProfileFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}

