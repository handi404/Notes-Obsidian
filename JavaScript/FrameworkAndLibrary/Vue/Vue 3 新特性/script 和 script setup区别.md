在 Vue 3 中，`<script>` 和 `<script setup>` 是两种用来定义组件逻辑的方式，它们各有不同的特点和使用场景。`<script setup>` 是 Vue 3 中引入的一个新的语法糖，它让开发者在编写组件时更加简洁和直观。下面详细介绍它们的区别：

### 1. **基本区别**

- **`<script>`**：Vue 组件中传统的 `<script>` 标签，用来导入依赖、定义数据、方法、计算属性、生命周期钩子等。需要显式地导出一个对象来定义组件。
  
- **`<script setup>`**：Vue 3 提供的一种新的语法糖，简化了组件的定义。它无需显式地导出组件定义，所有在 `<script setup>` 中定义的变量、方法和逻辑都是直接绑定到模板中的，并且默认具有更简洁的写法。

### 2. **具体差异**

#### **(1) 语法简化**
- **传统 `<script>`**

使用传统的 `<script>`，你需要在 `export default` 中定义组件的逻辑，比如 `data`、`methods`、`computed`、`props` 等：

```vue
<script>
export default {
  props: {
    title: String
  },
  data() {
    return {
      message: 'Hello World'
    };
  },
  methods: {
    sayHello() {
      console.log(this.message);
    }
  }
};
</script>
```

- **`<script setup>`**

使用 `<script setup>`，你不需要显式地导出组件，变量和方法可以直接在 `<script setup>` 中定义和使用，代码变得更加简洁：

```vue
<script setup>
import { ref } from 'vue';

const title = 'Hello World';
const message = ref('Hello World');

function sayHello() {
  console.log(message.value);
}
</script>
```

- **解释**：
  - 在 `<script setup>` 中，所有顶级变量和函数都是自动暴露给模板的，省去了 `export default` 和 `this` 的使用。
  - 不需要显式的 `this` 来引用变量或方法，直接使用声明的变量即可。

#### **(2) 自动注册的模板变量**
- **传统 `<script>`**

在传统的 `<script>` 中，所有的变量和方法需要通过 `this` 访问，或者明确定义在 `data`、`methods`、`computed` 等对象内。

- **`<script setup>`**

在 `<script setup>` 中，任何声明的变量都可以直接在模板中使用，不需要 `this`。你声明的任何变量在模板中自动绑定，无需显式导出或传递。

#### **(3) 更高的性能**
- **传统 `<script>`**

在传统 `<script>` 中，所有组件逻辑会在 `setup()` 函数中手动配置，存在一定的性能开销。

- **`<script setup>`**

`<script setup>` 是编译时执行的，它会在组件编译时被转换为更高效的代码，减少了运行时开销，从而提升性能。

#### **(4) 组合式 API**
- **传统 `<script>`**

在使用组合式 API 时，传统的 `<script>` 需要手动调用 `setup()` 函数来定义组合逻辑，并需要通过返回值将属性和方法暴露给模板使用：

```vue
<script>
import { ref } from 'vue';

export default {
  setup() {
    const message = ref('Hello World');
    const sayHello = () => console.log(message.value);

    return { message, sayHello };
  }
};
</script>
```

- **`<script setup>`**

在 `<script setup>` 中，组合式 API 的使用更加简化，你可以直接使用组合 API，而不需要显式返回任何东西：

```vue
<script setup>
import { ref } from 'vue';

const message = ref('Hello World');
const sayHello = () => console.log(message.value);
</script>
```

#### **(5) TS 支持**
- **传统 `<script>`**

在传统 `<script>` 中，你可以通过添加 `lang="ts"` 来使用 TypeScript。类型声明需要显式书写在 `setup()` 函数中：

```vue
<script lang="ts">
import { ref } from 'vue';

export default {
  setup() {
    const count = ref<number>(0);
    return { count };
  }
};
</script>
```

- **`<script setup>`**

在 `<script setup>` 中，TypeScript 支持更加简洁。变量和函数可以直接使用类型声明：

```vue
<script setup lang="ts">
import { ref } from 'vue';

const count = ref<number>(0);
</script>
```

#### **(6) Prop 的定义**
- **传统 `<script>`**

在传统的 `<script>` 中，`props` 需要在 `export default` 中显式定义：

```vue
<script>
export default {
  props: {
    title: {
      type: String,
      required: true
    }
  }
};
</script>
```

- **`<script setup>`**

在 `<script setup>` 中，`props` 可以通过 `defineProps()` 来获取，定义更加灵活：

```vue
<script setup>
const props = defineProps<{ title: string }>();
</script>
```

#### **(7) Emits 的定义**
- **传统 `<script>`**

在传统的 `<script>` 中，事件需要在 `setup()` 中显式定义和触发：

```vue
<script>
export default {
  emits: ['customEvent'],
  setup(props, { emit }) {
    const triggerEvent = () => emit('customEvent');
    return { triggerEvent };
  }
};
</script>
```

- **`<script setup>`**

在 `<script setup>` 中，事件可以通过 `defineEmits()` 来定义和触发：

```vue
<script setup>
const emit = defineEmits(['customEvent']);
const triggerEvent = () => emit('customEvent');
</script>
```

### 3. **适用场景**

- **`<script>`**：适用于需要更多自定义逻辑的场景，或者你希望继续使用传统的 `options API` 风格（如 `data`、`methods`、`computed`）。
  
- **`<script setup>`**：更适合使用 Vue 3 的组合式 API 时，能简化代码，提升开发效率和性能。它非常适合小型组件、功能单一的组件，或者更倾向于组合式 API 的开发者。

### 总结

- `<script setup>` 是 Vue 3 中的语法糖，用于简化组件逻辑的书写，不需要手动导出组件，所有顶级声明的变量和方法会自动暴露给模板使用。
- 它比传统的 `<script>` 更简洁，并且性能更高。
- `<script>` 适用于复杂的项目或者仍然喜欢使用 Vue 2 中的选项式 API 的开发者，而 `<script setup>` 更适合组合式 API 和较为轻量的组件。

总体来说，`<script setup>` 提供了更简洁的语法和更好的性能，在 Vue 3 中被广泛推荐使用。