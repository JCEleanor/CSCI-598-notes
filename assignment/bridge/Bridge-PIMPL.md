### Header File: dataprocessor.h

```c++
#ifndef DATAPROCESSOR_H
#define DATAPROCESSOR_H

#include <memory> // For std::unique_ptr

class DataProcessor
{
private:
    struct Impl;
    // Smart pointer to implementation
    std::unique_ptr<Impl> pimpl;

public:
    DataProcessor();

    void addNumber(int number);
    int getSum() const;
};

#endif
```

#### Source File: dataprocessor.cpp

```c++

#include "dataprocessor.h"
#include <vector>
#include <numeric>
#include <iostream>

struct DataProcessor::Impl
{
    std::vector<int> data;
};

// Constructor should initialize pimpl with a new Impl using std::make_unique<Impl>()
DataProcessor::DataProcessor()
    : pimpl(std::make_unique<Impl>()) {}

// Implement DataProcessor constructor, addNumber, getSum
void DataProcessor::addNumber(int number)
{
    // addNumber should add number to pimpl->data
    pimpl->data.push_back(number);
}

int DataProcessor::getSum() const
{
    // getSum should return sum of all numbers in pimpl->data
    return std::accumulate(pimpl->data.begin(), pimpl->data.end(), 0);
}

// No destructor needed (std::unique_ptr handles deletion)
// Main function for testing
int main()
{
    DataProcessor processor;
    processor.addNumber(10);
    processor.addNumber(20);
    processor.addNumber(30);
    std::cout << processor.getSum() << std::endl;
    return processor.getSum(); // Should return 60
}


```
