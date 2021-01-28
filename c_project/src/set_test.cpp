#include <iostream>
#include <algorithm>
#include <set>
#include <vector>
using namespace std;
struct Comp
{
    //使用自定义规则，从大到小,必须要const修饰
    bool operator()(const int &x, const int &y) const
    {
        return x > y;
    }
};
int main()
{
    // vector<int> nums = {0, 2, 5, 2, 3, 4, 5};
    // cout<<"ans="<<maxLength(nums);
    //Comp指定从大到小
    //元素类型如果是struct或类，重载 "<" 运算符比较大小以确定插入顺序
    set<int,Comp> gather;
    gather.insert(100);
    gather.insert(102);
    gather.insert(103);
    //元素是否已存在
    cout<<gather.count(100)<<endl;
    pair<set<int>::iterator, bool> res = gather.insert(108);
    //插入是否成功
    cout << res.second << endl;
    //迭代输出
    for (auto it = gather.begin(); it != gather.end(); ++it)
    {
        cout << *it << ",";
    }
  
}