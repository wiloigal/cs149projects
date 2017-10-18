//
//  Customer.hpp
//  Homework3
//
//  Created by Josh on 10/16/17.
//  Copyright © 2017 Josh. All rights reserved.
//

#ifndef Customer_hpp
#define Customer_hpp

#include <stdio.h>
#include "Seat.hpp"


/**
 * This class holds information for someone that wants to purchase tickets from the seller
 *
 */
class Customer{
protected:
    int arrivalTime;
    int processTime;
    int priority;
public:
    Customer();
    Seat seat;
    int getArrivalTime() const;
    int getProcessTime() const;
    int getPriority() const;
    bool operator <(const Customer & customerObj) const;
};




#endif /* Customer_hpp */
