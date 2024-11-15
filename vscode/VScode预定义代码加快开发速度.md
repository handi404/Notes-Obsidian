
# VScode预定义代码，加快开发速度

原创  yanessa  [ 前端后端开发测试技术分享 ](javascript:void\(0\);)

__ _ _ _ _

# 设置步骤

1. 单击VScode左下角的“设置”标签。 single click "setting" icon in the bottom lefthand side of VScode. 

2. 选择“用户代码片段”。Select "User Snippets". 

3. 选择“新建全局代码片段文件”。 Select "New Global Snippets file..." 

4. 在输入框中输入“代码段文件名”，比如“yanessa-snippets.json”，按“Enter”确认。 

5. 在“yanessa-snippets.json”文件中添加下面片段（这些片段只是一些例子，你可以定义自己的片段）： 

```js
{
	
	"Print to console": {
		"prefix": "cl",
		"scope": "javascript,typescript,javascriptreact",
		"body": ["console.log($1)"],
		"description": "console.log"
	  },

	"reactComponent": {
		"prefix": "rfc",
		"scope": "javascript,typescript,javascriptreact",
		"body": [
		  "function ${1:${TM_FILENAME_BASE}}() {",
		  "\treturn (",
		  "\t\t<div>",
		  "\t\t\t$0",
		  "\t\t</div>",
		  "\t)",
		  "}",
		  "",
		  "export default ${1:${TM_FILENAME_BASE}}",
		  ""
		],
		"description": "React component"
	  },
	  "reactStyledComponent": {
		"prefix": "rsc",
		"scope": "javascript,typescript,javascriptreact",
		"body": [
		  "import styled from 'styled-components'",
		  "",
		  "const Styled${TM_FILENAME_BASE} = styled.$0``",
		  "",
		  "function ${TM_FILENAME_BASE}() {",
		  "\treturn (",
		  "\t\t<Styled${TM_FILENAME_BASE}>",
		  "\t\t\t${TM_FILENAME_BASE}",
		  "\t\t</Styled${TM_FILENAME_BASE}>",
		  "\t)",
		  "}",
		  "",
		  "export default ${TM_FILENAME_BASE}",
		  ""
		],
		"description": "React styled component"
	  },
	  "Vue3 Component": {
		"prefix": "vc",
		"body": [
		  "<template>",
		  "  <div class=\"container\">",
		  "    <!-- 在这里添加你的HTML代码 -->",
		  "  </div>",
		  "</template>",
		  "",
		  "<script setup>",
		  
		  "</script>",
		  "",
		  "<style scoped>",
		  "/* 在这里添加你的样式 */",
		  "</style>"
		],
		"description": "Create a new Vue component"
	  },
	  "Vue2 Component": {
		"prefix": "vc2",
		"body": [
		  "<template>",
		  "  <div class=\"container\">",
		  "    <!-- 在这里添加你的HTML代码 -->",
		  "  </div>",
		  "</template>",
		  "",
		  "<script>",
		  "export default {",
		  "  name: 'YourComponentName',",
		  "  // 在这里添加你的组件逻辑",
		  "}",
		  "</script>",
		  "",
		  "<style scoped>",
		  "/* 在这里添加你的样式 */",
		  "</style>"
		],
		"description": "Create a new Vue component"
	  },
}
```

6. 打开一个.vue文件，输入“vc”，在弹出的“快捷选项”中选择“vc”，此时.vue文件就会自动的添加对应的片段代码。 

预览时标签不可点
