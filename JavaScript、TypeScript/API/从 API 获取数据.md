#### // fetch = 用于发出 HTTP 请求以获取资源的函数。
#### //（JSON 样式数据、图像、文件）
#### // 简化 JavaScript 中的异步数据获取和
#### // 用于与 API 交互以检索和发送
#### // 通过网络异步传输数据。
#### // fetch(url, {options})

---
[[URL 参数]]

```js
async function fetchData(){

    try{
		const pokemonName = document.getElementById("pokemonName").value.toLowerCase();//得到输入内容并小写  
        
        const response = await fetch(`https://pokeapi.co/api/v2/pokemon/${pokemonName}`);

        if(!response.ok){
            throw new Error("Could not fetch resource");
        }

        const data = await response.json();
        const pokemonSprite = data.sprites.front_default;
        const imgElement = document.getElementById("pokemonSprite");

        imgElement.src = pokemonSprite;
        imgElement.style.display = "block";
    }
    catch(error){
        console.error(error);
    }
}
```
```html
<body>

    <input type="text" id="pokemonName" placeholder="Enter Pokemon name">
    <button onclick="fetchData()">Fetch Pokemon</button><br>
    
    <img src="" alt="Pokemon Sprite" id="pokemonSprite" style="display: none">
    
    <script src="index.js"></script>
</body>
```
