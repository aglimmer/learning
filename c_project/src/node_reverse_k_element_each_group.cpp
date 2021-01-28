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
    ListNode *reverseListNode(ListNode *head){
        if(head==nullptr || head->next==nullptr){
            return head;
        }
        ListNode *p1 = head;
        ListNode *p2 = p1->next;
        while(p2!=nullptr){
            ListNode *tmp = p2->next;
            p2->next = p1;
            p1 = p2;
            p2 = tmp;
        }
        head->next = nullptr;
        return p1;
    }
    
    ListNode* reverseKGroup(ListNode* head, int k) {
        if(k<2){
            return head;
        }
        int cnt = 0;
        ListNode dumy(0);
        dumy.next = head;
        ListNode *first = &dumy;
        while(first->next != nullptr){
            ListNode *second = first->next;
            cnt +=2;
            while(cnt%k!=0 && second->next!=nullptr){
                second = second->next;
                cnt +=1;
            }
            if(cnt%k==0 && second->next!=nullptr){
                ListNode *nextNode = second->next->next;
                second->next->next =  nullptr;
                ListNode *tailer = first->next;
                ListNode *ans = reverseListNode(first->next);
                first->next = ans;
                tailer->next = nextNode;
                first = tailer;
                continue;
            }
            first = second;
        }
        return dumy.next;
       
    }
 
};
int main()
{
   Solution pp;
   int arr[]={1,2,3,4,5,6,7,8,9,10,11};
   int len  = sizeof(arr)/sizeof(int);
    ListNode *curr = pp.insertListNode(arr,len);
    pp.disp(curr);
    int k = 4;
    curr = pp.reverseKGroup(curr,k);
    pp.disp(curr);
}