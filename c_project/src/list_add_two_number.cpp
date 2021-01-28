#include <iostream>
using namespace std;
struct ListNode
{
    int val;
    struct ListNode *next;
    ListNode(int x):val(x){}
};

class Solution
{
public:
    //从尾部插入
    ListNode *insertNode(int arr[], int n)
    {
        ListNode *head = nullptr;
        ListNode *curr = nullptr;
        for (int i = 0; i < n; i++)
        {
            if (i == 0)
            {
                head = new ListNode(arr[i]);
                head->next = nullptr;
                curr = head;
            }
            else
            {
                curr->next = new ListNode(arr[i]);
                curr = curr->next;
            }
        }
        curr->next = nullptr;
        return head;
    }
    //从头部插入
    ListNode *insertFront(int arr[], int n)
    {
        ListNode *tailer = nullptr;
        ListNode *lastNode, *newNode;
        for (int i = 0; i < n; i++)
        {
            if (i == 0)
            {
                //头部插入，第一个节点作为尾部节点
                tailer = new ListNode(arr[i]);
                tailer->next = nullptr;
                //每次记录上一个节点
                lastNode = tailer;
            }
            else
            {
                newNode = new ListNode(arr[i]);
                //新节点的后序节点，要连接上一个节点
                newNode->next = lastNode;
                lastNode = newNode;
            }
        }
        return newNode;
    }
    void disp(ListNode *head)
    {
        ListNode *curr = head;
        while (curr != nullptr)
        {
            cout << curr->val << "->";
            curr = curr->next;
        }
        cout << endl;
    }
    //翻转链表，变为逆序
    ListNode *reverseListNode(ListNode *head)
    {
        //节点为空或只有一个节点，直接返回
        if (head == nullptr || head->next == nullptr)
        {
            return head;
        }
        ListNode *p1 = head;
        ListNode *p2 = p1->next;
        //翻转需要两两交换
        while (p2 != nullptr)
        {
            ListNode *tmp = p2->next;
            p2->next = p1;
            p1 = p2;
            p2 = tmp;
        }
        //翻转后，头结点变为尾结点，并且尾结点的后序节点为空
        head->next = nullptr;
        return p1;
    }

    ListNode *addInList(ListNode *head1, ListNode *head2)
    {
        //head1,head2需要翻转
        //比如：937 --> 7 3 9
        //      63 --> 3 6
        //对应位依次相加得：1 0 0 0
        ListNode *p1 = reverseListNode(head1);
        ListNode *p2 = reverseListNode(head2);
        ListNode *lastNode = nullptr;
        ListNode *newNode = nullptr;
        //记录有无进位
        int step = 0;
        while (p1 != nullptr || p2 != nullptr)
        {
            int a = 0;
            int b = 0;
            if (p1 != nullptr)
            {
                a = p1->val;
                p1 = p1->next;
            }
            if (p2 != nullptr)
            {
                b = p2->val;
                p2 = p2->next;
            }
            int sums = a + b + step;
            //进位已加，则清零
            step = 0;
            //两个数之和大于9，则进1位
            if (sums > 9)
            {
                step = 1;
                sums -= 10;
            }
            newNode = new ListNode(sums);
            newNode->next = lastNode;
            lastNode = newNode;
        }
        //最后如果有进位，则需要插入一个节点
        if(step==1){
            newNode = new ListNode(1);
            newNode->next = lastNode;
        }
        return newNode;
    }
};
int main()
{
    Solution pp;
    int arr1[] = {9, 3, 7};
    int len1 = sizeof(arr1) / sizeof(int);
    ListNode *node1 = pp.insertNode(arr1, len1);
    pp.disp(node1);

    int arr2[] = {6,3};
    int len2 = sizeof(arr2)/sizeof(int);
    ListNode *node2 = pp.insertNode(arr2,len2);
    ListNode *ans = pp.addInList(node1,node2);
    pp.disp(ans);
}