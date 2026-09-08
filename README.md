# OpsFlow

[![OpsFlow CI](https://github.com/L0op47/opsFlow/actions/workflows/ci.yml/badge.svg)](https://github.com/L0op47/opsFlow/actions/workflows/ci.yml)

OpsFlow 是一个面向企业内部场景的工单与资产管理后端，围绕工单流转、RBAC 权限控制、资产管理、SLA 超时检测和操作审计构建。

项目采用前后端分离的 REST API 设计，支持本地开发、独立 JAR 运行以及 Docker Compose 一键启动。

## 核心功能

- 用户认证：注册、登录、退出登录、获取当前用户信息
- 登录状态：JWT 身份认证结合 Redis 登录会话校验
- RBAC 权限：用户、角色、权限及其关联关系管理，支持方法级权限控制
- 工单管理：创建、编辑、查询、接单、解决、关闭、取消和流转历史
- SLA 管理：计算工单截止时间、定时扫描并分页查询超时工单
- 资产管理：资产增改查、状态修改、使用人分配和详情缓存
- 组织管理：用户与部门的基础管理
- 操作审计：通过自定义注解和 AOP 记录关键写操作及执行结果
- 统一接口：统一响应结构、参数校验、分页响应和全局异常处理
- 工程化：Flyway 数据库迁移、OpenAPI 文档、Actuator 健康检查、GitHub Actions CI 和容器化部署

## 技术栈

| 类别 | 技术 |
| --- | --- |
| 基础框架 | Java 17、Spring Boot 3.5 |
| Web 与安全 | Spring MVC、Spring Validation、Spring Security、JWT |
| 数据访问 | MyBatis、PageHelper、MySQL 8 |
| 缓存与会话 | Redis、Spring Data Redis |
| 数据库迁移 | Flyway |
| 对象转换 | MapStruct |
| 审计日志 | Spring AOP |
| API 文档 | SpringDoc OpenAPI、Swagger UI |
| 监控 | Spring Boot Actuator |
| 构建与交付 | Maven Wrapper、GitHub Actions、Docker、Docker Compose |

## 业务设计亮点

### 工单状态流转

工单支持从创建到结束的完整业务链路：

```text
PENDING -> PROCESSING -> RESOLVED -> CLOSED
   \-----------> CANCELED <---------/
```

每次状态变更都会校验当前状态和操作者权限，并记录工单历史。接单等关键写操作使用事务保证工单状态与历史记录的一致性。

### 权限控制

接口首先通过 JWT 和 Redis 会话完成身份校验，再由 Spring Security 的方法级鉴权判断用户是否拥有对应权限，例如：

- `ticket:create`
- `ticket:accept`
- `ticket:resolve`
- `ticket:read:self`
- `asset:manage`
- `role:manage`

### Redis 应用

- 保存当前用户的有效登录令牌，实现退出登录后令牌失效
- 缓存资产详情，降低重复查询对数据库的访问
- 更新资产后主动删除或刷新对应缓存，保证缓存一致性

### SLA 与查询优化

系统根据工单优先级计算截止时间，并由定时任务扫描未结束的超时工单。针对超时查询建立 `(status, deadline_at)` 联合索引，减少全表扫描。

## 项目结构

```text
src/main/java/org/example/opsflow
├── auth            # 注册、登录和当前用户
├── security        # JWT 过滤器、异常处理和 Redis 会话
├── rbac            # 角色、权限及关联关系
├── user            # 用户管理
├── department      # 部门管理
├── ticket          # 工单、流转历史和 SLA
├── asset           # 资产管理与缓存
├── operationlog    # 操作日志注解、切面和查询
├── common          # 统一响应与全局异常
└── config          # Security、OpenAPI 等配置
```

数据库脚本位于 `src/main/resources/db/migration`，应用启动时由 Flyway 按版本自动执行。

## 快速启动

### 方式一：Docker Compose

环境要求：

- Docker Engine
- Docker Compose 插件

克隆项目并准备环境变量：

```bash
git clone https://github.com/L0op47/opsFlow.git
cd opsFlow
cp .env.example .env
```

修改 `.env` 中的密码和 JWT 密钥，然后启动全部服务：

```bash
docker compose up -d --build
```

检查容器和应用状态：

```bash
docker compose ps
curl http://localhost:8080/actuator/health
```

查看应用日志：

```bash
docker compose logs -f app
```

停止服务：

```bash
docker compose down
```

如需同时删除 MySQL 和 Redis 的持久化数据，可执行 `docker compose down -v`。该命令会删除本项目 Compose 创建的数据卷，请谨慎使用。

### 方式二：本地开发

环境要求：

- JDK 17
- MySQL 8
- Redis
- 已创建名为 `opsflow` 的数据库

设置必要环境变量：

```bash
export DB_PASSWORD='your_mysql_password'
export JWT_SECRET='replace_with_a_random_secret_at_least_32_characters'
```

通过 Maven Wrapper 启动：

```bash
./mvnw spring-boot:run
```

默认启用 `dev` 配置，连接本机 MySQL 和 Redis。

## 服务入口

| 地址 | 说明 |
| --- | --- |
| `http://localhost:8080/health` | 应用基础健康接口 |
| `http://localhost:8080/actuator/health` | Actuator 健康检查 |
| `http://localhost:8080/swagger-ui.html` | Swagger UI，仅 dev 环境启用 |
| `http://localhost:8080/v3/api-docs` | OpenAPI JSON，仅 dev 环境启用 |

Docker Compose 默认启用 `prod` 配置，因此不会开放 Swagger 和 OpenAPI 文档。

## 主要 API 模块

| 模块 | 基础路径 | 能力 |
| --- | --- | --- |
| 认证 | `/api/v1/auth` | 注册、登录、退出、当前用户及权限 |
| 工单 | `/api/v1/tickets` | 工单查询、流转、历史和超时查询 |
| 资产 | `/api/v1/assets` | 资产管理、状态和使用人分配 |
| 用户 | `/api/v1/users` | 用户查询、修改和组织关系 |
| 部门 | `/api/v1/departments` | 部门管理 |
| 角色 | `/api/v1/roles` | 角色管理与权限分配 |
| 权限 | `/api/v1/permissions` | 权限查询与管理 |
| 操作日志 | `/api/v1/operation-logs` | 审计日志分页与条件查询 |

除注册、登录和健康检查外，其余接口需要在请求头携带登录返回的 JWT：

```http
Authorization: Bearer <token>
```

项目不会提交默认管理员账号或明文凭据。首次启动后可注册普通用户；完整管理能力需要初始化相应的角色、权限和用户角色关系。

## 环境变量

| 变量 | 用途 | Compose 是否必填 |
| --- | --- | --- |
| `DB_PASSWORD` | OpsFlow 数据库用户密码 | 是 |
| `MYSQL_ROOT_PASSWORD` | MySQL root 密码 | 是 |
| `JWT_SECRET` | JWT 签名密钥，建议使用足够长的随机字符串 | 是 |
| `JWT_EXPIRATION` | JWT 有效期，单位毫秒 | 否 |
| `DB_URL` | 独立运行 prod 配置时的数据库地址 | Compose 已配置 |
| `DB_USERNAME` | 独立运行 prod 配置时的数据库用户名 | Compose 已配置 |
| `REDIS_HOST` | Redis 主机 | Compose 已配置 |
| `REDIS_PORT` | Redis 端口，默认 6379 | 否 |
| `REDIS_DATABASE` | Redis 数据库编号，默认 0 | 否 |
| `REDIS_PASSWORD` | Redis 密码 | 否 |

## 构建与 CI

本地打包：

```bash
./mvnw clean package
```

推送或提交 Pull Request 到 `main` 时，GitHub Actions 会在 JDK 17 环境中执行 Maven 编译和打包。目前 CI 使用 `-DskipTests`，自动化测试将在后续补充后接入流水线。

## 后续计划

- 补充 Service 单元测试和关键接口集成测试
- 在 CI 中启用自动化测试并保留构建产物
- 增强指标监控、日志检索和告警能力
- 根据部署环境补充 HTTPS、反向代理和持续部署
