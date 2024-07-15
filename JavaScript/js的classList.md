# classList的深入学习

- - [[#前言|前言]]
- [[#一、classList 的基本使用|一、classList 的基本使用]]
	- [[#一、classList 的基本使用#add方法|add方法]]
	- [[#一、classList 的基本使用#remove方法|remove方法]]
	- [[#一、classList 的基本使用#[toggle]方法|[toggle]方法]]
	- [[#一、classList 的基本使用#contains方法|contains方法]]
- [[#二、classList 的高级用法|二、classList 的高级用法]]
	- [[#二、classList 的高级用法#replace方法|replace方法]]
	- [[#二、classList 的高级用法#item方法|item方法]]
	- [[#二、classList 的高级用法#length用法|length用法]]
	- [[#二、classList 的高级用法#forEach方法|forEach方法]]
- [[#三、代码示例：|三、代码示例：]]
- [[#四、总结|四、总结]]

## 前言

classList 是 JavaScript 中一个用于操作元素类名的东西，这个东西有很多好用的方法，可以让我们轻松地添加、删除、切换和检查元素的类名。接下来我们详细学习一下 classList 的使用方法以及相关方法。

## 一、classList 的基本使用

classList 是 Element 接口的一个属性，其返回值是一个 DOMTokenList 对象，该对象表示当前元素的类名列表。我们可以通过以下方式来获取一个元素的 classList：

```javascript
const element = document.getElementById('myElement'); 
const classList = element.classList;
```

上述代码中，我们首先获取了一个 ID 为 myElement 的元素，然后通过 classList 属性获取了该元素的类名列表。

获取到 classList 后，我们可以使用以下方法来操作元素的类名：

### add方法

add 方法用于向元素添加一个或多个类名。例如，我们可以使用以下代码向一个元素添加一个名为 active 的类名：

```javascript
element.classList.add('active');
```

如果需要添加多个类名，可以在 add 方法中传入多个参数，例如：

```javascript
element.classList.add('active', 'visible', 'highlight');
```

### remove方法

remove 方法用于从元素中删除一个或多个类名。例如，我们可以使用以下代码从一个元素中删除名为 active 的类名：

```javascript
element.classList.remove('active');
```

如果需要删除多个类名，可以在 remove 方法中传入多个参数，例如：

```javascript
element.classList.remove('active', 'visible', 'highlight');
```

### [toggle]方法

toggle 方法用于在元素中切换一个类名的状态。如果元素中已经存在该类名，则该类名将被删除；如果元素中不存在该类名，则该类名将被添加。例如，我们可以使用以下代码在一个元素中切换名为 active 的类名：

```javascript
element.classList.toggle('active');
```

我们还可以在 toggle 方法中传入一个布尔值作为第二个参数，用于指定是否强制添加或删除类名。例如，以下代码将强制为一个元素添加名为 active 的类名：

```javascript
element.classList.toggle('active', true);
```

### contains方法

contains 方法用于检查元素中是否包含指定的类名。如果元素中包含该类名，则返回 true；否则返回 false。例如，我们可以使用以下代码检查一个元素中是否包含名为 active 的类名：  
Copy

```javascript
const hasActiveClass = element.classList.contains('active');
```

## 二、classList 的高级用法

除了基本的添加、删除、切换和检查类名的方法之外，classList 还提供了一些高级用法，可以让我们更加方便地操作元素的类名。

### replace方法

replace 方法用于替换元素中的一个类名为另一个类名。例如，我们可以使用以下代码将一个元素中名为 oldClass 的类名替换为名为 newClass 的类名：

```javascript
element.classList.replace('oldClass', 'newClass');
```

### item方法

item 方法用于获取元素类名列表中指定索引位置的类名。例如，以下代码将获取一个元素的第二个类名：

```javascript
const secondClass = element.classList.item(1);
```

需要注意的是，索引位置从 0 开始，因此上述代码中的 1 表示获取第二个类名。

### length用法

length 属性用于获取元素类名列表的长度。例如，以下代码将获取一个元素的类名[列表长度](https://so.csdn.net/so/search?q=%E5%88%97%E8%A1%A8%E9%95%BF%E5%BA%A6&spm=1001.2101.3001.7020)：

```javascript
const classListLength = element.classList.length;
```

### forEach方法

forEach 方法用于遍历元素的类名列表，并对每个类名执行指定的操作。例如，以下代码将输出一个元素的所有类名：

```javascript
element.classList.forEach(className => { 
	console.log(className); 
});
```

需要注意的是，forEach 方法只在现代浏览器中得到支持，如果需要兼容旧版浏览器，可以使用 Array.prototype.forEach 方法来代替。

## 三、代码示例：

```javascript
			function createMenu(menuData) {
				const menuEl = document.createElement('ul');

				menuData.forEach(item => {
					const itemEl = document.createElement('li');
					const labelEl = document.createElement('span');
					labelEl.innerText = item.label;
					itemEl.appendChild(labelEl);

					if (item.children.length > 0) {
						labelEl.addEventListener('click', () => {
							if (itemEl.classList.contains('open')) {
								itemEl.classList.remove('open');
								itemEl.removeChild(itemEl.lastChild);
							} else {
								itemEl.classList.add('open');
								const submenuEl = createMenu(item.children);
								itemEl.appendChild(submenuEl);
							}
						});
					}

					menuEl.appendChild(itemEl);
				});

				return menuEl;
			}

			const menuEl = createMenu(menu);
			document.body.appendChild(menuEl);

```

以这段代码为例，很显然这是[递归函数]，里面用到了`classList.contains('open') classList.remove('open') classList.add('open')`实现了动态添加删除元素类名，而通过类名是否存在的状态来实现递归的树形菜单，非常精髓

## 四、总结

classList 是一个非常实用的工具，可以让我们轻松地操作元素的类名。在使用 classList 时，我们可以通过 add、remove、toggle 和 contains 等方法来添加、删除、切换和检查类名；还可以通过 replace、item、length 和 forEach 等方法来进行更高级的操作。需要注意的是，classList 只在现代浏览器中得到支持，如果需要兼容旧版浏览器，可以使用其他库或者手动操作元素的 className 属性来代替。