package com.mabawa.nnpdairy;

import org.springframework.boot.SpringApplication;

import java.util.TimeZone;
import java.util.UUID;

public class GenerateUUID {
    public static void main(String[] args) {
        final String uuid = UUID.randomUUID().toString();
        System.out.println("uuid = " + uuid);
    }
}
