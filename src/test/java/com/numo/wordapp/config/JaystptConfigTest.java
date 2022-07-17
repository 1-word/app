package com.numo.wordapp.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class JaystptConfigTest {
    public static void main(String[] args){
        final String key = "";
        final String pw = "";
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(key);
        jasypt.setAlgorithm("PBEWithMD5AndDES");

        String encryptedText = jasypt.encrypt(pw);   //암호화
        String planText = jasypt.decrypt(encryptedText);    //복호화

        System.out.println("encryptedText = " + encryptedText);
        System.out.println("planText = " + planText);
    }
}
