#include <iostream>
#include <algorithm>
#include <set>
#include <vector>
#include<unordered_set>
using namespace std;
int maxLength(vector<int> &arr)
{
    if (arr.size() <= 1)
    {
        return arr.size();
    }
    int ans = 0;
    unordered_set<int> buff;
    for (int i = 1; i < arr.size(); i++)
    {
        int j = i;
        while (j >= 0 && buff.insert(arr[j--]).second);
        ans = max(ans, int(buff.size()));
        buff.clear();
    }
    return ans;
}

int main()
{
    vector<int> nums = {0, 2, 5, 2, 3, 4, 5};
    cout << "ans=" << maxLength(nums);
}