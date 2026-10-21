# 基于 RAG 的宠物医院管理系统

宠物医院在线业务系统，含管理后台、服务端 API 和用户端 App 三端，AI 问诊通过 Rasa Webhook 接入。

## 目录结构

```
pet-hospital-system/
├── admin/    # 管理后台（Vue 3 + Vite + Element Plus）
├── server/   # 服务端（Spring Boot 3.5 + MyBatis + Druid + JWT）
└── app/      # 用户端 App（uni-app + Vue 3，H5 / 小程序 / App 多端）
```

## 环境要求

| 组件 | 版本 |
| --- | --- |
| JDK | 17 |
| Maven | 3.9.x（本项目实测 3.9.11） |
| MySQL | 8.x |
| Redis | 5.x+ |
| Node.js | 18+（`admin/`、`app/` 构建） |
| HBuilderX | App 端真机调试时需要 |

## 快速开始

### 1. 服务端

```bash
cd server
# 先建库并导入：CREATE DATABASE `pet-hospital`; 然后导入 sql/pet-hospital.sql
mvn spring-boot:run
```

服务默认 `http://localhost:8080`。数据库、Redis 连接参数在
`server/src/main/resources/application-druid.yml` 与 `application.yml`。

**凭据必须由环境变量提供，仓库内不再保留可用默认值**（缺失时启动即失败，避免带着公开密钥上线）：

| 环境变量 | 是否必填 | 用途 |
| --- | --- | --- |
| `MYSQL_PASSWORD` | 必填 | MySQL 口令 |
| `TOKEN_SECRET` | 必填 | JWT 签名密钥，HS512 建议 ≥ 64 字符随机串 |
| `MYSQL_USERNAME` | 可选 | 默认 `root` |
| `DRUID_MONITOR_ENABLED` | 可选 | 默认 `false`，即 Druid 监控台关闭 |
| `DRUID_MONITOR_USER` / `DRUID_MONITOR_PASSWORD` | 开启监控台时必填 | Druid 监控台账号，默认值是占位 `changeme` |

PowerShell 示例：

```powershell
$env:MYSQL_PASSWORD = "<你的本地口令>"
$env:TOKEN_SECRET   = "<64 位以上随机串>"
mvn spring-boot:run
```

也可以把这些写进 `server/src/main/resources/application-local.yml`（已被 `.gitignore` 忽略），
不要把真实口令提交到仓库。

### 2. 管理后台

```bash
cd admin
npm install        # 或 yarn --registry=https://registry.npmjs.org
npm run dev        # 开发，http://localhost:90
npm run build:prod # 生产构建
```

开发代理指向 `http://localhost:8080`（见 `admin/vite.config.js`）。

### 3. 用户端 App

```bash
cd app
npm install
npm run dev        # H5 预览
```

接口地址在 `app/config.js` 的 `baseUrl`，默认 `http://localhost:8080`。
小程序 / App 端请用 HBuilderX 打开 `app/` 后发行。

## 默认账号

管理后台演示账号 `admin / admin123`（`login.vue` 中预填，数据库内为对应的 BCrypt 演示哈希）。

## 数据与清洗说明

- `server/sql/pet-hospital.sql` 为演示种子数据（约 130 条 INSERT），其中联系电话与邮箱已替换为 `13800000000` / `demo@example.com`。
- 配置文件里的凭据改为**必填环境变量**，仓库内不含任何可用的真实口令。
- `admin/src/utils/jsencrypt.js` 中的 RSA 密钥对是 RuoYi 上游模板自带的演示密钥，与后端公钥配对，**不是真实凭据，请勿用于生产**。
- 未纳入本仓库：开发工具链（HBuilderX、Maven 发行包、Redis 二进制）与前端依赖产物。

## 已做的安全加固（含行为变更）

