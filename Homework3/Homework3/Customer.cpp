//
//  Customer.cpp
//  Homework3
//
//  Created by Josh on 10/16/17.
//  Copyright © 2017 Josh. All rights reserved.
//
#include <cstdlib>
#include "Customer.hpp"
Customer::Customer(){
    arrivalTime = processTime = (rand()%(60 + 1 - 1) + 1);
}



bool Customer::operator <(const Customer & customerObj) const{
    return arrivalTime > customerObj.arrivalTime;
}
int Customer::getArrivalTime() const{
    return arrivalTime;
}
int Customer::getProcessTime() const{
    return processTime;
}

