package com.example.vknewexperience2;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class User {
    String name =" ";

    public Single getName() {
        return Single.just(name);
    }

    public Completable setName(String name) {
        if(name.isEmpty()){
            return Completable.error(new EmptyException());
        }
        this.name = name;
        return Completable.complete();
    }
}