| 改动 | 位置 | 对使用的影响 |
| --- | --- | --- |
| AI 聊天接口不再整棵子树匿名放行 | `SecurityConfig.java` | **App 端 AI 问诊需登录后才能用**，游客模式取消 |
| Rasa 会话标识改由登录用户推导，不再接受请求体里的 `sender` / `userId` | `ChatController.java`、`ChatRasaController.java` | 前端不再传这两个字段；跨用户读写会话的问题消除 |
| 移除 `@CrossOrigin(origins = "*")` | `ChatController.java` | 跨域走 `ResourcesConfig` 的统一配置 |
| Druid 监控台默认关闭，且 `/druid/**` 需应用登录 | `application-druid.yml`、`SecurityConfig.java` | 需要监控时设 `DRUID_MONITOR_ENABLED=true` 并覆盖账号 |
| 关闭 Druid `multi-statement-allow` | `application-druid.yml` | 阻止堆叠 SQL，正常单语句业务不受影响 |
| 上传白名单去掉 `html` / `htm` | `MimeTypeUtils.java` | 不再能上传网页文件；`/profile/**` 匿名可读导致的存储型 XSS 面消除 |
| Rasa 调用统一使用带超时（连接 5s / 读取 15s）的 `RestTemplate` Bean | `RasaServiceImpl.java`、`ChatRasaController.java` | Rasa 未启动时快速失败，不再拖住请求线程 |
| 消息长度上限 1000 字符 | 两个聊天控制器 | 超长输入返回「消息过长，请分段发送」 |
| App 端聊天改走统一请求封装（自动带 token） | `app/pages/api/hospital/chat.js` | 删除了未被调用且指向不存在端点的 `sseChat` |

后端改动已通过离线 `mvn -o compile`（232 个源文件，BUILD SUCCESS）验证；前端仅改动该 API 模块，未做真机运行验证。

## 剩余风险（本次未处理）

1. **依赖版本未升级**：Spring Boot 3.5.0、Druid 1.2.23、fastjson2 2.0.53、Velocity 2.3、MyBatis 3.0.3 均非最新版，fastjson2 与 Velocity 历史 CVE 较多。升级需要联网拉包并回归测试，本次刻意没动，以免给你一个编译不过的仓库。
2. **无频率限制**：AI 接口现在需登录，但没有 per-user 限流，登录用户仍可高频调用 Rasa。
3. **`/profile/**` 仍匿名可读**：上传目录里的文件对任何拿到 URL 的人可见（头像等需要公开展示）。若存放敏感附件，需要改为签名 URL 或鉴权。
4. **`${params.dataScope}` 依赖注解约束**：`SysUser/SysRole/SysDept` 三处 `${params.dataScope}` 已确认调用方都带 `@DataScope`，安全；但后续新增复用这些 mapper 而漏加注解的接口会变成注入口。
5. **代码生成模块**（`GenController` + Velocity）权限依赖管理员身份，本身是高价值目标，生产环境建议关闭或严格限权。
6. **无测试、无 CI**；`app/package.json` 依赖是 uni-app alpha 通道版本号，可复现构建较弱。
7. **许可证不一致**：`admin/package.json` 写 MIT，代码实际源自 Apache-2.0 的 RuoYi，见下文。

## 尚未包含的部分

AI 问诊依赖 Rasa / RAG 服务，地址由 `application.yml` 的 `rasa.url` 配置（默认 `http://localhost:5005/webhooks/rest/webhook`）。
该服务不在本仓库内，未启动时 AI 聊天接口返回「服务繁忙」，其余功能不受影响。

## 致谢与来源

后端与后台基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 脚手架改造；
用户端沿用 uni-app 模板（`app/package.json` 保留了上游 Gitee 仓库地址）。
包名 `com.huacai`、页面标题「基于RAG的宠物医院管理系统」为项目原始标识。

## License

`admin/package.json` 声明为 MIT；本仓库代码含 RuoYi 上游授权范围，生产使用前请自行核对各上游许可证。
