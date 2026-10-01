#include <iostream>

using namespace std;

int binary_search(int arr[], int val, int low, int high)
{
    if (low > high)
        return -1;

    int mid = (low + high) / 2;

    if (arr[mid] == val)
    {
        return mid;
    }
    else if (arr[mid] > val)
    {
        return binary_search(arr, val, low, mid - 1);
    }
    else
    {
        return binary_search(arr, val, mid + 1, high);
    }
}

int binary_search(int arr[], int length, int val)
{
    return binary_search(arr, val, 0, length - 1);
}

int main()
{
    int arr[] = {10, 20, 30, 40, 50};
    int val = 40;

    int length = sizeof(arr) / sizeof(arr[0]);

    int index = binary_search(arr, length, val);

    if (index != -1)
        cout << "Value " << val << " found at index " << index << endl;
    else
        cout << "Value " << val << " not found in the array" << endl;

    return 0;
}
