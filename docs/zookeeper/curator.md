
### watcher not work when zk reconnection

```
@Override
public void process(WatchedEvent event) {
    if (event.getType == Event.EventType.None && event.getState() == Event.KeeperState.SyncConnected){
        framework.getChildren().usingWatcher(this).forPath(path);
...
```

https://stackoverflow.com/questions/25849911/apache-curator-loses-all-watches-after-reconnection 