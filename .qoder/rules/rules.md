---
trigger: always_on
---
# vehicle-cloud 项目规范（Project Rules）                 
                                                                                                                                                                                                                                                     
  > 这是团队的**项目契约**——所有 Qoder 生成或修改的代码都必须遵守。                                                                                                                                                                                  
  > 规则会随项目演进持续丰富，**不允许随意删除**。                                                                                                                                                                                                   
                                                                                                                                                                                                                                                     
  ---                                                       

  ## 一、技术栈锁定（硬红线）                                                                                                                                                                                                                        
   
  - Spring Boot **2.7.18**，禁止升级到 3.x / 4.x                                                                                                                                                                                                     
  - Java **11**，禁止升级到 17 / 21                         
  - Maven 3.8+                                                                                                                                                                                                                                       
  - 命名空间用 **javax.\***，禁止 jakarta.*
  - 演示数据库：**H2 内存库**（单测、启动皆用）                                                                                                                                                                                                      
                                                                                                                                                                                                                                                     
  ## 二、Java 11 语法约束                                                                                                                                                                                                                            
                                                                                                                                                                                                                                                     
  - 禁用 `record` 类型 → 用 Lombok `@Data` / `@Value` 替代                                                                                                                                                                                           
  - 禁用 text block（`"""..."""`）→ 用字符串拼接
  - 禁用 switch expression（`case -> ...`）→ 用传统 switch-case                                                                                                                                                                                      
  - 禁用 sealed class、pattern matching for instanceof      
  - **允许** `var`（Java 10+ 特性）                                                                                                                                                                                                                  
                                                                                                                                                                                                                                                     
  ## 三、包结构                                                                                                                                                                                                                                      
                                                                                                                                                                                                                                                     
  - 根包：`com.wenjie.cloud`                                
  - 子模块包：`com.wenjie.cloud.<module-name>`
  - 每个模块下标准目录：                                                                                                                                                                                                                             
    - `controller/`
    - `service/`（接口）                                                                                                                                                                                                                             
    - `service/impl/`（实现）                               
    - `repository/`
    - `entity/`                                                                                                                                                                                                                                      
    - `dto/`
    - `config/`                                                                                                                                                                                                                                      
    - `exception/`                                          

  ## 四、命名规范

  - Controller 类：以 `Controller` 结尾                                                                                                                                                                                                              
  - Service：接口 + Impl 实现**分离**（`UserService` + `UserServiceImpl`）
  - Repository 类：以 `Repository` 结尾                                                                                                                                                                                                              
  - Entity 放 `entity/`，DTO 放 `dto/`                                                                                                                                                                                                               
                                                                                                                                                                                                                                                     
  ## 五、分层纪律                                                                                                                                                                                                                                    
                                                                                                                                                                                                                                                     
  - Controller **只做**：参数校验（`@Valid`）+ 响应封装     
  - 业务逻辑**全部**在 Service 实现类
  - 数据访问**全部**通过 Repository                                                                                                                                                                                                                  
  - **禁止** Controller 直接操作 Repository
  - **禁止** Service 之间循环依赖                                                                                                                                                                                                                    
                                                                                                                                                                                                                                                     
  ## 六、响应规范
                                                                                                                                                                                                                                                     
  - 所有 HTTP 响应**统一包装**为 `ApiResponse<T>`（common 模块提供）
  - 成功：`return ApiResponse.success(data)`
  - 失败：`return ApiResponse.error(code, message)`                                                                                                                                                                                                  
   
  ## 七、异常处理                                                                                                                                                                                                                                    
                                                            
  - 业务异常抛 `BusinessException`（common 模块提供）                                                                                                                                                                                                
  - **禁止**裸抛 `RuntimeException` / `Exception`
  - catch 必须有明确处理或上抛理由，**禁止空 catch**                                                                                                                                                                                                 
  - `finally` 必须释放所有 IO / Lock 资源                                                                                                                                                                                                            
   
  ## 八、日志规范                                                                                                                                                                                                                                    
                                                            
  - 使用 SLF4J（Lombok 的 `@Slf4j` 注解）                                                                                                                                                                                                            
  - **禁止** `System.out.println`
  - 错误日志**必须**包含业务上下文：                                                                                                                                                                                                                 
    `log.error("创建车辆失败, vin={}, reason={}", vin, e.getMessage())`                                                                                                                                                                              
   
  ## 九、参数校验                                                                                                                                                                                                                                    
                                                            
  - 使用 `javax.validation.constraints`（**禁止** `jakarta.validation`）                                                                                                                                                                             
  - Controller 入参用 `@Valid`
  - 校验失败由 `GlobalExceptionHandler` 统一处理                                                                                                                                                                                                     
                                                                                                                                                                                                                                                     
  ## 十、JPA 规范
                                                                                                                                                                                                                                                     
  - Entity 用 `@Entity` + `@Table` **显式**声明表名（蛇形）                                                                                                                                                                                          
  - 字段用 `@Column` **显式**声明列名（蛇形）
  - 事务用 `@Transactional` **显式**声明                                                                                                                                                                                                             
  - 分页：用 `Pageable` 参数 + 返回 `Page<T>`（**需要分页时才加**）
  - 避免 N+1：必要时用 `@EntityGraph` 或 JOIN FETCH                                                                                                                                                                                                  
                                                                                                                                                                                                                                                     
  ## 十一、测试规范                                                                                                                                                                                                                                  
                                                                                                                                                                                                                                                     
  - JUnit 5 + Mockito                                       
  - 命名：`methodName_condition_expectedResult`
    - 示例：`createVehicle_withInvalidVin_shouldThrowBusinessException`                                                                                                                                                                              
  - 覆盖率目标：**≥ 80%**
  - 所有外部依赖必须 Mock（Redis / MQ / 第三方 HTTP）                                                                                                                                                                                                
                                                                                                                                                                                                                                                     
  ## 十二、车联网业务术语（生成代码时用标准命名）                                                                                                                                                                                                    
                                                                                                                                                                                                                                                     
  | 术语 | 含义 | 标准字段/类名 |                                                                                                                                                                                                                    
  |---|---|---|
  | VIN | 车辆识别码（17 位） | 字段名 `vin` |                                                                                                                                                                                                       
  | TBox | 车载通信终端（Telematics Box） | `TBoxMessage` / `TBoxClient` |                                                                                                                                                                           
  | OTA | 整车远程升级 | `OtaTask` / `OtaService` |                                                                                                                                                                                                  
  | Owner | 车主（全部权限） | `Owner` / `OwnerId`（不用 User 代替） |                                                                                                                                                                               
  | FamilyMember | 车主授权的家人 | `FamilyMember` |                                                                                                                                                                                                 
  | StatusReport | 状态上报（TBox 周期推送） | `VehicleStatus` / `StatusReport` |                                                                                                                                                                    
  | Command | 控车指令 | `Command` / `CommandLog` |                                                                                                                                                                                                  
                                                                                                                                                                                                                                                     
  ## 十三、演进约定                                                                                                                                                                                                                                  
                                                            
  - 这份 Rules **是活的契约**，会随项目演进持续丰富                                                                                                                                                                                                  
  - 现有条款**不允许随意删除**                              
  - 新增条款应在团队 Code Review 后合入                                                                                                                                                                                                              
  - 每次变更用 git 追踪，变更理由写在 commit message 里