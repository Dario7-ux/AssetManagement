package org.example.assets.application.ports.out;

public interface PasswordOut {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}

