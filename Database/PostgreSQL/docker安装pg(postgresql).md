拉取镜像

```
docker pull postgres:15.5
```

创建本地卷或创建挂载目录(/docker/postgresql/data)

```
docker volume create pgdata
```

数据卷可以在容器之间共享和重用， 默认会一直存在，即使容器被删除（docker volume inspect pgdata可查看数据卷的本地位置，如果使用该方式创建可查看创建成功数据

```
sudo ls /var/lib/docker/volumes/pgdata/_data
```

）

方式一启动容器使用持久数据存储启动 PostgreSQL 容器

```
docker run --name postgres15 -e POSTGRES_PASSWORD=123456 -p 5432:5432 -v pgdata:/var/lib/postgresql/data -d postgres:15.5
```

方式二启动容器挂载到指定目录

```
docker run --name postgres15 -e POSTGRES_PASSWORD=123456 -p 5432:5432 -v /usr/lcoal/pg/data:/var/lib/postgresql/data -d postgres:15.5
```

`-v /usr/local/pg/data:/var/lib/postgresql/data` 将运行镜像的 `/var/lib/postgresql/data` 目录挂载到宿主机 `/usr/local/pg/data` 目录

## 进入容器创建角色

```
docker exec -it postgres15 /bin/bash
```

切换用户

```
su postgres;
```

创建sonar用户

```
createuser -P -s -e sonar;
```

连接数据库

```
psql;
```

创建sonar数据库

```
create database sonar owner=sonar;
```

查看创建后的数据库

```
\l
```

查看用户

```
\du
```

在 PostgreSQL 中创建一个支持远程登录、可创建数据库并能执行增删改查操作的用户，需按以下步骤操作：

---

### 1. **允许远程连接（修改配置文件）**
#### 修改 `postgresql.conf`
```bash
sudo nano /etc/postgresql/<版本>/main/postgresql.conf
```
将 `listen_addresses` 改为：
```ini
listen_addresses = '*'   # 允许所有IP连接（生产环境建议限制IP）
```

#### 修改 `pg_hba.conf`（IP 白名单）
```bash
sudo nano /etc/postgresql/<版本>/main/pg_hba.conf
```
添加规则（示例允许所有 IPv 4 连接）：
```ini
# TYPE  DATABASE  USER  ADDRESS      METHOD
host    all       all   0.0.0.0/0    md5   # 使用密码认证
```

#### 重启 PostgreSQL
```bash
sudo systemctl restart postgresql
```

---

### 2. **创建用户并授权**
#### 登录 PostgreSQL
```bash
sudo -u postgres psql
```

#### 创建用户并设置密码
```sql
CREATE USER myuser WITH PASSWORD 'your_password';
```

#### 授予数据库创建权限
```sql
ALTER USER myuser CREATEDB;  -- 允许创建数据库
```

#### 授予基本操作权限（可选全局授权）
```sql
-- 授予连接所有数据库权限
ALTER USER myuser WITH LOGIN;

-- 授予对新创建数据库的所有权（自动拥有全部权限）
-- 用户创建的数据库默认成为其所有者
```

---

### 3. **测试用户操作**
#### 以新用户登录
```bash
psql -h <服务器IP> -U myuser -d postgres  # 连接到默认数据库
```

#### 创建新数据库
```sql
CREATE DATABASE mydb;
```

#### 在新数据库执行操作
```sql
\c mydb  -- 切换到新数据库

-- 创建表
CREATE TABLE test (id SERIAL PRIMARY KEY, name VARCHAR(50));

-- 增删改查
INSERT INTO test (name) VALUES ('Alice');
SELECT * FROM test;
UPDATE test SET name = 'Bob' WHERE id = 1;
DELETE FROM test WHERE id = 1;
```

---

### 4. **权限说明**
| 权限                | 命令                          | 说明                          |
|---------------------|-------------------------------|-------------------------------|
| 远程登录            | `ALTER USER ... LOGIN`        | 默认已包含                    |
| 创建数据库          | `ALTER USER ... CREATEDB`     | 关键授权项                    |
| 数据库所有权        | 自动获得自建数据库所有权      | 无需额外授权                  |
| 增删改查            | 在自建数据库中自动拥有所有权限| 包括表、序列等                |

---

### 5. **安全建议**
1. **强密码策略**：使用复杂密码（避免默认密码）。
2. **IP限制**：在 `pg_hba.conf` 中限制来源IP（如 `192.168.1.0/24`）。
3. **最小权限原则**：非必要不授予 `CREATEDB` 或 `SUPERUSER`。
4. **防火墙**：配置服务器防火墙（如 `ufw`）仅允许信任 IP 访问端口 5432。

---

### 故障排查
- **连接失败**：检查 `listen_addresses` 和 `pg_hba.conf` 规则顺序。
- **权限不足**：确认用户是否拥有目标数据库的操作权限（对非自建库需单独授权）。
- **日志查看**：检查日志 `/var/log/postgresql/postgresql-<版本>-main.log`。

通过以上步骤，用户即可远程登录、创建数据库并在自建库中执行完整操作。