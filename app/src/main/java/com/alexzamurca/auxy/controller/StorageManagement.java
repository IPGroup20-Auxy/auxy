package com.alexzamurca.auxy.controller;

// Interface with the basic actions for storage management
public interface StorageManagement
{
    boolean add(Object object, int positionalIndex);
    Object remove(int positionalIndex);
    boolean update(Object object, int positionalIndex);
}
