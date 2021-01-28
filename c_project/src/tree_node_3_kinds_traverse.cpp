#include<iostream>
#include<list>
#include<vector>
#include<stack>
#include<queue>
#include<algorithm>
using namespace std;
struct TreeNode {
	int val;
	TreeNode *left;
	TreeNode *right;
	TreeNode():left(nullptr),right(nullptr) {
	}
};

class Solution {
	public:
		/**
		 *
		 * @param root TreeNode类 the root of binary tree
		 * @return int整型vector<vector<>>
		 */
		Solution() {
			root = nullptr;
		}
		TreeNode* insertTreeNode(int arr[],int n) {
			if(n<=0) {
				return nullptr;
			}
			if(root==nullptr) {
				root = new TreeNode;
				root->val = arr[0];
				cout<<"root="<<root->val<<endl;
			}
			queue<TreeNode*> qe;
			TreeNode *curr = nullptr;
			qe.push(root);
			int k = 1;
			while(!qe.empty()) {
				curr = qe.front();
//				cout<<"val="<<(curr->val)<<endl;
				qe.pop();
				if(k<n && curr->left==nullptr) {
					curr->left = new TreeNode;
					curr->left->val = arr[k];
					k++;
				}
				if(k<n && curr->right==nullptr) {
					curr->right = new TreeNode;
					curr->right->val = arr[k];
					k++;
				}
				if(curr->left) {
					qe.push(curr->left);
				}
				if(curr->right) {
					qe.push(curr->right);
				}
//				cout<<"k="<<k<<endl;
			}
//			cout<<"ok"<<endl;
			return nullptr;
		}
		void disp() {
			TreeNode *curr = nullptr;
			queue<TreeNode*> qe;
			qe.push(root);
			while(!qe.empty()) {
				curr = qe.front();
				cout<<curr->val<<"->";
				qe.pop();
				if(curr->left) {
					qe.push(curr->left);
				}
				if(curr->right) {
					qe.push(curr->right);
				}
			}
			cout<<endl;
		}
		//中序遍历 
		vector<int> inorderTravel() {
			vector<int> ans;
			TreeNode *subtree = root;
			inorderTravel(subtree,ans);
			return ans;
		}
		void inorderTravel(TreeNode *subtree,vector<int> &ans) {
			if(subtree!=nullptr) {
				inorderTravel(subtree->left,ans);
				ans.push_back(subtree->val);
				inorderTravel(subtree->right,ans);
			}
		}
		//前序遍历 
		vector<int> preorderTravel() {
			vector<int> ans;
			TreeNode *subtree = root;
			preorderTravel(subtree,ans);
			return ans;
		}
		void preorderTravel(TreeNode *subtree,vector<int> &ans) {
			if(subtree!=nullptr) {
				ans.push_back(subtree->val);
				preorderTravel(subtree->left,ans);
				preorderTravel(subtree->right,ans);
			}
		}
		//后序遍历 
		vector<int> postorderTravel() {
			vector<int> ans;
			TreeNode *subtree = root;
			postorderTravel(subtree,ans);
			return ans;
		}
		void postorderTravel(TreeNode *subtree,vector<int> &ans) {
			if(subtree!=nullptr) {
				postorderTravel(subtree->left,ans);
				postorderTravel(subtree->right,ans);
				ans.push_back(subtree->val);
			}
		}
		//返回前序、中序、后序遍历三种遍历结果 
		vector<vector<int> > threeOrders(TreeNode* root) {
			this->root = root;
			vector<vector<int> > ans;
			ans.push_back(preorderTravel());
			ans.push_back(inorderTravel());
			ans.push_back(postorderTravel());
			return ans;
		}
		TreeNode *getRoot(){
			return root;
		}
	private:
		TreeNode *root;
};
int main() {
	Solution pp;
	int arr[]= {1,2,3,4,5,6,7,8,9,0};
	int len = sizeof(arr)/sizeof(int);
	//建立二叉树，从左到右逐层建立 
	pp.insertTreeNode(arr,len);
	vector<vector<int> > ans = pp.threeOrders(pp.getRoot());
	// for(auto &vs:ans){
	// 	for(auto &x:vs){
	// 		cout<<x<<"->";
	// 	}
	// 	cout<<endl;
	// }
//	pp.disp();
	vector<int> vs = pp.postorderTravel();
	for(int &x:vs) {
		cout<<x<<"->";
	}
	cout<<endl;

}