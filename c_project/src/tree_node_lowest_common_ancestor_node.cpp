#include <iostream>
#include <algorithm>
#include <set>
#include <vector>
#include <queue>
#include <unordered_set>
using namespace std;
struct TreeNode
{
    int val;
    struct TreeNode *left;
    struct TreeNode *right;
    TreeNode():left(nullptr),right(nullptr){}
};

class Solution
{
public:
    /**
     * 
     * @param root TreeNode类 
     * @param o1 int整型 
     * @param o2 int整型 
     * @return int整型
     */
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


    //返回元素target的父节点，这里是严格的父节点，target为根节点，则无解
    TreeNode *getParent(TreeNode *root, int &target)
    {
        if (root == nullptr)
        {
            return nullptr;
        }
        if (root->left != nullptr && root->left->val == target)
        {
            return root;
        }
        if (root->right != nullptr && root->right->val == target)
        {
            return root;
        }
        TreeNode *ptr = getParent(root->left, target);
        ptr = (ptr == nullptr) ? getParent(root->right, target) : ptr;
        return ptr;
    }
    //运行超时的方法，不推荐
    int lowestCommonAncestor_2(TreeNode *root, int o1, int o2)
    {

        TreeNode *p1 = getParent(root, o1);
        TreeNode *p2 = nullptr;
        int ans = INT_MIN;
        while (p1 != nullptr)
        {
            TreeNode *p2 = getParent(p1, o2);
            if (p2 != nullptr)
            {
                ans = p1->val;
                break;
            }
            else
            {
                p1 = getParent(root, p1->val);
            }
        }
        return ans == INT_MIN ? 0 : ans;
    }
    TreeNode *getCommonParent(TreeNode *root, int &o1, int &o2)
    {
        if (root == nullptr || root->val == o1 || root->val == o2)
        {
            return root;
        }
        TreeNode *left = getCommonParent(root->left, o1, o2);
        TreeNode *right = getCommonParent(root->right, o1, o2);
        if (left == nullptr)
        {
            return right;
        }
        if (right == nullptr)
        {
            return left;
        }
        return root;
    }
    //推荐：求公共祖先节点
    int lowestCommonAncestor(TreeNode *root, int o1, int o2)
    {
        return getCommonParent(root, o1, o2)->val;
    }
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
};
int main()
{
    Solution pp;
    int arr[] = {3, 5, 1, 6, 2, 0, 8, INT_MIN, INT_MIN, 7, 4};
    int len = sizeof(arr) / sizeof(int);
    cout << "len=" << len << endl;
    TreeNode *root = pp.insertTreeNode(arr, len);
    pp.disp(root);
    int target = 5;
    TreeNode *ptr = pp.getParent(root, target);
    cout<<"ptr="<<ptr->val<<endl;
    int ancestor = pp.lowestCommonAncestor(root, 3, 5);
    cout << "ancestor=" << ancestor << endl;
}