# 构建脚本

## buildAll.py

### 用法

````
python buildAll.py JAVA_HOME (不必要,仅在需要制定JAVA时使用)
````

### 脚本内部变量

* ENABLE_GRADLE_OUTPUT 是否显示gradle的输出
* ENABLE_GRADLE_OUTPUT_INFO 是否在gradle的命令行加入--info
* OUTPUT_DIR 输出目录
* VERSION_CONFIGS_DIR 版本配置目录

## genfont.js

### 用法

````
node genfont.js [参数...]
node genfont.js 
    -f, --family-name <name>  字体家族名称 (defaul默认t: "Fluent UI Icons")
    -v, --variation <name>    字体变体 (default: "Regular")
    -i, --icons <path>        输入的.icons文件
    -o, --output <dir>        输出目录
    -c, --codepoint <hex>     起始 Unicode 代码点 (默认值:0x0,格式为十六进制)
    -h, --help                显示帮助
````

### .icons文件格式

```
图标名称|@\@|<svg>....</svg>
```

* `|@\@|` 是分隔符
* SVG文件不能太复杂,且脚本忽略填充颜色
* SVG字符串中的 `width="${WIDTH}" height="${HEIGHT}` 会自动替换
