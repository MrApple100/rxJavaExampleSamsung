package com.example.vknewexperience2;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAuthenticationResult;
import com.vk.api.sdk.auth.VKScope;
import com.vk.api.sdk.exceptions.VKApiException;
import com.vk.api.sdk.requests.VKRequest;
import com.vk.api.sdk.utils.VKUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!VK.isLoggedIn())
            authLauncher.launch(new ArrayList<VKScope>(Arrays.asList(VKScope.WALL,VKScope.GROUPS,VKScope.FRIENDS)));


        TextView textView = findViewById(R.id.TV);

        String[] fingerPrint = VKUtils.getCertificateFingerprint(this, getPackageName());
        Log.d("Kuuu ", ""+ fingerPrint[0]);

        Observable obs = obserIdOwner("mrapple100")
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Throwable {
                        return Integer.parseInt(s)+1;
                    }
                }).share();

        Observer observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(Integer o) {
                Log.d("Ku4","!@#");
                textView.setText(""+o);
               // textView.app
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        for (int i = 0; i < 10; i++) {
            obs.subscribe(observer);
        }


        User user = new User();
        disposables.add((Disposable) user.getName()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );

            user.setName("Makarons")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
    }



    public String getIdOwner(String screenName){
        String id ="0";
        Log.d("ku2","Start");

        VKRequest<JSONObject> r = new VKRequest("users.get", VK.getApiVersion())
                .addParam("user_ids",screenName);

        try {
            JSONObject response = VK.executeSync(r);
            Log.d("Ku4",response.toString());
            id = response.getJSONArray("response")
                    .getJSONObject(0)
                    .getString("id");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (VKApiException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public Observable<String> obserIdOwner(String screenName){
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getIdOwner(screenName);
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> apply(Throwable throwable) throws Throwable {
                return Observable.just("0");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    ActivityResultLauncher<Collection<VKScope>> authLauncher = VK.login(this, new ActivityResultCallback<VKAuthenticationResult>() {
        @Override
        public void onActivityResult(VKAuthenticationResult o) {
            //
        }
    });


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}