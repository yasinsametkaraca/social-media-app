package com.example.questionapp.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super();  //RuntimeException in classlarını çağırıyoruz super diyerek.
    }

    public UserNotFoundException(String message) {  //kendi exception mesajimiz için
        super(message);
    }
}
