//
//  main.cpp
//  Homework3
//
//  Created by Josh on 10/16/17.
//  Copyright © 2017 Josh. All rights reserved.
//

#include <iostream>
#include <cstdlib>
#include <vector>

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





/////////
//std::rand() % (Max + 1 - Min) + Min;
/////////
int main(int argc, const char * argv[]) {
    //THis will have to change because N is supposed to be a command line parameter
    int N;//number of user
    cout << "WELCOME TO TICKET_EXCHANGE!" << endl;
    cout << "   How many customers will we receive?" << endl;
    cin >> N;

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
    
    Customer* c1 = new Customer();
    cout << c1->getPriority() << " " << c1->getArrivalTime() << " " << c1->getProcessTime() << endl;
    Customer* c2 =  new Customer();
    cout << c2->getPriority() << " " << c2->getArrivalTime() << " " << c2->getProcessTime() << endl;
    Customer* c3 = new Customer();
    cout << c3->getPriority() << " " << c3->getArrivalTime() << " " <<  c3->getProcessTime() <<endl;
    
    Customer* c4 = new Customer();
    cout << c4->getPriority() << " " << c4->getArrivalTime() << " " <<  c4->getProcessTime() <<endl;
    priority_queue<Customer> allCustomers = priority_queue<Customer>();
    Customer* c5 = new Customer();
    cout << c5->getPriority() << " " << c5->getArrivalTime() << " " <<  c5->getProcessTime() <<endl;
    
    cout << endl;
    
    return 0;
}
