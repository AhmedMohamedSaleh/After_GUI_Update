package com.example.android.demo;

import android.util.Log;

public class Session_Token {
        private static String token = null;

        public static String getToken() {
                return token;
        }

        public static void setToken(String token) {
                token = token.substring(1,token.length()-1);
            Log.d("token test :- " , token);
                Session_Token.token = token;
        }
}
