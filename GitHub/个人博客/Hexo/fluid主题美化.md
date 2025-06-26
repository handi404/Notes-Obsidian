首先在根目录新建 `_config.fluid.yml`
## Mac 风格代码块
在 themes/fluid/source/css 中添加 macpanel.styl
```css
.highlight
    background: #21252b
    border-radius: 5px
    box-shadow: 0 10px 30px 0 rgba(0, 0, 0, .4)
    padding-top: 30px

    &::before
      background: #fc625d
      border-radius: 50%
      box-shadow: 20px 0 #fdbc40, 40px 0 #35cd4b
      content: ' '
      height: 12px
      left: 12px
      margin-top: -20px
      position: absolute
      width: 12px
```

在 `_config.fluid.yml` 中引用
```yaml
# --- Fluid 主题覆盖配置 ---
code:
  # 确保开启 Fluid 的代码复制按钮等功能
  copy_btn: true
  
# 指定自定义 .js 文件路径，支持列表；路径是相对 source 目录，如 /js/custom.js 对应存放目录 source/js/custom.js
# Specify the path of your custom js file, support list. The path is relative to the source directory, such as `/js/custom.js` corresponding to the directory `source/js/custom.js`
custom_js:

# 指定自定义 .css 文件路径，用法和 custom_js 相同
# The usage is the same as custom_js
custom_css:
  - /css/macpanel # Mac风格代码块
  
highlight:
  enable: true
  lib: highlightjs            # 使用 highlight.js
  highlightjs:
    # 注意：style 名称必须和 /themes/fluid/source/css/highlightjs/styles/ 目录下的文件名一一对应
    # “github gist” 应写为 “github-gist”
    style: "github dark dimmed"
    # 若想单独为暗色模式指定样式，也请使用有效的名称，例如 atom-one-dark
    style_dark: "github dark"
```

## 导航栏毛玻璃特效
实验性，fluid 配置中开启即可
```yaml
# 导航栏的相关配置
# Navigation bar
navbar:
  # 导航栏左侧的标题，为空则按 hexo config 中 `title` 显示
  # The title on the left side of the navigation bar. If empty, it is based on `title` in hexo config
  blog_title: "HAN'S BLOG"

  # 导航栏毛玻璃特效，实验性功能，可能会造成页面滚动掉帧和抖动，部分浏览器不支持会自动不生效
  # Navigation bar frosted glass special animation. It is an experimental feature
  ground_glass:
    enable: true

    # 模糊像素，只能为数字，数字越大模糊度越高
    # Number of blurred pixel. the larger the number, the higher the blur
    px: 3

    # 不透明度，数字越大透明度越低，注意透明过度可能看不清菜单字体
    # Ratio of opacity, 1.0 is completely opaque
    # available: 0 - 1.0
    alpha: 0.5
```

## 背景固定
在根目录新建 scripts 文件夹，在其中新建 injector.js
```js
const { root: siteRoot = "/" } = hexo.config;
hexo.extend.injector.register("body_begin", `<div id="web_bg"></div>`);
hexo.extend.injector.register("body_end",`<script src="${siteRoot}js/backgroundize.js"></script>`);
```

在根目录的 source 文件夹中新建 js 文件夹，在其中新建 backgroundize.js
```js
document
  .querySelector('#web_bg')
  .setAttribute('style', `background-image: ${document.querySelector('.banner').style.background.split(' ')[0]};position: fixed;width: 100%;height: 100%;z-index: -1;background-size: cover;`);

document
  .querySelector("#banner")
  .setAttribute('style', 'background-image: url()')

document
  .querySelector("#banner .mask")
  .setAttribute('style', 'background-color:rgba(0,0,0,0)')
```

