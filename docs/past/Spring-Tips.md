
## 对于 @Service 类中的 @Transaction / @Cacheable 方法内部调用失效的问题

可以使用继承 BeanFactoryAware 接口，并实现接口方法，通过 BeanFactory 将自身的 Spring 的代理类注入到类中，所有内部调用直接调用代理类即可