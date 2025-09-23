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