//
//  LowLevelCustomer.cpp
//  Homework3
//
//  Created by Josh on 10/17/17.
//  Copyright © 2017 Josh. All rights reserved.
//

#include "LowLevelCustomer.hpp"
#include <cstdlib>
#include <random>
using namespace std;
LowLevelCustomer::LowLevelCustomer():Customer(){
    processTime = (rand()%(7 + 1 - 4) + 4);
}
