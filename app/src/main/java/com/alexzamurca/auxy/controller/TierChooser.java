package com.alexzamurca.auxy.controller;

public class TierChooser
{
    // Tier Range
    final int[] GREEN_RANGE = {0, 10};
    final int[] YELLOW_RANGE = {11, 20};
    final int[] RED_RANGE = {21, Integer.MAX_VALUE};


    /**
     * Given the number of crimes returns which tier the area with the input number of crimes belongs in.
     *
     * @param numberOfCrimes
     * @return -1 if input is not valid, 0 if in green tier, 1 if in yellow range, 2 if in red range
     */
    public int getTier(int numberOfCrimes)
    {
        if(numberOfCrimes >= GREEN_RANGE[0] && numberOfCrimes <= GREEN_RANGE[1])
        {
            return 0;
        }
        else if(numberOfCrimes >= YELLOW_RANGE[0] && numberOfCrimes <= YELLOW_RANGE[1])
        {
            return 1;
        }
        else if(numberOfCrimes >= RED_RANGE[0] && numberOfCrimes <= RED_RANGE[1])
        {
            return 2;
        }
        else
        {
            return -1;
        }
    }
}
