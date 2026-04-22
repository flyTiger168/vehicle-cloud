# vehicle-status-service 实现方案（含前端 + 模拟上报）

## Context

车辆 TBox 周期上报状态数据（电量、位置、里程、车速、温度），云端后台负责接收、存储、查询。运营团队需要通过管理后台可视化监控车队状态。当前项目缺少状态管理能力，需要：
1. 新增后端 `vehicle-status-service` 模块
2. 前端 `vehicle-ui` 增加"车辆状态"页面 + 仪表盘集成状态统计
3. 前端提供"模拟上报"按钮，手动触发生成模拟数据调真实上报接口

---

## 一、后端 -- vehicle-status-service

### 1.1 模块概览

| 项 | 值 |
|---|---|
| 模块名 | `vehicle-status-service` |
| 端口 | **8083** |
| 错误码段 | **3000-3999** |
| 包名 | `com.wenjie.cloud.vehiclestatus` |
| 数据库 | H2 内存库 `jdbc:h2:mem:statusdb` |

### 1.2 后端文件清单

**修改文件（1 个）**
- `pom.xml`（根） -- `<modules>` 追加 `<module>vehicle-status-service</module>`

**新建文件（14 个）**
```
vehicle-status-service/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/wenjie/cloud/vehiclestatus/
    │   │   ├── VehicleStatusServiceApplication.java
    │   │   ├── controller/
    │   │   │   └── StatusReportController.java
    │   │   ├── dto/
    │   │   │   ├── StatusReportDTO.java       # 上报请求入参
    │   │   │   └── StatusReportVO.java        # 查询响应出参
    │   │   ├── entity/
    │   │   │   └── StatusReport.java
    │   │   ├── repository/
    │   │   │   └── StatusReportRepository.java
    │   │   └── service/
    │   │       ├── StatusReportService.java
    │   │       └── impl/
    │   │           └── StatusReportServiceImpl.java
    │   └── resources/
    │       ├── application.yml
    │       └── data.sql
    └── test/java/com/wenjie/cloud/vehiclestatus/
        ├── controller/
        │   └── StatusReportControllerTest.java
        └── service/impl/
            └── StatusReportServiceImplTest.java
```

### 1.3 Entity -- `StatusReport`

表名 `status_report`，`vin` + `report_time` 复合索引。

| Java 字段 | 列名 | 类型 | 约束 | 说明 |
|---|---|---|---|---|
| id | id | Long | PK, IDENTITY | 主键 |
| vin | vin | String(17) | NOT NULL | 车辆识别码 |
| batteryLevel | battery_level | Integer | NOT NULL | 电量 0-100% |
| latitude | latitude | Double | NOT NULL | 纬度 |
| longitude | longitude | Double | NOT NULL | 经度 |
| mileage | mileage | Double | NOT NULL | 总里程 km |
| speed | speed | Double | NOT NULL | 车速 km/h |
| temperature | temperature | Double | NOT NULL | 车外温度 |
| reportTime | report_time | Instant | NOT NULL | TBox 上报时间 (UTC) |
| createdAt | created_at | Instant | NOT NULL, immutable | 服务端入库时间 |

### 1.4 DTO / VO

**StatusReportDTO**（上报入参，带校验注解）

| 字段 | 类型 | 校验 |
|---|---|---|
| vin | String | `@NotBlank` + `@Size(min=17, max=17)` |
| batteryLevel | Integer | `@NotNull` + `@Min(0)` + `@Max(100)` |
| latitude | Double | `@NotNull` + `@DecimalMin("-90")` + `@DecimalMax("90")` |
| longitude | Double | `@NotNull` + `@DecimalMin("-180")` + `@DecimalMax("180")` |
| mileage | Double | `@NotNull` + `@DecimalMin("0")` |
| speed | Double | `@NotNull` + `@DecimalMin("0")` |
| temperature | Double | `@NotNull` |
| reportTime | Instant | `@NotNull` |

**StatusReportVO**（查询出参，无校验）：DTO 全部字段 + `id`(Long) + `createdAt`(Instant)

### 1.5 Repository

继承 `JpaRepository<StatusReport, Long>`：

| 方法 | 用途 |
|---|---|
| `Page<StatusReport> findByVinAndReportTimeBetween(...)` | 历史分页查询 |
| `Optional<StatusReport> findFirstByVinOrderByReportTimeDesc(String vin)` | 单车最新状态 |
| `@Query` 自定义：按 VIN 分组取每组最新一条 | 全部车辆最新状态（前端列表页用） |

