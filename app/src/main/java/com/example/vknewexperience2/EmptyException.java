package com.example.vknewexperience2;

import io.reactivex.rxjava3.functions.Supplier;

public class EmptyException extends Throwable {

    public EmptyException() {
        super("name is empty");
    }
}
