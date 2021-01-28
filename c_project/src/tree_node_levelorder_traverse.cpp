#include <iostream>
#include <algorithm>
#include <set>
#include <vector>
#include <queue>
#include <unordered_set>
#include <iterator>
using namespace std;
struct TreeNode
{
    int val;
    struct TreeNode *left;
    struct TreeNode *right;
    TreeNode() : left(nullptr), right(nullptr) {}
};

class Solution
{
public:
    void disp(TreeNode *root)
    {
        TreeNode *curr = nullptr;
        queue<TreeNode *> qe;
        qe.push(root);
        while (!qe.empty())
        {
            curr = qe.front();
            cout << curr->val << "->";
            qe.pop();
            if (curr->left)
            {
                qe.push(curr->left);
            }
            if (curr->right)
            {
                qe.push(curr->right);
            }
        }
        cout << endl;
    }
    TreeNode *insertTreeNode(int arr[], int n)
    {

        if (n <= 0)
        {
            return nullptr;
        }
        int k = 0;
        TreeNode *root = new TreeNode;
        root->val = arr[k++];
        cout << "root=" << root->val << endl;

        queue<TreeNode *> qe;
        TreeNode *curr = nullptr;
        qe.push(root);
        while (!qe.empty())
        {
            curr = qe.front();
            // cout<<"val="<<(curr->val)<<endl;
            qe.pop();
            if (k < n && curr->left == nullptr)
            {
                curr->left = new TreeNode;
                curr->left->val = arr[k++];
            }
            if (k < n && curr->right == nullptr)
            {
                curr->right = new TreeNode;
                curr->right->val = arr[k++];
            }
            if (curr->left)
            {
                qe.push(curr->left);
            }
            if (curr->right)
            {
                qe.push(curr->right);
            }
            //				cout<<"k="<<k<<endl;
        }
        // cout<<"ok"<<endl;
        return root;
    }
    /**
     * 
     * @param root TreeNode类 
     * @return int整型vector<vector<>>
     */
    //二叉树层次遍历
    vector<vector<int>> levelOrder(TreeNode *root)
    {
        if (root == nullptr)
        {
            return {};
        }
        queue<TreeNode *> qe;
        TreeNode *curr = nullptr;
        qe.push(root);
        vector<vector<int>> ans;
        //cnt计算每一层节点个数
        int cnt = 0;
        //k计算遍历的节点个数
        int k = 1;
        vector<int> levels;
        while (!qe.empty())
        {
            curr = qe.front();
            levels.push_back(curr->val);
            qe.pop();
            k--;
            if (curr->left != nullptr)
            {
                qe.push(curr->left);
                cnt++;
            }
            if (curr->right != nullptr)
            {
                qe.push(curr->right);
                cnt++;
            }
            //完成一层遍历
            if (k == 0)
            {
                k = cnt;
                cnt = 0;
                ans.push_back(levels);
                levels.clear();
            }
        }
        return ans;
    }
};
int main()
{
    Solution pp;
    int arr[] = {3, 5, 1, 6, 2, 0, 8, INT_MIN, INT_MIN, 7, 4};
    int len = sizeof(arr) / sizeof(int);
    cout << "len=" << len << endl;
    TreeNode *root = pp.insertTreeNode(arr, len);
    pp.disp(root);
    vector<vector<int>> ans = pp.levelOrder(root);
    for (vector<int> &vs : ans)
    {
        copy(vs.begin(), vs.end(), ostream_iterator<int>(cout, ","));
        cout << endl;
    }
}