全部车辆最新状态的 JPQL：
```java
@Query("SELECT sr FROM StatusReport sr WHERE sr.id IN "
     + "(SELECT MAX(sr2.id) FROM StatusReport sr2 GROUP BY sr2.vin)")
List<StatusReport> findLatestForAllVehicles();
```

### 1.6 Service

| 方法 | 说明 |
|---|---|
| `StatusReportVO report(StatusReportDTO dto)` | 状态上报 |
| `Page<StatusReportVO> getHistory(String vin, Instant start, Instant end, Pageable pageable)` | 历史查询（分页） |
| `StatusReportVO getLatestByVin(String vin)` | 单车最新状态 |
| `List<StatusReportVO> getLatestAll()` | **新增** -- 全部车辆最新状态列表 |

业务逻辑：
- **report**：校验 reportTime 不晚于当前时间 -> toEntity -> save -> toVO
- **getHistory**：校验 startTime <= endTime -> 分页查询 -> page.map(toVO)
- **getLatestByVin**：校验 VIN 17 位 -> TOP 1 -> 无数据抛 BusinessException(3003)
- **getLatestAll**：调用 `findLatestForAllVehicles()` -> stream.map(toVO)

### 1.7 Controller API

`@RestController` + `@RequestMapping("/api/v1/status-reports")`

| # | Method | URL | 入参 | 出参 | 说明 |
|---|---|---|---|---|---|
| 1 | POST | `/api/v1/status-reports` | `@Valid @RequestBody StatusReportDTO` | `ApiResponse<StatusReportVO>` | TBox 上报 |
| 2 | GET | `/api/v1/status-reports` | `vin, startTime, endTime, page, size` | `ApiResponse<Page<StatusReportVO>>` | 历史分页（默认 20 条，reportTime DESC） |
| 3 | GET | `/api/v1/status-reports/latest/{vin}` | `@PathVariable vin` | `ApiResponse<StatusReportVO>` | 单车最新 |
| 4 | GET | `/api/v1/status-reports/latest` | 无 | `ApiResponse<List<StatusReportVO>>` | **新增** -- 全部车辆最新状态 |

### 1.8 错误码

| 码 | 含义 | 场景 |
|---|---|---|
| 3001 | 上报时间不能晚于当前时间 | report() |
| 3002 | 查询起始时间不能晚于结束时间 | getHistory() |
| 3003 | 该车辆无状态数据 | getLatestByVin() |
| 3004 | VIN 格式不正确 | Service 层 VIN 校验 |

### 1.9 配置要点

`application.yml` 额外加：
```yaml
spring.jackson.serialization.write-dates-as-timestamps: false
```
使 Instant 序列化为 ISO-8601 字符串。

### 1.10 data.sql 演示数据

从已有 30 辆车中选 6 辆（M5/M7/M9 各 2 辆），每辆 5 条记录，共 30 条。
- 时间分布在 2025-06-01 ~ 2025-06-30
- 电量递减、里程递增、位置在成都附近小幅变动

### 1.11 单元测试

**StatusReportServiceImplTest**（Mockito）：

| 测试 | 场景 |
|---|---|
| report_withValidDTO_shouldSaveAndReturnVO | 正常上报 |
| report_withFutureReportTime_shouldThrowBusinessException | 未来时间 |
| getHistory_withValidParams_shouldReturnPageOfVO | 正常分页 |
| getHistory_withStartTimeAfterEndTime_shouldThrowBusinessException | 时间非法 |
| getLatestByVin_withExistingData_shouldReturnLatestVO | 有数据 |
| getLatestByVin_withNoData_shouldThrowBusinessException | 无数据 |
| getLatestAll_shouldReturnList | 全车最新 |

**StatusReportControllerTest**（@WebMvcTest + MockMvc）：

| 测试 | 场景 |
|---|---|
| report_withValidBody_shouldReturn200 | 合法上报 |
| report_withInvalidVin_shouldReturn400 | VIN 非 17 位 |
| report_withBatteryLevelOutOfRange_shouldReturn400 | 电量超范围 |
| getHistory_withValidParams_shouldReturn200 | 合法查询 |
| getLatest_withValidVin_shouldReturn200 | 有数据 |
| getLatestAll_shouldReturn200 | 全车最新 |

---

## 二、前端 -- vehicle-ui 改造

### 2.1 前端文件清单

**修改文件（3 个）**

| 文件 | 改动 |
|---|---|
| `vite.config.js` | 新增代理 `/api/v1/status-reports` -> `http://localhost:8083` |
| `src/App.jsx` | 新增路由 `/status` + 侧边栏菜单项"车辆状态" |
| `src/pages/Dashboard.jsx` | 集成状态统计卡片（上报总数、平均电量、最近上报车辆数） |

