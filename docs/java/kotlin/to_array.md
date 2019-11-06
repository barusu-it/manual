
### toArray Usage

```
this.resolver.getResources(this.pathPattern).toList().stream()
    .filter { it.url.path.endsWith(StringUtils.SLASH) }
    .map { DEFAULT_PATH + StringUtils.SLASH + FilenameUtils.getName(it.url.path.removeSuffix(StringUtils.SLASH)) }
    .toArray<String> { length -> arrayOfNulls(length)} // kotlin  
//  .collect(Collectors.toList()).toTypedArray() // java
```