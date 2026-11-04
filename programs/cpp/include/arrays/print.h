#ifndef PRINT_H
#define PRINT_H

#include <iostream>
#include <vector>

inline void print_array(int arr[], int length)
{
    for (int i = 0; i < length; i++)
    {
        std::cout << arr[i] << " ";
    }

    std::cout << std::endl;
}

inline void print_vector(std::vector<int> arr, int length)
{
    for (int i = 0; i < length; i++)
    {
        std::cout << arr[i] << " ";
    }

    std::cout << std::endl;
}

#endif
