package com.alexzamurca.auxy.controller;

public interface StorageManagement
{
    boolean add(Object object, int positionalIndex);
    Object remove(int positionalIndex);
    boolean update(Object object, int positionalIndex);
}
