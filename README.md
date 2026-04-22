# Vehicle Cloud — 车联网云平台

车联网云平台微服务演示项目，采用 Spring Boot 多模块架构 + React 前端，提供车辆管理与用户管理功能。

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 2.7.18 |
| JDK | Java | 11 |
| 数据库 | H2（内存库） | — |
| ORM | Spring Data JPA | — |
| 参数校验 | javax.validation | — |
| 工具库 | Lombok / MapStruct | 1.18.36 / 1.5.5 |
| 前端框架 | React | 19 |
| UI 组件 | Ant Design | 6 |
| 构建工具 | Maven 3.8+ / Vite 8 | — |

## 项目结构

```
vehicle-cloud/
├── vehicle-common/          # 公共模块：统一响应、异常体系
├── user-service/            # 用户管理服务（端口 8081）
├── vehicle-service/         # 车辆管理服务（端口 8080）
└── vehicle-ui/              # React 前端（端口 5173）
```

### 模块说明

**vehicle-common** — 公共基础设施
- `ApiResponse<T>`：统一 API 响应封装（code / message / data / timestamp）
- `BusinessException`：业务异常类
- `GlobalExceptionHandler`：全局异常处理器（参数校验、业务异常、未知异常）

**user-service** — 用户管理
- CRUD 操作，含参数校验（姓名非空、手机号 11 位格式校验）
- H2 内存库，启动自动初始化 5 条用户数据

**vehicle-service** — 车辆管理
- CRUD 操作，含 VIN 码 17 位校验
- H2 内存库，启动自动初始化 30 条车辆数据（AITO M5 / M7 / M9）

**vehicle-ui** — 前端应用
- React + Ant Design 单页应用
- Vite 开发服务器，代理 API 请求到后端服务

## 快速启动

### 环境要求

- JDK 11+
- Maven 3.8+
- Node.js 18+

### 1. 构建后端

```bash
mvn clean install
```

### 2. 启动后端服务

分别启动两个服务（各开一个终端）：

```bash
# 车辆服务（端口 8080）
cd vehicle-service
mvn spring-boot:run

# 用户服务（端口 8081）
cd user-service
mvn spring-boot:run
```

### 3. 启动前端

```bash
cd vehicle-ui
npm install
npm run dev
```

前端默认运行在 http://localhost:5173，API 请求会通过 Vite 代理转发到后端服务。

## API 接口

### 用户服务（8081）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/v1/users` | 创建用户 |
| GET | `/api/v1/users` | 查询用户列表 |
| GET | `/api/v1/users/{id}` | 按 ID 查询用户 |
| DELETE | `/api/v1/users/{id}` | 删除用户 |

**请求体示例（创建用户）：**
```json
{
  "name": "张三",
  "phone": "13800138000"
}
```

### 车辆服务（8080）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/v1/vehicles` | 创建车辆 |
| GET | `/api/v1/vehicles` | 查询车辆列表 |
| GET | `/api/v1/vehicles/{id}` | 按 ID 查询车辆 |
| DELETE | `/api/v1/vehicles/{id}` | 删除车辆 |

**请求体示例（创建车辆）：**
```json
{
  "vin": "LWVBD1A56NR100099",
  "model": "AITO M5",
  "ownerUserId": 1
}
```

### 统一响应格式

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": "2025-01-15T08:30:00Z"
}
```

## H2 控制台

两个服务均开启了 H2 Web 控制台，启动后可通过浏览器访问：

- 车辆服务：http://localhost:8080/h2-console（JDBC URL: `jdbc:h2:mem:vehicledb`）
- 用户服务：http://localhost:8081/h2-console（JDBC URL: `jdbc:h2:mem:userdb`）

用户名：`sa`，密码：空

## 初始化数据

| 服务 | 数据量 | 说明 |
|------|--------|------|
| user-service | 5 条 | 小王、小李、老张、赵敏、陈博 |
| vehicle-service | 30 条 | AITO M5 × 10、AITO M7 × 10、AITO M9 × 10 |

车辆均匀分配给 5 个用户，每人 6 辆。
