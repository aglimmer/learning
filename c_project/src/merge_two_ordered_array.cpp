#include <iostream>
#include <algorithm>
#include <set>
#include <vector>
#include<unordered_set>
using namespace std;
class Solution {
public:
    void merge(int A[], int m, int B[], int n) {
        int j = 0;
        int i;
        int len = m;
        for(i=0;i<len;i++){
            //如果B中元素小于A[i],则需要插入元素B[i],并扩展长度
            if(j<n && A[i]>B[j]){
                for(int k=len;k>=i;k--){
                    A[k] = A[k-1];
                }
                A[i] = B[j];
                len += 1;
                //B数组元素指向下一个
                j += 1;
            }
        }
        // B中元素还没有全部加入，继续
        while(j<n){
            A[i++] = B[j++];
        }
    }
};
int main(){
    Solution pp;
   
  
    int B[]={12,15};
    const int n = sizeof(B)/sizeof(int);
    int A[n+4]={1,4,8,11};
    int m = sizeof(A)/sizeof(int)-n;

    pp.merge(A,m,B,n);
    for(int &x:A){
        cout<<x<<",";
    }
}