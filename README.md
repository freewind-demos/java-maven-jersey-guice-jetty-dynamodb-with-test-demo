Java Maven Jersey Guice Jetty Dynamodb with Test Demo
====================================

jersey内置了依赖框架hk2，但不好用，当找不到依赖时，没有信息提示，所以最后还是用了Guice

Server之所以用了Grizzly（而不是jetty），是因为 LocalDynamoDB 里使用了旧版本的jetty，依赖处理起来很麻烦

https://mkyong.com/webservices/jax-rs/jersey-and-hk2-dependency-injection-auto-scanning/

```
mvn clean test
```