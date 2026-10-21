<template>
	<view class="container" ref="containerRef">
		<uni-data-checkbox :mode="props.mode" :multiple="props.multiple" v-model="selectedValue" :localdata="data"
			:min="props.min" :max="props.max" :wrap="props.wrap" :icon="props.icon" :selectedColor="props.selectedColor"
			:selectedTextColor="props.selectedTextColor" :emptyText="props.emptyText" :disabled="props.disabled"
			:map="props.map" @change="updateValue" />
	</view>
</template>

<script setup>
	import {
		ref,
		toRefs,
		watch,
		getCurrentInstance,
		inject,
		computed
	} from 'vue';
	import {
		onLoad,
		onUnload,
	} from '@dcloudio/uni-app'

	const {
		proxy
	} = getCurrentInstance()

	/** 接收传入参数 */
	const props = defineProps({
		/** 下拉框数据 */
		data: {
			type: Array,
			default: [],
		},
		/** 选中的值 */
		modelValue: { //
			type: [String, Number, Array],
		},
		/** 选中数据是字符串时的分隔符 */
		separator: {
			type: String,
			default: ",",
		},
		/** 是否开启多选 */
		multiple: {
			type: Boolean,
			default: false,
		},
		mode: {
			type: String,
			default: 'default'
		},
		min: {
			type: [Number, String],
			default: ''
		},
		max: {
			type: [Number, String],
			default: ''
		},
		wrap: {
			type: Boolean,
			default: false
		},
		icon: {
			type: String,
			default: 'left'
		},
		selectedColor: {
			type: String,
			default: ''
		},
		selectedTextColor: {
			type: String,
			default: ''
		},
		emptyText: {
			type: String,
			default: '暂无数据'
		},
		disabled: {
			type: Boolean,
			default: false
		},
		map: {
			type: Object,
			default () {
				return {
					text: 'text',
					value: 'value'
				}
			}
		},
		/** 自定义标签名称（key） */
		labelName: {
			type: String,
			default: "label",
		},
		/** 自定义值名称（value） */
		valueName: {
			type: String,
			default: "value",
		}
	})

	/** 定义父组件能接收事件 */
	const emit = defineEmits(['update:modelValue', 'change'])

	const values = computed(() => {
		if (props.modelValue === null || typeof props.modelValue === 'undefined' || props.modelValue === '')
			return []
		return Array.isArray(props.modelValue) ? props.modelValue.map(item => '' + item) : String(props.modelValue)
			.split(props.separator)
	});

	const data = computed(() => {
		return Array.isArray(props.data) ? props.data.map(item => {
			item['text'] = item[props.labelName]
			item['value'] = item[props.valueName] + ''
			return item
		}) : []
	});

	/** 选中的值 */
	const selectedValue = ref(values.value)
	
	watch(() => props.modelValue, (newVal) => {
		selectedValue.value = values.value
	})

	/** 点击列表中的项时，更新选中值 */
	const updateValue = (obj) => { //方便以后扩展
		if(props.multiple) emit('update:modelValue', selectedValue.value.join(props.separator))
		else emit('update:modelValue', selectedValue.value)
		emit('change', obj)
	}
</script>

<style lang="scss" scoped>
	.container {
		display: inline-block;
	}
</style>