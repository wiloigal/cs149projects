//
//  Customer.cpp
//  Homework3
//
//  Created by Josh on 10/16/17.
//  Copyright © 2017 Josh. All rights reserved.
//

#include <cstdlib>
#include <climits>
#include "Customer.hpp"

int static RandomInt(int low, int high)
{
   
    if(RAND_MAX == INT_MAX)
        return ( rand() % (1+high-low) + low );
    else
        return ( ((rand()*RAND_MAX)+rand()) % (1+high-low) + low );
}
Customer::Customer(){
    
    arrivalTime = RandomInt(0,59);
    priority = RandomInt(1,3);
    if(priority == 1)
        processTime = RandomInt(1, 2);
    else if(priority == 2)
        processTime = RandomInt(2, 4);
    else
        processTime = RandomInt(4, 7);
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
int Customer::getPriority() const{
    return priority;
}