## 各级标题以及引用修改
在 fluid\source\css 中新建 mk.css
```css
/* 各级标题以及引用修改 */
:root {
  --control-text-color: #777;
  --select-text-bg-color: rgba(223, 197, 223); /*#7e66992e;*/
  --search-select-bg-color: #8163bd;

  /* side bar */
  /* --side-bar-bg-color: #f7f7fb;
  --active-file-text-color: #8163bd;
  --active-file-bg-color: #e9e4f0;
  --item-hover-bg-color: #e9e4f0;
  --active-file-border-color: #8163bd; */

  --title-color: #6c549c;
  /* --blur-text-color: #c8c8c8; */
  --text-color:/*  #5e676d */ #333333 /* #2c3e50 */ /* #34495e */;
  --light-text-color: #414141;
  --font-sans-serif: "Ubuntu", "Source Sans Pro", sans-serif !important;
  --font-monospace: "Fira Code", "Roboto Mono", monospace !important;

  --purple-1: #8163bd;
  --purple-2: #79589f;
  --purple-3: #fd5eb8;
  --purple-4: #bb60d5;
  --purple-light-1: rgba(99, 99, 172, 0.05);
  --purple-light-2: rgba(99, 99, 172, 0.1);
  --purple-light-3: rgba(99, 99, 172, 0.2);
  --purple-light-4: rgba(129, 99, 189, 0.3);
  --purple-light-5: #e9e4f0;
  --purple-light-6: rgba(129, 99, 189, 0.7);
  --purple-light-7: rgba(129, 99, 189, 0.3);

  --shadow: var(--purple-light-3) 0px 5px 10px;

  --fence-border: #e7eaed;
  --table-border: rgb(143, 143, 143);
  --boxes: rgba(10, 10, 10, 0.05);
}


.markdown-body h1, .markdown-body h2 {
    border-bottom: none;
}





.markdown-body > h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4,
.markdown-body h5,
.markdown-body h6 {
  position: relative;
  margin-top: 3.5em;
  font-weight: normal;
  cursor: text;
  color: #6c549c;
}

h1 tt,
h1 code {
  font-size: inherit !important;
}
h2 tt,
h2 code {
  font-size: inherit !important;
}
h3 tt,
h3 code {
  font-size: inherit !important;
}
h4 tt,
h4 code {
  font-size: inherit !important;
}
h5 tt,
h5 code {
  font-size: inherit !important;
}
h6 tt,
h6 code {
  font-size: inherit !important;
}



h1 {
  padding-bottom: 0.4rem;
  font-size: 2.2rem;
  line-height: 1.3;
}
h1 {
  text-align: center;
  padding-bottom: 0.3em;
  font-size: 2.2em;
  line-height: 1.2;
  margin: 1.6em auto 1em;
}
h1:after {
  content: "";
  display: block;
  margin: 0.2em auto 0;
  width: 100px;
  height: 2px;
  border-bottom: 2px solid var(--title-color);
}

h2 {
  /* margin: 1.4em auto 0.8em; */
  /* padding-left: 5px; */
  /* line-height: 1.0; */
  font-size: 1.8em;
  /* border-left: 5px solid var(--title-color); */
  /* border-bottom: 1px solid var(--title-color); */
}
h3 {
  font-size: 1.5rem;
  margin: 1.2em auto 0.5em;
}
h4 {
  font-size: 1.3rem;
}
h5 {
  font-size: 1.2rem;
}
h6 {
  font-size: 1.1rem;
}



/* 引用 */
.markdown-body blockquote {
  border-left: 0.2rem solid var(--purple-light-4);
  padding: 10px 15px;
  color: var(--light-text-color);
  background-color: var(--purple-light-1);
}



.markdown-body code {
  padding: 2px 4px;
  border-radius: 2px;
  font-size: 0.92rem;
  /* color: var(--purple-3); */
  background-color: var(--purple-light-1);
}

.markdown-body code {
  margin: 0 2px;
}


.markdown-body pre {
  --select-text-bg-color: rgba(223, 197, 223) !important;
}
```

然后在配置中引用

