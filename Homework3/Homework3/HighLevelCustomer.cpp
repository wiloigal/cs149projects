//
//  HighLevelCustomer.cpp
//  Homework3
//
//  Created by Josh on 10/17/17.
//  Copyright © 2017 Josh. All rights reserved.
//
#include <cstdlib>
#include "HighLevelCustomer.hpp"
#include <random>
using namespace std;
HighLevelCustomer::HighLevelCustomer():Customer(){
   processTime = (rand()%(2 + 1 - 1) + 1);
}
