

#  都2024了，还在用 Postman 做 HTTP 接口测试吗？

__ _ _ _ _

作为Java开发者，尤其是做业务开发的，免不了要写一大堆接口，也就是我们所说的 Controller API。

身为一名合格的后端开发人员，我们不能直接把写完的接口抛给前端用（ps：虽然有一些人就是这么干的，实话说，我也这么干过，但前提是跟前端关系比较好，要不然容易挨打）。而是先自己测试一下（俗称自测），等测试通过了，再上测试环境，再给前端联调。

从而节省来回返工和扯皮的时间，顺便也能维护作为后端的尊严，以及证明自己写的代码质量没问题。

那你平时做接口测试的时候用什么工具呢？我问了一圈发现，很多人都用 Postman。Postman
确实是很好用，我也用，但是更多的是用来测试第三方服务，比如要集成一个外部HTTP SDK，一般会在 Postman 中单独创建一个集合来用。

但是如果是平时做写 Java 接口的话，再从 IDEA 中跳出来到 Postman 中创建一个 Request 就有点儿麻烦了。人懒，所以我选择在 IDEA这还是 JetBrains 官方出品的。
![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFglD0NJgHxwWAheYHQOJ2SDCZnNU767ggZDCbNdpdQajeGd5iaekOibGrA/640?wx_fmt=png&from=appmsg)

安装完成后，项目中Controller 中带有 ` @RequestMapping ` 、 ` @GetMapping ` 、 ` @PostMapping
` 注解的HTTP接口前都会多一个小图标，IDEA 版本不一样图片的样式也有些差别。但是功能都一样，点击小图标，会转到一个以 ` .http `
结尾的文件中，这就是 HTTP Client 的编辑器，可以在这里创建一个项目或者一个 Controller 的所有请求。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgXnS6vO2HsnY8plM83qI3PM21ibSJSuia2SD5wHFWm5wia13Eib0adJeXfw/640?wx_fmt=png&from=appmsg)

然后在 ` .http ` 文件中，点击任何一个接口前的绿色小按钮，就可以执行请求了。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgIXUXxVwPyGic5s0Fb4RHRldvMjLe8ox8fDCicRQibiakicnXicVtR5M0ZnZQ/640?wx_fmt=png&from=appmsg)

所有的 ` .http ` 文件统一在项目的 ` Scratches and Consoles ` 下的 ` Scratches `
目录下，可以根据功能模块创建多个文件，来区分不同的功能，和 Postman中的集合是一个意思。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFg7mbvNmTKMWrKulE7O1poYFcsxPicias8I9FUOlatTovvj0BTticFEbjKQ/640?wx_fmt=png&from=appmsg)

可以通过左上角小按钮来添加不同类型的请求，比如 Get、不同参数形式的 Post ，还支持GRPC、WebSocket 这些。
![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgJ4aIkUYicnicDJOgKBObEnWncK1aoic1B22DPbNXtdJU5gQfJ6188Bzlg/640?wx_fmt=png&from=appmsg)

这里要说一个小技巧  ，有时候我们做的一个 POST 接口请求参数过多，有些大表单甚至有几十个参数，这也是很多人不想测试的原因，构造表单参数就很麻烦了。

可以装一个实体转 JSON 的插件，直接一下将实体类转为 JSON 。这不一个JSON参数就出来了，比如这个 ` POJO to JSON ` 插件。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgrZF6oD1DNIuKaZMk1pXsOqQ1N5AXq2os2jHzwjqNQrEWrRiaKiaRygUg/640?wx_fmt=png&from=appmsg)

例如，进入到一个参数实体中，点击右键，然后选择 ` Copy Json ` ，完整的 JSON 参数就有了，很方便。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgdyB0YhmDLI0gmntuKHhOv1XPyxGMy63iamHq7qtib79BJqqlKLG1U40Q/640?wx_fmt=png&from=appmsg)

如果不想装插件的话，可以用 ChatGPT、Kimi ，将实体类给他们，让他们转一个 JSON 出来，只要你的参数名命名规范，出来的参数值都堪称完美。

说回 HTTP Client，还可以创建不同的环境文件，来区分不同的环境，比如开发和测试环境。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgmCsB7qPEFK8EYHP6ZGZN7ad2UKgVwW1j1M9hp7Va7pXouHTTaicKG5g/640?wx_fmt=png&from=appmsg)

我创建了一个环境文件，然后在里面定义了 dev 和 test 两个环境，每个环境里的参数值是不一样的。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgk82eINrjzibtVRuJCsxKicMoue0wwSRad48zsf1u5Ngh7hl8puH5grcA/640?wx_fmt=png&from=appmsg)

然后在请求中选择对应的环境，就可以使用对应的参数了。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgfUQn2PsODL0j9ibPsRg4ic3rN6Hnib0gSCajWu1QyiasIWnZ6QrjK41XxQ/640?wx_fmt=png&from=appmsg)

HTPP Client 当然也考虑到一直使用 Postman 的用户了，安装 ` Import from Postman Collections `
这个插件后，可以一键将 Postman 集合导进来，然后在 IDEA 中快速的将 Postman 的请求转为 HTTP Client 请求。

![](https://mmbiz.qpic.cn/sz_mmbiz_png/iaWSDo4TfyZiaZnJYU3NXewF7QhWLQcHFgD3DXOBPyPn8O8UduwSUOCaunnFnNO6t2TstXG9w8XcS5N6M6xE42yg/640?wx_fmt=png&from=appmsg)

别的功能其实还有，比如支持指定 HTTP 协议的版本，在结尾加上 ` HTTP/2 ` 可以指定使用 HTTP/2 协议。

将 curl 命令粘贴进去，自动转为 HTTP Client 格式等等。