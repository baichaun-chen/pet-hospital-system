# 基于 RAG 的宠物医院管理系统

宠物医院在线业务系统，含管理后台、服务端 API 和用户端 App 三端，AI 问诊通过 Rasa Webhook 接入；
Rasa 导诊服务本身在 `rasa/`。

## 目录结构

```
pet-hospital-system/
├── admin/    # 管理后台（Vue 3 + Vite + Element Plus）
├── server/   # 服务端（Spring Boot 3.5 + MyBatis + Druid + JWT）
├── app/      # 用户端 App（uni-app + Vue 3，H5 / 小程序 / App 多端）
└── rasa/     # AI 问诊的 Rasa 导诊服务（意图、规则、自定义 Action）
```

## 环境要求

| 组件 | 版本 |
| --- | --- |
| JDK | 17 |
| Maven | 3.9.x（本项目实测 3.9.11） |
| MySQL | 8.x |
| Redis | 5.x+ |
| Node.js | 18+（`admin/`、`app/` 构建） |
| Python | 3.10（实测 3.10.19；Rasa 3.5 不支持 3.11+） |
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
小程序 / App 端请用 HBuilderX 打开 `app/` 发行；`mp-weixin` 这类编译产物不入库（已在 `.gitignore` 中忽略 `unpackage/`）。

### 4. Rasa 导诊服务

```bash
cd rasa
pip install -r requirements.txt   # 需 Python 3.10；jieba 通过 rasa[jieba] extra 引入
rasa train                        # 仓库不含训练好的模型，首次必须先训练
rasa run actions                  # 终端一：自定义 Action，端口 5055
rasa run --enable-api             # 终端二：对话 API，端口 5005，即 server 端 rasa.url 指向的地址
```

回归测试（需要 5055 的 Action Server 在跑，否则自定义 Action 那一步会失败）：

```bash
rasa test core -s tests/test_stories.yml --endpoints endpoints.yml
```

验证导诊链路（和后端调的是同一个端点）：

```bash
curl -X POST http://localhost:5005/webhooks/rest/webhook \
  -H 'Content-Type: application/json' \
  -d '{"sender":"demo","message":"你好"}'
```

依次发「你好」→「我的宠物是狗」→「呕吐」应得到「请问您的宠物是什么种类」→「请问有什么症状」→「建议挂张医生的号」。
猫 → 李医生，其他种类 → 陈医生。

`models/` 与 `.rasa/`（训练缓存）不入库，克隆后必须 `rasa train` 才能启动；
`rasa.url` 在 `server/src/main/resources/application.yml` 里配置，Rasa 未启动时聊天接口返回「服务繁忙，请稍后再试。」

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

## Rasa 侧本轮修复（并入仓库时实测发现）

这份 Rasa 工程训练过，但只有 2026-02-23 19:51 那一次，产出的是 `language: en`、domain 内只有 7 个英文意图的
starter 模型；`domain.yml` / `data/nlu.yml` 的导诊内容是 31 分钟后（20:22）才加进去的，此后再没重训过——
而带着这份导诊数据重训会直接失败，原因见下表。真正跑 `rasa data validate` / `rasa train` 才把缺陷暴露出来：

