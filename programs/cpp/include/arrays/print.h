#ifndef PRINT_H
#define PRINT_H

#include <iostream>

inline void print_array(int arr[], int length)
{
    for (int i = 0; i < length; i++)
    {
        std::cout << arr[i] << " ";
    }

    std::cout << std::endl;
}

#endif
