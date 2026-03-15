#include <iostream>
#include <stack>
using namespace std;

// Bài 1: Đếm số lần xuất hiện của x
int countX(int a[], int n, int x) {
    int count = 0;
    for(int i = 0; i < n; i++) {
        if(a[i] == x) count++;
    }
    return count;
}

// Bài 2: Tìm giá trị lớn nhất
int findMax(int a[], int n) {
    int max = a[0];
    for(int i = 1; i < n; i++) {
        if(a[i] > max) max = a[i];
    }
    return max;
}

// Bài 3: Tìm số lớn thứ hai
int secondMax(int a[], int n) {
    int max1 = a[0];
    int max2 = -1000000;

    for(int i = 0; i < n; i++) {
        if(a[i] > max1) {
            max2 = max1;
            max1 = a[i];
        }
        else if(a[i] > max2 && a[i] != max1) {
            max2 = a[i];
        }
    }
    return max2;
}

// Bài 4: Kiểm tra Palindrome
bool isPalindrome(string s) {
    int left = 0;
    int right = s.length() - 1;

    while(left < right) {
        if(s[left] != s[right]) return false;
        left++;
        right--;
    }
    return true;
}

// Bài 5: Kiểm tra số nguyên tố
bool isPrime(int x) {
    if(x < 2) return false;

    for(int i = 2; i <= x/2; i++) {
        if(x % i == 0) return false;
    }
    return true;
}

int countPrime(int a[], int n) {
    int count = 0;
    for(int i = 0; i < n; i++) {
        if(isPrime(a[i])) count++;
    }
    return count;
}

// Bài 6: Kiểm tra phần tử duy nhất
bool isUnique(int a[], int n) {
    for(int i = 0; i < n; i++) {
        for(int j = i + 1; j < n; j++) {
            if(a[i] == a[j]) return false;
        }
    }
    return true;
}

// Bài 7: Kiểm tra ngoặc hợp lệ
bool isValid(string s) {
    stack<char> st;

    for(char c : s) {
        if(c == '(' || c == '{' || c == '[') {
            st.push(c);
        }
        else {
            if(st.empty()) return false;

            char top = st.top();
            st.pop();

            if((c == ')' && top != '(') ||
               (c == '}' && top != '{') ||
               (c == ']' && top != '[')) {
                return false;
            }
        }
    }
    return st.empty();
}

// Bài 8: Tổng đệ quy
int sumRecursive(int n) {
    if(n == 0) return 0;
    return n + sumRecursive(n - 1);
}

// Bài 9: Linear Search
int linearSearch(int a[], int n, int x) {
    for(int i = 0; i < n; i++) {
        if(a[i] == x) return i;
    }
    return -1;
}

int main() {

    int n, x;
    int a[1000];

    cout << "Nhap so phan tu: ";
    cin >> n;

    cout << "Nhap mang: ";
    for(int i = 0; i < n; i++) {
        cin >> a[i];
    }

    // Bài 1
    cout << "Nhap x de dem: ";
    cin >> x;
    cout << "So lan x xuat hien: " << countX(a, n, x) << endl;

    // Bài 2
    cout << "Max: " << findMax(a, n) << endl;

    // Bài 3
    cout << "Second Max: " << secondMax(a, n) << endl;

    // Bài 4
    string s;
    cout << "Nhap chuoi: ";
    cin >> s;

    if(isPalindrome(s))
        cout << "Palindrome: YES" << endl;
    else
        cout << "Palindrome: NO" << endl;

    // Bài 5
    cout << "So luong so nguyen to: " << countPrime(a, n) << endl;

    // Bài 6
    if(isUnique(a, n))
        cout << "Unique: true" << endl;
    else
        cout << "Unique: false" << endl;

    // Bài 7
    string bracket;
    cout << "Nhap chuoi ngoac: ";
    cin >> bracket;

    if(isValid(bracket))
        cout << "Valid bracket: YES" << endl;
    else
        cout << "Valid bracket: NO" << endl;

    // Bài 8
    int k;
    cout << "Nhap n tinh tong de quy: ";
    cin >> k;
    cout << "Tong: " << sumRecursive(k) << endl;

    // Bài 9
    cout << "Nhap x de tim: ";
    cin >> x;
    cout << "Vi tri: " << linearSearch(a, n, x) << endl;

    return 0;
}