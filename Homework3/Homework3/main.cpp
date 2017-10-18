//
//  main.cpp
//  Homework3
//
//  Created by Josh on 10/16/17.
//  Copyright © 2017 Josh. All rights reserved.
//

#include <iostream>
#include <sstream>
#include <cstdlib>
#include <vector>
#include <ctime>

#include "Customer.hpp"
#include "Seller.hpp"
#include "HighLevelSeller.hpp"
#include "MidLevelSeller.hpp"
#include "LowLevelSeller.hpp"
#include "Seat.hpp"
#include "Ampitheatre.hpp"

using namespace std;

const int ONE_HOUR = 60;
const int HIGH_LEVEL_PRICE = 100;
const int HID_LEVEL_PRICE = 50;
const int LOW_LEVEL_PRICE = 20;
const int NumbLowSellers = 6;
const int NumbMidSellers = 3;
const int NumbHighSeller = 1;

int main(int argc, const char * argv[]) {
    stringstream sstream;
    sstream << argv[1];
    int N;//number of users
    sstream >> N;
    while (N == NULL){
        cout << "Integer was not detected." << endl;
        cout << "   How many customers will we receive?" << endl;
        cin >> N;
    }
    
    srand((int)time(0));
    
    LowLevelSeller lowSellers[NumbLowSellers];
    MidLevelSeller midSellers[NumbMidSellers];
    vector<Customer> allCustomers = vector<Customer>();
    
    HighLevelSeller H = HighLevelSeller("H");
    
    MidLevelSeller M1 = MidLevelSeller("M1");
    MidLevelSeller M2 = MidLevelSeller("M2");
    MidLevelSeller M3 = MidLevelSeller("M3");
    
    LowLevelSeller L1 = LowLevelSeller("L1");
    LowLevelSeller L2 = LowLevelSeller("L2");
    LowLevelSeller L3 = LowLevelSeller("L3");
    LowLevelSeller L4 = LowLevelSeller("L4");
    LowLevelSeller L5 = LowLevelSeller("L5");
    LowLevelSeller L6 = LowLevelSeller("L6");
    
    for(int i = 1; i <= N; i++){
        allCustomers.push_back(*new Customer());
    }
    lowSellers[0] = L1;
    lowSellers[1] = L2;
    lowSellers[2] = L3;
    lowSellers[3] = L4;
    lowSellers[4] = L5;
    lowSellers[5] = L6;
    
    midSellers[0] = M1;
    midSellers[1] = M2;
    midSellers[2] = M3;
    
    return 0;
}
