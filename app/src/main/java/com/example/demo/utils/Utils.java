package com.example.demo.utils;

import com.example.demo.model.GioHang;
import com.example.demo.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.1.13/banhang/";
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();

    public static User user_current = new User();
}
