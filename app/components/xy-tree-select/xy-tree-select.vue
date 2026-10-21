<template>
	<view v-for="(item,index) in props.data" :key="index">

		<view :class="updateSelectClass(item)">

			<view @click.stop="updateExpand(item)" class="icon">
				<svg-icon icon="caret-right"
					v-show="Array.isArray(item[props.childrenName]) && item[props.childrenName].length > 0 && !item.expand" />
				<svg-icon icon="caret-bottom"
					v-show="Array.isArray(item[props.childrenName]) && item[props.childrenName].length > 0 && item.expand" />
			</view>

			<view :class="['label', item.disabled ? 'disabled' : '' ]" @click.stop="updateValue(item)">
				{{ item[props.labelName] }}
			</view>

		</view>

		<view v-if="item.expand" class="tree">
			<xy-tree-select v-model="selectedValue" :data="item[props.childrenName]"
				:default-expand-all="props.defaultExpandAll" :multiple="props.multiple" :value-name="props.valueName"
				:label-name="props.labelName" :children-name="props.childrenName" />
		</view>

	</view>
</template>

<script setup>
	/**
	 * 树形选择组件
	 * @description 可以通过点击树节点选中项目，并返回value值
	 * @property {Array} data 格式 tmp = {label:String,value:[String, Number],children:tmp}
	 * @property {[String, Number]} v-model 双向绑定返回值，支持初始化选中项
	 * @property {Boolean} defaultExpandAll 是否全部展开，默认false
	 * @event {[String, Number]} change 点击节点时调用方法，返回节点value值
	 */
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
	})

	/** 向父组件传递信息 */
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
			selectedValue.value = newValue;
		}
	})

	watch(() => props.data, (newValue, oldValue) => { //父组件传入下拉框数据时，向下更新
		newValue.forEach(item => {
			oldValue.forEach(i => {
				if (i[props.valueName] == item[props.valueName]) {
					item['expand'] = i['expand']
				}
			})
			if(typeof item['expand'] != 'boolean') item['expand'] = props.defaultExpandAll
		})
	})

	if (props.defaultExpandAll) {
		props.data.forEach(item => {
			if(typeof item['expand'] != 'boolean') item['expand'] = props.defaultExpandAll
		})
	}

	/** 更新选中值，方便以后扩展 */
	const updateValue = (obj) => {
		//是否被禁用
		if (!obj.disabled) {
			if (props.multiple) { //开启多选时
				//传入的值不正确就初始化
				if (!Array.isArray(selectedValue.value)) selectedValue.value = [];

				var oldLength = selectedValue.value.length;
				selectedValue.value = selectedValue.value.filter(value => value != obj[props.valueName]);

				//长度没有变化，说明不在选中列表里，那就加入选中列表，否则移除
				if (oldLength == selectedValue.value.length) {
					selectedValue.value.push(obj[props.valueName]);
				}
			} else { //关闭多选时
				selectedValue.value = obj[props.valueName];
			}
		}
	}

	const updateExpand = (item) => { //更新节点展开状态
		item['expand'] = item['expand'] ? false : true;
	}

	/** 更新下拉框选中样式 **/
	const updateSelectClass = (item) => {
		// console.log(props.modelValue, item);
		var classStr = "node";
		//是被选中的项（单选，多选两种情况）
		if (item[props.valueName] == props.modelValue ||
			(
				(Array.isArray(props.modelValue) && props.modelValue.filter(val => val == item[props.valueName])
					.length > 0)
			)
		) classStr += ' selected_item';
		//是禁用项
		if (item.disabled) classStr += ' disabled';
		return classStr;
	}
</script>

<style lang="scss" scoped>
	.node {
		display: flex;
		// justify-content: center;
		// align-items: center;
		padding: 3px;
		height: 30px;

		.icon {
			height: 20px;
			color: #00000055;
			display: inline-block;
		}

		.label {
			display: inline-block;
			// float: right;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			// font-size: 24px;
			// width: 100%;
		}

		.disabled {
			//禁用样式
			background-color: #00000055;
		}
	}

	.selected_item {
		//选中样式
		background-color: #ebf5ff;
		// color: #fff;
	}

	.tree {
		margin-left: 10px;
	}
</style>