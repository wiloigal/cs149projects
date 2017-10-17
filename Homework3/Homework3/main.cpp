//
//  main.cpp
//  Homework3
//
//  Created by Josh on 10/16/17.
//  Copyright © 2017 Josh. All rights reserved.
//

#include <iostream>
#include <cstdlib>
#include "Customer.hpp"
#include "HighLevelCustomer.hpp"
#include "MidLevelCustomer.hpp"
#include "LowLevelCustomer.hpp"
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
    //THis will have to change because N is supposed to be a command line parameter
    int N = 0;//number of user
    cout << "WELCOME TO TICKET_EXCHANGE!" << endl;
    cout << "   How many customers will we receive?" << endl;
    cin >> N;
    
    cout << endl;
    
    return 0;
}