**新建文件（2 个）**

| 文件 | 说明 |
|---|---|
| `src/api/statusApi.js` | 状态相关 API 封装 |
| `src/pages/VehicleStatus.jsx` | 车辆状态页面（最新状态表格 + 模拟上报） |

### 2.2 statusApi.js

```javascript
// API 函数
getLatestAll()              // GET /api/v1/status-reports/latest
getLatestByVin(vin)         // GET /api/v1/status-reports/latest/{vin}
getStatusHistory(params)    // GET /api/v1/status-reports?vin=...&startTime=...&endTime=...
reportStatus(data)          // POST /api/v1/status-reports
```

### 2.3 VehicleStatus.jsx -- 车辆状态页面

**页面布局**：
1. **顶部操作栏**：VIN 搜索框 + 刷新按钮 + "模拟上报"按钮
2. **状态表格**：调用 `getLatestAll()` 展示所有车辆最新状态

**表格列定义**：

| 列 | 字段 | 渲染 |
|---|---|---|
| VIN | vin | 文本 |
| 电量 | batteryLevel | Ant Design `Progress` 组件，颜色按阈值（>60 绿，30-60 橙，<30 红） |
| 位置 | latitude + longitude | `30.57, 104.07` 格式 |
| 里程 | mileage | `12,345.6 km` |
| 车速 | speed | `60.5 km/h`，0 时显示 Tag "静止" |
| 温度 | temperature | `28.3 ℃` |
| 上报时间 | reportTime | 格式化为本地时间 |

**模拟上报功能**：
- 点击"模拟上报"按钮弹出 Modal
- Modal 内容：从 vehicle-service 加载车辆列表的 Select 下拉框（选择 VIN）
- 点击"生成并上报"：前端生成一条随机但合理的模拟数据，调用 `POST /api/v1/status-reports`
- 随机数据规则：
  - batteryLevel: 20~95 随机
  - latitude: 30.52~30.62（成都范围）
  - longitude: 104.02~104.12
  - mileage: 10000~50000 随机
  - speed: 0/40/60/80/100 中随机选
  - temperature: 18~35
  - reportTime: 当前时间 `new Date().toISOString()`
- 上报成功后自动刷新表格，显示 message.success

### 2.4 Dashboard.jsx -- 仪表盘集成

在现有统计卡片行下方新增一行状态统计卡片：

| 卡片 | 数据来源 | 说明 |
|---|---|---|
| 上报记录总数 | `getLatestAll()` 返回列表的 length | 有多少车上报过数据 |
| 车队平均电量 | 对列表 batteryLevel 求平均值 | 百分比显示 |
| 低电量车辆 | batteryLevel < 30 的数量 | 红色高亮，运营关注 |

### 2.5 vite.config.js 代理新增

```javascript
'/api/v1/status-reports': {
  target: 'http://localhost:8083',
  changeOrigin: true,
},
```

### 2.6 App.jsx 路由/菜单新增

- 路由：`<Route path="/status" element={<VehicleStatus />} />`
- 菜单项：图标用 `<ThunderboltOutlined />`（或 `<DashboardOutlined />`），标签 "车辆状态"

---

## 三、实施顺序

### Phase 1: 后端
1. 修改根 pom.xml 添加模块声明
2. 创建 vehicle-status-service/pom.xml
3. 创建 application.yml + data.sql
4. 创建 Entity -> DTO/VO -> Repository -> Service -> ServiceImpl -> Controller -> 启动类
5. 创建 ServiceImplTest + ControllerTest
6. `mvn clean install` 全量构建验证

### Phase 2: 前端
7. 修改 vite.config.js 添加代理
8. 新建 src/api/statusApi.js
9. 新建 src/pages/VehicleStatus.jsx（含模拟上报 Modal）
10. 修改 src/App.jsx 添加路由和菜单
11. 修改 src/pages/Dashboard.jsx 集成状态统计
12. `npm run build` 验证前端构建

---

## 四、验证方式

1. **后端编译**：`mvn clean install` 全量构建通过
2. **后端测试**：`mvn test -pl vehicle-status-service` 全部通过
3. **启动后端**：启动 vehicle-status-service (8083)，curl 测试 4 个 API
4. **前端构建**：`npm run build` 通过
5. **端到端验证**：
   - 启动 vehicle-service (8080) + vehicle-status-service (8083) + 前端 (5173)
   - 打开浏览器访问管理后台
   - "车辆状态"页面显示演示数据
   - 点击"模拟上报" -> 选择 VIN -> 生成数据 -> 表格刷新看到新记录
   - 仪表盘显示状态统计卡片
