
## 如何在 Chrome 上查看 element 的事件

1. F12 选中 element，查看右边 Tab 'EventListeners'
2. 如果不要查看所有的，可以将 Ancestors All 的勾选取消掉
3. 确认勾选了 Framework Listeners
4. 点击下面的事件列表，右键 handler: f () ， 选择 Show Function Definition 即可定位到你所要的事件函数

另外可 F12 选择元素后，进入 Console 输入 getEventListeners($0) 返回该元素的事件对象