## 页面动态黑色线条
在 fluid\source\js 中新建 DynamicLine.min.js
```js
/**
 * Skipped minification because the original files appears to be already minified.
 * Original file: /gh/EmoryHuang/BlogBeautify@1.1/DynamicLine.js
 *
 * Do NOT use SRI with dynamically generated files! More information: https://www.jsdelivr.com/using-sri-with-dynamic-files
 */
//背景黑色线条bynote.cn
!(function () {
  function o(w, v, i) {
    return w.getAttribute(v) || i;
  }
  function j(i) {
    return document.getElementsByTagName(i);
  }
  function l() {
    var i = j("script"),
      w = i.length,
      v = i[w - 1];
    return {
      l: w,
      z: o(v, "zIndex", -1),
      o: o(v, "opacity", 0.5),
      c: o(v, "color", "0,0,0"),
      n: o(v, "count", 99),
    };
  }
  function k() {
    (r = u.width =
      window.innerWidth ||
      document.documentElement.clientWidth ||
      document.body.clientWidth),
      (n = u.height =
        window.innerHeight ||
        document.documentElement.clientHeight ||
        document.body.clientHeight);
  }
  function b() {
    e.clearRect(0, 0, r, n);
    var w = [f].concat(t);
    var x, v, A, B, z, y;
    t.forEach(function (i) {
      (i.x += i.xa),
        (i.y += i.ya),
        (i.xa *= i.x > r || i.x < 0 ? -1 : 1),
        (i.ya *= i.y > n || i.y < 0 ? -1 : 1),
        e.fillRect(i.x - 0.5, i.y - 0.5, 1, 1);
      for (v = 0; v < w.length; v++) {
        x = w[v];
        if (i !== x && null !== x.x && null !== x.y) {
          (B = i.x - x.x), (z = i.y - x.y), (y = B * B + z * z);
          y < x.max &&
            (x === f &&
              y >= x.max / 2 &&
              ((i.x -= 0.03 * B), (i.y -= 0.03 * z)),
            (A = (x.max - y) / x.max),
            e.beginPath(),
            (e.lineWidth = A / 2),
            (e.strokeStyle = "rgba(" + s.c + "," + (A + 0.2) + ")"),
            e.moveTo(i.x, i.y),
            e.lineTo(x.x, x.y),
            e.stroke());
        }
      }
      w.splice(w.indexOf(i), 1);
    }),
      m(b);
  }
  var u = document.createElement("canvas"),
    s = l(),
    c = "c_n" + s.l,
    e = u.getContext("2d"),
    r,
    n,
    m =
      window.requestAnimationFrame ||
      window.webkitRequestAnimationFrame ||
      window.mozRequestAnimationFrame ||
      window.oRequestAnimationFrame ||
      window.msRequestAnimationFrame ||
      function (i) {
        window.setTimeout(i, 1000 / 45);
      },
    a = Math.random,
    f = { x: null, y: null, max: 20000 };
  u.id = c;
  u.style.cssText =
    "position:fixed;top:0;left:0;z-index:" + s.z + ";opacity:" + s.o;
  j("body")[0].appendChild(u);
  k(), (window.onresize = k);
  (window.onmousemove = function (i) {
    (i = i || window.event), (f.x = i.clientX), (f.y = i.clientY);
  }),
    (window.onmouseout = function () {
      (f.x = null), (f.y = null);
    });
  for (var t = [], p = 0; s.n > p; p++) {
    var h = a() * r,
      g = a() * n,
      q = 2 * a() - 1,
      d = 2 * a() - 1;
    t.push({ x: h, y: g, xa: q, ya: d, max: 6000 });
  }
  setTimeout(function () {
    b();
  }, 100);
})();
```

依旧在配置中引用

## 毛玻璃滤镜
在 fluid\source\css 中新建 glassBg.css
```css
/* 正文底页毛玻璃效果 */
#board {
  -webkit-backdrop-filter: blur(10px);
  backdrop-filter: blur(10px);
}
/* 侧边目录毛玻璃效果 */
#toc {
  padding: 10px;
  top: 4rem;
  background-color: var(--board-bg-color);
  border-radius: 10px;
  -webkit-backdrop-filter: blur(var(--background-blur));
  backdrop-filter: blur(var(--background-blur));
}
```

