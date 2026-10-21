<template>
	<view class="container" ref="containerRef">
		<!-- 显示在父组件的框 -->
		<view class="search">
			<view class="show_name">
				<view class="item" v-if="showName.length > 0">
					<view style="">{{ showName[0][props.labelName] }}</view>
					<view class="clearable_icon" @click="handleClearable(showName[0])" v-if="props.clearable">
						<svg-icon class="clearable" icon="close"></svg-icon>
					</view>
				</view>
				<view class="number" v-if="showName.length > 1">+{{ showName.length }}</view>
			</view>
			<uni-easyinput ref="inputRef" class="uni-easyinput" @focus="listShow"
				@input="handleFilter" :placeholder="props.placeholder" />
		</view>
		<!-- 防止悬浮脱离文档流时，发生错位 -->
		<view>
			<view class="th" @tap="listHiden" v-if="showList"></view>
			<!-- v-if每次打开重新渲染 -->
			<scroll-view class="list" :scroll-y="true" :style="{top: props.placement === 'top' ? listHeight + 'px' : 'auto'}" v-if="showList" @tap.stop="listHiden">
				<!-- 悬浮显示的点击选项 -->
				<view :class="updateSelectClass(item)" v-for="(item,index) in selectData" :key="index"
					@click="updateValue(item)">
					{{ item[props.labelName] }}
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script setup>
	/**
	 * 下拉选择组件
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
		getCurrentInstance,
		inject
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
		/** 是否开启多选 */
		multiple: {
			type: Boolean,
			default: false,
		},
		/** 最多能选多少 */
		multipleLimit: {
			type: Number,
			default: 0,
		},
		/** 最少选多少 */
		minLimit: {
			type: Number,
			default: 0,
		},
		/** 是否能清除 */
		clearable: {
			type: Boolean,
			default: false,
		},
		/** 下拉框处出现位置 */
		placement: {
			type: String,
			default: "bottom",
		},
		/** 筛选框提示信息 */
		placeholder: {
			type: String,
			default: "关键字筛选",
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

	// console.log("下拉框组件已被加载！");

	/** 键盘高度 */
	// const keyboardHeight = ref(0)

	/** 下拉框高度 */
	const listHeight = ref(0)

	/** 重新获取下拉框高度重试次数 */
	const listHeightRetry = ref(100)

	/** 该组件内部下拉框数据 */
	const selectData = ref(props.data);

	/** 判断父组件有没有给该组件加上multiple属性，即判断是否开启了多选，（不可修改） */
	const multiple = props.multiple;

	/** 判断父组件有没有给该组件加上clearable属性，即判断是否开启了清除功能，（不可修改） */
	const clearable = props.clearable;

	/** 是否显示点击列表 */
	const showList = ref(false);

	/** 筛选框中显示的文本对象 */
	const showName = ref([]);

	/** 定义父组件能接收事件 */
	const emit = defineEmits(['update:modelValue', 'change']);

	/** 选中的值 */
	const selectedValue = ref(props.modelValue);
	// console.log(selectedValue.value);

	watch(() => props.data, (newValue, oldValue) => { //传入下拉框数据时，初始化下拉框数据
		if (newValue != selectData.value) {
			selectData.value = newValue;
			//修改输入框内容
			updateInput();
		}
	})
	watch(() => selectedValue.value, (newValue, oldValue) => { //选中值更新时，向上更新
		if (newValue != props.modelValue) {
			emit('update:modelValue', newValue);
			emit('change', newValue);
		}
	})
	watch(() => props.modelValue, (newValue, oldValue) => { //父组件传入选中值时，向下更新
		if (newValue != selectedValue.value) {
			//修改选中值
			selectedValue.value = newValue;
			//修改输入框内容
			updateInput();
		}
	})

	/** 点击列表中的项时，更新选中值 */
	const updateValue = (obj) => { //方便以后扩展
		if (!obj.disabled) { //判断该项是否被禁用
			if (multiple) { //开启多选时

				//传入的值不正确就初始化
				if (!Array.isArray(selectedValue.value)) selectedValue.value = [];

				//达到上限禁止再添加
				if (props.multipleLimit != 0 && selectedValue.value.length >= props.multipleLimit) return;

				//达到最小值禁止再减少
				if (props.minLimit != 0 && props.minLimit >= selectedValue.value.length) return;

				var oldLength = selectedValue.value.length;
				selectedValue.value = selectedValue.value.filter(value => value != obj[props.valueName]);

				//长度没有变化，说明不在选中列表里，那就加入选中列表，否则移除
				if (oldLength == selectedValue.value.length) {
					selectedValue.value.push(obj[props.valueName]);
					showName.value.push(obj);
				} else {
					// console.log(multiple, showName.value);
					showName.value = showName.value.filter(o => o[props.valueName] != obj[props.valueName]);
				}
			} else { //没有开启多选时
				showName.value = [obj];
				selectedValue.value = obj[props.valueName];
			}
		}
	}

	/** 获得焦点时，显示点击列表 */
	const listShow = (e) => {
		// if (e.detail.height) {
		// 	keyboardHeight.value = e.detail.height
		// }
		showList.value = true
		isTop()
	}
	/** 失去焦点时，隐藏点击列表 */
	const listHiden = () => {
		setTimeout(function() {
			showList.value = false
		}, 100)
	}

	/** 更新输入框文字 **/
	const updateInput = () => {
		if (selectData.value == undefined || selectData.value == null || selectedValue.value == null || selectedValue
			.value == undefined) {
			showName.value = [];
			return
		}
		//是单选
		if (!multiple) showName.value = selectData.value.filter(d => d[props.valueName] == selectedValue.value);
		else {
			//是多选
			showName.value = [];
			for (let data of selectData.value) {
				for (let val of selectedValue.value) {
					if (data[props.valueName] == val) {
						showName.value.push(data);
						break;
					}
				}
			}
		}
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

	/** 过滤label */
	const handleFilter = (e) => {
		if (typeof e != 'string' || e == '') {
			selectData.value = props.data
			return
		}
		selectData.value = props.data.filter(i => i[props.labelName].includes(e));
	}

	/** 清除对象 */
	const handleClearable = (obj) => {
		if (!obj.disabled) { //判断该项是否被禁用
			if (multiple) { //开启多选时

				//传入的值不正确就初始化
				if (!Array.isArray(selectedValue.value)) selectedValue.value = [];

				//达到最小值禁止再减少
				if (props.minLimit != 0 && props.minLimit >= selectedValue.value.length) return;

				selectedValue.value = selectedValue.value.filter(value => value != obj[props.valueName]);
				showName.value = showName.value.filter(o => o[props.valueName] != obj[props.valueName]);
			} else { //没有开启多选时
				showName.value = [];
				selectedValue.value = null;
			}
		}
		selectedValue.value
	}

	/** 判断是不是在顶部显示 */
	const isTop = () => {
		if (props.placement === 'top') {
			const query = uni.createSelectorQuery().in(proxy)
			const elem = query.select(".list")
			elem.boundingClientRect(data => {
				if (data == null && listHeightRetry.value > 0) { //数据为空且还有重置次数
					listHeightRetry.value--
					setTimeout(isTop, 100)
					return
				} else if (data != null) { //数据不为空
					listHeightRetry.value = 100 //重置重试次数
					const height = data.height;
					listHeight.value = -1 * (height > 200 ? 200 : height);
					// console.log(props.placement === 'top', listHeight.value);
				}
			}).exec();
		}
	}
	
</script>

<style lang="scss" scoped>
	.list {
		position: absolute;
		border: 1px solid #00000022;
		border-radius: 5px;
		background-color: white;
		padding: 5px;
		width: 100%;
		max-height: 200px;
		z-index: 99;
		overflow-y: auto;

		.node {
			// display: flex;
			// justify-content: center;
			// align-items: center;
			padding: 3px;
			height: 30px;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			width: 100%;
		}

		.node:focus {
			background-color: #00000022;
		}

		.disabled {
			//禁用样式
			background-color: #00000055;
		}

		.selected_item {
			//选中样式
			background-color: #ebf5ff;
			color: #000;
		}
	}

	.th{
		width: 100%;
		height: 100vh;
		background-color: #00000001;
		position: fixed;
		top: 0px;
		left: 0px;
		z-index: 98;
	}

	.search {
		display: flex;
		justify-content: center;
		align-items: center;
		border: 1px solid #dcdfe6;
		border-radius: 4px;
		z-index: -1;

		.show_name {
			display: flex;
			max-width: 70%;

			.item {
				// margin-left: 5px;
				padding: 8px;
				// border-radius: 4px;
				// display: inline-block;
				background-color: rgb(243.9, 244.2, 244.8);
				// max-width: 100%;
				overflow: hidden;
				float: left;
				text-overflow: ellipsis;
				white-space: nowrap;
				display: flex;
				justify-content: center;
				align-items: center;

				.clearable_icon {
					margin-left: 5px;
					width: 19px;
					height: 19px;
				}
			}

			.number {
				// margin-left: 5px;
				padding: 8px;
				// border-radius: 4px;
				// display: inline-block;
				background-color: #efefef;
				min-width: 20%;
				overflow: hidden;
				float: left;
				text-overflow: ellipsis;
				white-space: nowrap;
				z-index: 0;
			}
		}

		:deep(.uni-easyinput .uni-easyinput__content) {
			border: 0px;
		}
	}
</style>