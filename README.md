# mq-ws-shiro
## 依赖使用
- 数据库与连接池：mysql、druid
- 持久层框架：mybatis-plus
- 分页工具：pagehelper
- 缓存与连接池：Redis、Lettuce
- 权限认证：Shiro
- 消息通知：RabbitMQ、WebSocket
- 接口文档：Swagger2
## 功能介绍
- 集成renren-security中的shiro模块实现登录认证和权限校验
- 添加shiro-redis依赖，使用redis管理用户缓存信息,自定义缓存前缀
- 集成rabbitmq和websocket结合实现消息通知与一对一聊天功能`stomp和sockjs`
- 使用lombok和mybatis-plus简化代码
