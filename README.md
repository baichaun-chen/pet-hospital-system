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

凭据支持环境变量覆盖，不设置则使用仓库内的本地开发默认值：

| 环境变量 | 用途 |
| --- | --- |
| `MYSQL_PASSWORD` | MySQL 口令 |
| `DRUID_MONITOR_USER` / `DRUID_MONITOR_PASSWORD` | Druid 监控台账号 |
| `TOKEN_SECRET` | JWT 签名密钥 |

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
- 配置文件里的口令改为「环境变量 + 本地默认值」形式，仓库内不含真实生产凭据。
- `admin/src/utils/jsencrypt.js` 中的 RSA 密钥对是 RuoYi 上游模板自带的演示密钥，与后端公钥配对，**不是真实凭据，请勿用于生产**。
- 未纳入本仓库：开发工具链（HBuilderX、Maven 发行包、Redis 二进制）与前端依赖产物。

## 尚未包含的部分

`server/.../ChatRasaController.java` 调用 `http://localhost:5005/webhooks/rest/webhook`，
即 RAG / Rasa 对话服务。该服务不在本仓库内，未启动时 AI 问诊相关接口不可用，其余功能不受影响。

## 致谢与来源

后端与后台基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 脚手架改造；
用户端沿用 uni-app 模板（`app/package.json` 保留了上游 Gitee 仓库地址）。
包名 `com.huacai`、页面标题「基于RAG的宠物医院管理系统」为项目原始标识。

## License

`admin/package.json` 声明为 MIT；本仓库代码含 RuoYi 上游授权范围，生产使用前请自行核对各上游许可证。
