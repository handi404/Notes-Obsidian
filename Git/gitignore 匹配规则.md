`.gitignore` 文件是 Git 用于指定需要忽略的文件或目录的配置文件。以下是 `.gitignore` 的主要匹配规则和示例：

---

### **基础规则**

1. **忽略文件或目录**
    
    - `*.log`：忽略所有 `.log` 结尾的文件。
    - `temp/`：忽略名为 `temp` 的目录及其下所有内容。
    - `*.class`：忽略所有 `.class` 文件。
2. **排除规则**
    
    - 在规则前加 `!` 表示排除忽略：
        - `!important.log`：不忽略 `important.log` 文件，即使前面有 `*.log` 的规则。
        - `!dir/*.log`：不忽略 `dir` 目录下的 `.log` 文件。
3. **匹配当前目录下的文件**
    
    - `/*.txt`：只忽略当前目录下的 `.txt` 文件，不影响子目录的 `.txt` 文件。
4. **匹配子目录中的文件**
    
    - `**/debug.log`：忽略所有目录下的 `debug.log` 文件。
    - `**/temp/`：忽略任何位置名为 `temp` 的目录。

---

### **特殊字符说明**

1. `*`：匹配零个或多个任意字符（不包括路径分隔符 `/`）。
    
    - 示例：`*.log` 匹配 `error.log` 和 `debug.log`，但不匹配 `logs/error.log`。
2. `?`：匹配任意单个字符。
    
    - 示例：`file?.txt` 匹配 `file1.txt` 和 `file2.txt`，但不匹配 `file12.txt`。
3. `[]`：匹配指定范围内的字符。
    
    - 示例：`file[1-3].txt` 匹配 `file1.txt`、`file2.txt` 和 `file3.txt`。
4. `!`：排除忽略。
    
    - 示例：`!important.log` 不忽略 `important.log`。
5. `/`：指代路径分隔符，用于限制规则作用范围。
    
    - 示例：`/config/` 仅忽略项目根目录下的 `config` 目录。
6. `#`：注释行。
    
    - 示例：`# 这是注释`。

---

### **优先级**

1. `.gitignore` 文件从上到下解析，后面的规则会覆盖前面的规则。
2. 子目录中的 `.gitignore` 文件优先于父目录中的规则。

---

### **示例 `.gitignore` 文件**

```gitignore
# 忽略所有 .log 文件
*.log

# 忽略所有 target 目录及其内容
/target/

# 排除忽略一个特定的文件
!important.log

# 忽略所有 .class 文件
*.class

# 忽略某些文件或目录
temp/
build/

# 忽略任何位置的 debug.log 文件
**/debug.log
```

---

使用 `.gitignore` 时，可以通过 `git check-ignore -v <file>` 检查某个文件被忽略的原因，方便调试规则。