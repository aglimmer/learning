#include <iostream>
using namespace std;
struct ListNode
{
    int val;
    ListNode *next;
    ListNode(int x) : val(x), next(NULL) {}
};
class Solution
{
public:
    ListNode *detectCycle(ListNode *head)
    {
        ListNode *fast = head;
        ListNode *slow = head;
        while (fast != nullptr && fast->next != nullptr)
        {
            fast = fast->next->next;
            slow = slow->next;
            if (fast == slow)
            {
                ListNode *curr = head;
                while (curr != fast)
                {
                    curr = curr->next;
                    fast = curr->next;
                }
                return curr;
            }
        }
    }
    int getLength(ListNode *head)
    {
        int ans = 0;
        ListNode *curr = head;
        while (curr != nullptr)
        {
            curr = curr->next;
            ans += 1;
        }
        return ans;
    }
    ListNode *insertListNode(int arr[],int n){
        ListNode dumy(0);
        ListNode *curr = &dumy;
        for(int i=0;i<n;i++){
             curr->next = new ListNode(arr[i]);
             curr = curr->next;
        }
        return dumy.next;
    }
    void disp(ListNode *head){
        ListNode *curr = head;
        while(curr!=nullptr){
            cout<<curr->val<<"->";
            curr = curr->next;
        }
        cout<<endl;
    }
    ListNode *removeNthFromEnd(ListNode *head, int n)
    {
        int len = getLength(head);
        // cout<<"len="<<len<<",n="<<n<<endl;
        if (n <=0 || n > len)
        {
            return head;
        }
        ListNode dumy(0);
        dumy.next = head;
        ListNode *curr = &dumy;
        int k = len - n + 1;
        int cnt = 1;
        cout<<"k="<<k<<endl;
        ListNode *tmp = nullptr;
        while (cnt<k && curr != nullptr)
        {
            curr = curr->next;
            cnt += 1;
        }
        tmp = curr->next;
        curr->next = tmp->next;
        cout<<"tmp="<<tmp->val<<endl;
        // delete tmp;
        return dumy.next;
    }
};
int main()
{
   Solution pp;
   int arr[]={1,2,3,4,5,6};
   int len  = sizeof(arr)/sizeof(int);
   ListNode *curr = pp.insertListNode(arr,len);
   pp.disp(curr);
   curr = pp.removeNthFromEnd(curr,-1);
   pp.disp(curr); 
}