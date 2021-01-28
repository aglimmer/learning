#include <iostream>
#include <vector>
#include<unordered_map>
#include<algorithm>
using namespace std;

vector<int> twoSum(vector<int> &numbers, int target)
{
    if(numbers.size()<2){
        return {};
    }
    // sort(numbers.begin(),numbers.end());
    int right = numbers.size()-1;
    for(int i=0;i<right;i++){
        for(int j=i+1;j<=right;j++){
            if(numbers[i]+numbers[j]==target){
                return {i+1,j+1};
            }
        } 
    }
    return {};
}
vector<int> twoSum_2(vector<int> &numbers, int target)
{
    unordered_map<int,int> buff;
    for(int i=0;i<numbers.size();i++){
        buff[numbers[i]] = i;
    }
    for(int i=0;i<numbers.size();i++){
        int another = target-numbers[i];
        if(buff.count(another)!=0 && buff[another]!=i){
            return {i+1,buff[another]+1};
        }
    }
    return {};
}
int main(){
    vector<int> nums = {3,2,4};
    int target = 6;
    vector<int> ans = twoSum_2(nums,target);
    for(int &x:ans){
        cout<<x<<",";
    }
}