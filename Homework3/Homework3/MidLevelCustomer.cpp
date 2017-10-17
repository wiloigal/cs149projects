//
//  MidLevelCustomer.cpp
//  Homework3
//
//  Created by Josh on 10/17/17.
//  Copyright © 2017 Josh. All rights reserved.
//

#include "MidLevelCustomer.hpp"
#include <cstdlib>
#include <random>
using namespace std;
MidLevelCustomer::MidLevelCustomer():Customer(){
    processTime = (rand()%(4 + 1 - 2) + 2);
    
}
