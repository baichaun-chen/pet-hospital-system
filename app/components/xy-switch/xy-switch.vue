<template>
	<switch style="transform:scale(0.7)" :checked="props.activeValue == props.modelValue" @change="updateStatus"/>
</template>
<script setup>
	
	import {
		ref,
		// defineProps,
		// defineEmits,
		toRefs,
		watch,
		getCurrentInstance
	} from 'vue';

	const {
		proxy
	} = getCurrentInstance()

	const props = defineProps({
		/** 双向绑定的值 */
		modelValue: {
			type: [String, Number, Boolean]
		},
		/** 开关打开时返回的值 */
		activeValue: {
			type: [String, Number, Boolean],
			default: true,
		},
		/** 开关关闭时返回的值 */
		inactiveValue: {
			type: [String, Number, Boolean],
			default: false,
		},
	})

	/** 向父组件传递信息 */
	const emit = defineEmits(['update:modelValue', 'change']);
	
	/** 开关值更新时触发 */
	const updateStatus = (e) => {
		let val = props.inactiveValue
		if(e.detail.value) {
			val = props.activeValue
		}
		emit('update:modelValue', val);
		emit('change');
	}
</script>