<template>
	<view class="container">
		<button class="mini-btn xy-btn" type="primary" size="mini" @click="showDrawer('showLeft')">
			{{ message.label }}
			{{ props.multiple && message.number != 0 ? '+ ' + message.number : '' }}
		</button>
		<uni-drawer ref="showLeft" mode="left" :width="props.width" @change="change($event,'showLeft')">
			<scroll-view style="height: 100%;padding-left: 5px;" scroll-y="true">
				<xy-tree-select v-model="selectedValue" :data="props.data" :default-expand-all="props.defaultExpandAll"
					:value-name="props.valueName" :label-name="props.labelName" :children-name="props.childrenName"
					:multiple="props.multiple" @change="updateValue" />
			</scroll-view>
		</uni-drawer>
	</view>
</template>

<script setup>
	/**
	 * 抽屉树形选择组件
	 * @description 可以通过点击树节点选中项目，并返回value值
	 * @property {Array} data 格式 tmp = {label:String,value:[String, Number],children:tmp}
	 * @property {[String, Number]} v-model 双向绑定返回值，支持初始化选中项
	 * @property {Boolean} defaultExpandAll 是否全部展开，默认false
	 * @event {[String, Number]} change 点击节点时调用方法，返回节点value值
	 */
	import {
		ref,
		getCurrentInstance,
		// defineProps,
		// defineEmits,
		watch,
	} from 'vue'
	const {
		proxy
	} = getCurrentInstance();
	const props = defineProps({
		/** 树形下拉框数据 */
		data: {
			type: Array,
			default: [],
		},
		/** 选中的值 */
		modelValue: {
			type: [String, Number, Array]
		},
		/** 是否展开 */
		defaultExpandAll: {
			type: Boolean,
			default: false,
		},
		/** 是否开启多选 */
		multiple: {
			type: Boolean,
			default: false,
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
		},
		/** 自定义子节点的名称（children） */
		childrenName: {
			type: String,
			default: "children",
		},
		/** 设置宽度 */
		width: {
			type: Number,
			default: 130
		}
	})

	// console.log(props.valueName , props.labelName, props.childrenName);

	/** 选中信息 */
	const message = ref({
		number: 0,
		value: '',
		label: '请选择'
	});
	/** 左侧抽屉开关 */
	const showLeft = ref(false);

	/** 向父组件传递的事件 */
	const emit = defineEmits(['update:modelValue', 'change']);

	/** 当前选中值 */
	const selectedValue = ref(props.modelValue);

	watch(() => selectedValue.value, (newValue, oldValue) => { //选中值更新时，向上更新
		if (newValue != props.modelValue) {
			emit('update:modelValue', newValue);
			emit('change', newValue);
		}
	})
	watch(() => props.modelValue, (newValue, oldValue) => { //父组件传入选中值时，向下更新
		if (newValue != selectedValue.value) {
			if (props.multiple && newValue == null) newValue = [];
			selectedValue.value = newValue;
			updateValue();
			if (props.modelValue === undefined || props.modelValue === null || props.modelValue === 0) {
				message.value = {
					number: 0,
					value: '',
					label: '请选择'
				}
			}
		}
	})

	/** 更新选中按钮 */
	const updateValue = (e) => {
		if (props.multiple) { //开启多选时
			if (selectedValue.value == null || selectedValue.value == undefined) selectedValue.value = [];
			for (let selected of selectedValue.value) {
				props.data.forEach(i => {
					querySelect(i, selected);
				})
			}
		} else { //关闭多选时
			props.data.forEach(i => {
				querySelect(i, selectedValue.value);
			})
		}
	}

	/** 更新选中时更新选中信息 */
	const querySelect = (node, selected) => {
		if (selected == node[props.valueName]) { //找到后停止向下迭代
			node.value = node[props.valueName];
			node.label = node[props.labelName];
			if (props.multiple) {
				node.number = selectedValue.value.length - 1
			}
			message.value = node;
			return
		} else if (selected == message.value.value) { //已经找到就没必要再迭代了
			return
		} else if (!Array.isArray(node[props.childrenName])) { //已经没有儿子了结束
			return
		} else { //没有匹配到结果继续
			node[props.childrenName].forEach(i => {
				querySelect(i, selected);
			})
		}
	}

	// 打开窗口
	const showDrawer = (e) => {
		proxy.$refs[e].open()
	}
	// 关闭窗口
	const closeDrawer = (e) => {
		proxy.$refs[e].close()
	}
	// 抽屉状态发生变化触发
	const change = (e, type) => {
		// console.log((type === 'showLeft' ? '左窗口' : '右窗口') + (e ? '打开' : '关闭'));
		// console.log(queryParams.value);
		proxy[type] = e
	}

	updateValue();
</script>

<style lang="scss" scoped>
	.container {
		overflow-y: auto;
		overflow-x: hidden;
		height: 100%;
	}
</style>