| 缺陷 | 后果 | 处理 |
| --- | --- | --- |
| `domain.yml` 没有 `actions:` 段，自定义 Action 未注册 | `rasa data validate` 直接报 error，`rasa train` 起不来 | 补 `actions: [action_recommend_doctor]` |
| starter 的 happy path / sad path 1 / sad path 2 三条故事把 `greet` 接到 `utter_greet`，与「greet → 问宠物种类」的中文导诊规则矛盾 | `InvalidRule: Contradicting rules or stories found`，训练失败 | 删除这三条模板故事，保留导诊故事；原因写在 `data/stories.yml` 注释里 |
| `config.yml` 用 `WhitespaceTokenizer` 处理中文 | 整句被当成一个 token，训练时报 10 次 `Misaligned entity annotation` 并丢弃实体标注；实测「我的宠物是狗」抽不出 `pet_type`，该轮直接回空 | 改 `language: zh` + `JiebaTokenizer`，错位警告归零，三条导诊路径全部跑通 |
| `data/nlu.yml` 的 `examples: \|` 块里写了 `# === 中文问候 ===` 这类标记 | 块标量里 `#` 不是注释，被当成非法样本，`rasa data validate` 报 4 条 `This training example will be skipped` | 移除这些行内标记 |
| 导诊规则写了 `slot_was_set: pet_type: ["狗", "猫"]` | text 类型的 slot 不支持列表取值，这条规则永远匹配不上，推荐医生的那一步只靠训练故事的记忆偶然生效，测试里直接退化为 `action_default_fallback` | 改为 `- pet_type`（只要被填过即触发），具体分诊交给 Action 内部判断 |

修复后 `rasa data validate` 返回 0，`rasa train` 成功；
`rasa test core -s tests/test_stories.yml --endpoints endpoints.yml` 的 F1 / Accuracy 均为 **1.000**（5 条故事全过），
端到端经 `/webhooks/rest/webhook` 实测狗→张医生、猫→李医生、兔子→陈医生三条路径均正确。

写测试故事时有个坑：实体要用 Markdown 内联（`我的宠物是[狗](pet_type)`）标在 `user:` 文本里，
只在步骤上写 `intent:` 而不带实体的话 slot 不会填充，依赖 slot 的规则同样不触发。

## 剩余风险（本次未处理）

1. **依赖版本未升级**：Spring Boot 3.5.0、Druid 1.2.23、fastjson2 2.0.53、Velocity 2.3、MyBatis 3.0.3 均非最新版，fastjson2 与 Velocity 历史 CVE 较多。升级需要联网拉包并回归测试，本次刻意没动，以免给你一个编译不过的仓库。
2. **无频率限制**：AI 接口现在需登录，但没有 per-user 限流，登录用户仍可高频调用 Rasa。
3. **`/profile/**` 仍匿名可读**：上传目录里的文件对任何拿到 URL 的人可见（头像等需要公开展示）。若存放敏感附件，需要改为签名 URL 或鉴权。
4. **`${params.dataScope}` 依赖注解约束**：`SysUser/SysRole/SysDept` 三处 `${params.dataScope}` 已确认调用方都带 `@DataScope`，安全；但后续新增复用这些 mapper 而漏加注解的接口会变成注入口。
5. **代码生成模块**（`GenController` + Velocity）权限依赖管理员身份，本身是高价值目标，生产环境建议关闭或严格限权。
6. **无测试、无 CI**；`app/package.json` 依赖是 uni-app alpha 通道版本号，可复现构建较弱。
7. **许可证不一致**：`admin/package.json` 写 MIT，代码实际源自 Apache-2.0 的 RuoYi，见下文。
8. **Rasa 语料规模很小**：每个意图 4~11 条样本，换个说法就可能识别错；`mood_great` / `mood_unhappy` 等英文意图仍留在 domain 里，但对应故事已删除，命中后没有专门回复。
9. **Rasa 服务自身无鉴权**：实测监听的是 `0.0.0.0:5005`（对话 API）和 `0.0.0.0:5055`（Action Server），任何能访问该主机的人都能直接绕过 Spring 的登录校验调用导诊接口。只应在内网 / 本机运行，或加反向代理鉴权后再对外。

## 致谢与来源

后端与后台基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 脚手架改造；
用户端沿用 uni-app 模板（`app/package.json` 保留了上游 Gitee 仓库地址）。
包名 `com.huacai`、页面标题「基于RAG的宠物医院管理系统」为项目原始标识。

## License

`admin/package.json` 声明为 MIT；本仓库代码含 RuoYi 上游授权范围，生产使用前请自行核对各上游许可证。
