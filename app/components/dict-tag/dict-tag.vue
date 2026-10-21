<template>
	<view class="bg">
		<view class="list">
			<view class="item" v-for="(item, index) in selecteds" :key="item[props.valueName]">
				<text
					v-if="(item.elTagType == 'default' || item.elTagType == '' || item.elTagType == null) && (item.elTagClass == '' || item.elTagClass == null)"
					:class="item.elTagClass">
					{{ item[props.labelName] + (index === selecteds.length - 1 ? '' : "&nbsp;&nbsp;") }}
				</text>
				<uni-tag v-else :class="item.elTagClass" :type="item.elTagType" :text="item[props.labelName] + ' '" />
			</view>
		</view>
		<view class="list" v-if="unmatch && showValue">
			{{ unmatchArray | handleArray }}
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		// defineProps,
		computed,
		watch
	} from 'vue';
	// 记录未匹配的项
	const unmatchArray = ref([]);

	const props = defineProps({
		/** 需要筛选的数据集合 */
		options: {
			type: Array,
			default: null,
		},
		/** 选中的值 */
		value: [Number, String, Array],
		/** 当未找到匹配的数据时，显示value，默认true */
		showValue: {
			type: Boolean,
			default: true,
		},
		/** 选中数据是字符串时的分隔符 */
		separator: {
			type: String,
			default: ",",
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
	});

	/** 需要筛选的数据 */
	const dataList = ref([]);

	watch(() => props.options, (newVal) => {
		dataList.value = [];
		treeForEach(newVal);
	})

	// console.log("字典组件已被加载！");

	const values = computed(() => {
		if (props.value === null || typeof props.value === 'undefined' || props.value === '') return [];
		return Array.isArray(props.value) ? props.value.map(item => '' + item) : String(props.value).split(props
			.separator);
	});

	const unmatch = computed(() => {
		unmatchArray.value = [];
		// 没有value不显示
		if (props.value === null || typeof props.value === 'undefined' || props.value === '' || props.options
			.length === 0) return false

		// 传入值为数组
		let unmatch = false // 添加一个标志来判断是否有未匹配项
		values.value.forEach(item => {
			if (!dataList.value.some(v => v[props.valueName] === item)) {
				unmatchArray.value.push(item)
				unmatch = true // 如果有未匹配项，将标志设置为true
			}
		})
		return unmatch // 返回标志的值
	});
	
	const selecteds = computed(() => {
		return Array.isArray(dataList.value) ? dataList.value.filter(item => values.value.includes(item[props.valueName])) : [];
	});

	function handleArray(array) {
		if (array.length === 0) return "";
		return array.reduce((pre, cur) => {
			return pre + " " + cur;
		});
	}

	/** props.options是树结构 */
	const treeForEach = (list) => {
		for (let v of list) {
			if (Array.isArray(v[props.childrenName])) {
				treeForEach(v[props.childrenName]);
			}
			//对象[属性名]的形式会修改对象里的属性，需要复制出新的再操作
			let data = {
				...v
			};
			delete data[props.childrenName];
			data[props.valueName] = '' + data[props.valueName];
			dataList.value.push(data);
		}
	}

	treeForEach(props.options);
</script>

<style lang="scss" scoped>
	.bg {
		display: inline-block;

		.list {
			display: inline-block;

			.item {
				display: inline-block;
			}

		}
	}
</style>