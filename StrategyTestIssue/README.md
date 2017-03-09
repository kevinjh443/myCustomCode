# StrategyTestIssue
in strategy test, have a issue about interface return parameters
</br></br>
策略模式代码（JAVA）示例。</br>
# Issue
其中有个问题：</br>
1. 接口中两个接口的输入参数不一样：
```java
public <E> E loadResult(ArrayList<String> fileList);
public <E> E loadResult3(String fileList);
```
2. 实现中为什么输入参数为ArrayList的函数报错呢？
```java
@Override
public String loadResult(ArrayList<String> fileList) {// have a issue here, why?
// TODO Auto-generated method stub
return "xxxx";
}

@Override
public String loadResult3(String fileList) {
// TODO Auto-generated method stub
return null;
}
```

