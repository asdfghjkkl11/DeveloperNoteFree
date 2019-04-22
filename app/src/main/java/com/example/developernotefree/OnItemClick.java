package com.example.developernotefree;

import java.util.ArrayList;

public interface OnItemClick {
    void onClick (String value);
    ArrayList<String> push(String value);
    ArrayList<String> pop();
}