在 `_config.fluid.yml` 中引用并修改主面板背景色
```yaml
# --- Fluid 主题覆盖配置 ---
code:
  # 确保开启 Fluid 的代码复制按钮等功能
  copy_btn: true
  
  
# 指定自定义 .js 文件路径，支持列表；路径是相对 source 目录，如 /js/custom.js 对应存放目录 source/js/custom.js
# Specify the path of your custom js file, support list. The path is relative to the source directory, such as `/js/custom.js` corresponding to the directory `source/js/custom.js`
custom_js:
  - /js/DynamicLine.min.js # 页面动态黑色线条

# 指定自定义 .css 文件路径，用法和 custom_js 相同
# The usage is the same as custom_js
custom_css:
  - /css/macpanel # Mac风格代码块
  - /css/glassBg.css # 毛玻璃滤镜
  
highlight:
  enable: true
  lib: highlightjs            # 使用 highlight.js
  highlightjs:
    # 注意：style 名称必须和 /themes/fluid/source/css/highlightjs/styles/ 目录下的文件名一一对应
    # “github gist” 应写为 “github-gist”
    style: "github dark dimmed"
    # 若想单独为暗色模式指定样式，也请使用有效的名称，例如 atom-one-dark
    style_dark: "github dark"

color:

  # 主面板背景色
  # Color of main board
  -board_color: "#ffffff"
  -board_color_dark: "#252d38"
  +board_color: "#ffffff80"
  +board_color_dark: "#00000080"
```


## 文章滑入动画
fluid\source\css 中新建 scrollAnimation.css
```css
/* 首页添加文章滑入动画 */
.index-card {
  transition: all 0.5s;
  transform: scale(calc(1.5 - 0.5 * var(--state)));
  opacity: var(--state);
  margin-bottom: 2rem;
}

.index-img img {
  margin: 20px 0;
}
```

fluid\source\js 中新建 scrollAnimation.js
```js
// 首页添加文章滑入动画
const cards = document.querySelectorAll('.index-card')
if (cards.length) {
  document.querySelector('.row').setAttribute('style', 'overflow: hidden;')
  const coefficient = document.documentElement.clientWidth > 768 ? .5 : .3
  const origin = document.documentElement.clientHeight - cards[0].getBoundingClientRect().height * coefficient

  function throttle(fn, wait) {
    let timer = null;
    return function () {
      const context = this;
      const args = arguments;
      if (!timer) {
        timer = setTimeout(function () {
          fn.apply(context, args);
          timer = null;
        }, wait)
      }
    }
  }

  function handle() {
    cards.forEach(card => {
      card.setAttribute('style', `--state: ${(card.getBoundingClientRect().top - origin) < 0 ? 1 : 0};`)
    })
    console.log(1)
  }


  document.addEventListener("scroll", throttle(handle, 100));
}
```

配置中引用
```yaml
# --- Fluid 主题覆盖配置 ---
code:
  # 确保开启 Fluid 的代码复制按钮等功能
  copy_btn: true
  
  
# 指定自定义 .js 文件路径，支持列表；路径是相对 source 目录，如 /js/custom.js 对应存放目录 source/js/custom.js
# Specify the path of your custom js file, support list. The path is relative to the source directory, such as `/js/custom.js` corresponding to the directory `source/js/custom.js`
custom_js:
  - /js/scrollAnimation.js # 文章滑入动画
  - /js/DynamicLine.min.js # 页面动态黑色线条

# 指定自定义 .css 文件路径，用法和 custom_js 相同
# The usage is the same as custom_js
custom_css:
  - /css/macpanel # Mac风格代码块
  - /css/scrollAnimation.css # 文章滑入动画
  - /css/glassBg.css # 毛玻璃滤镜
  
highlight:
  enable: true
  lib: highlightjs            # 使用 highlight.js
  highlightjs:
    # 注意：style 名称必须和 /themes/fluid/source/css/highlightjs/styles/ 目录下的文件名一一对应
    # “github gist” 应写为 “github-gist”
    style: "github dark dimmed"
    # 若想单独为暗色模式指定样式，也请使用有效的名称，例如 atom-one-dark
    style_dark: "github dark"

color:

  # 主面板背景色
  # Color of main board
  -board_color: "#ffffff"
  -board_color_dark: "#252d38"
  +board_color: "#ffffff80"
  +board_color_dark: "#00000080"
```