# Orange

Orange是一个RPC协议的实现,下面是项目的规划.

项目规划一:
    阶段1: 能调用方法,传字符传参数. ===>> 已完成
    阶段2: 能返回方法调用返回值. ===>> 已完成,优化netty代码.深入到 Netty 中.
    阶段3: 能传多个字符串参数.
    阶段4: 支持业务对象 POJO.

项目规划二:
    阶段1: Orange消息协议.
    阶段2: 扩展Spring标签,实现和Spring的无缝对接.
    阶段3: 支持Orange云,客户端实现负载均衡.


暂时的规划是这样子的, 我们的目标是为开源提供一个强大的RPC工具.欢迎大家来一起讨论和贡献代码.