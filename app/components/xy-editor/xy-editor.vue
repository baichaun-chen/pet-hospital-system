<template>
	<view class="editor">
		<view class='wrapper'>
			<view class='toolbar' @tap="format" style="height: auto;overflow-y: auto;">
				<view :class="formats.bold ? 'ql-active' : ''" class="iconfont icon-zitijiacu" data-name="bold">
				</view>
				<view :class="formats.italic ? 'ql-active' : ''" class="iconfont icon-zitixieti" data-name="italic">
				</view>
				<view :class="formats.underline ? 'ql-active' : ''" class="iconfont icon-zitixiahuaxian"
					data-name="underline"></view>
				<view :class="formats.strike ? 'ql-active' : ''" class="iconfont icon-zitishanchuxian"
					data-name="strike"></view>
				<!-- #ifndef MP-BAIDU -->
				<view :class="formats.align === 'left' ? 'ql-active' : ''" class="iconfont icon-zuoduiqi"
					data-name="align" data-value="left"></view>
				<!-- #endif -->
				<view :class="formats.align === 'center' ? 'ql-active' : ''" class="iconfont icon-juzhongduiqi"
					data-name="align" data-value="center"></view>
				<view :class="formats.align === 'right' ? 'ql-active' : ''" class="iconfont icon-youduiqi"
					data-name="align" data-value="right"></view>
				<view :class="formats.align === 'justify' ? 'ql-active' : ''" class="iconfont icon-zuoyouduiqi"
					data-name="align" data-value="justify"></view>
				<!-- #ifndef MP-BAIDU -->
				<view :class="formats.lineHeight ? 'ql-active' : ''" class="iconfont icon-line-height"
					data-name="lineHeight" data-value="2"></view>
				<view :class="formats.letterSpacing ? 'ql-active' : ''" class="iconfont icon-Character-Spacing"
					data-name="letterSpacing" data-value="2em"></view>
				<view :class="formats.marginTop ? 'ql-active' : ''" class="iconfont icon-722bianjiqi_duanqianju"
					data-name="marginTop" data-value="20px"></view>
				<view :class="formats.marginBottom ? 'ql-active' : ''" class="iconfont icon-723bianjiqi_duanhouju"
					data-name="marginBottom" data-value="20px">
				</view>
				<!-- #endif -->

				<view class="iconfont icon-clearedformat" @tap="removeFormat"></view>

				<!-- #ifndef MP-BAIDU -->
				<view :class="formats.fontFamily ? 'ql-active' : ''" class="iconfont icon-font" data-name="fontFamily"
					data-value="Pacifico"></view>
				<view :class="formats.fontSize === '24px' ? 'ql-active' : ''" class="iconfont icon-fontsize"
					data-name="fontSize" data-value="24px"></view>
				<!-- #endif -->
				<view :class="formats.color === '#0000ff' ? 'ql-active' : ''" class="iconfont icon-text_color"
					data-name="color" data-value="#0000ff"></view>
				<view :class="formats.backgroundColor === '#00ff00' ? 'ql-active' : ''"
					class="iconfont icon-fontbgcolor" data-name="backgroundColor" data-value="#00ff00"></view>
				<view class="iconfont icon-date" @tap="insertDate"></view>
				<view class="iconfont icon--checklist" data-name="list" data-value="check"></view>
				<view :class="formats.list === 'ordered' ? 'ql-active' : ''" class="iconfont icon-youxupailie"
					data-name="list" data-value="ordered"></view>
				<view :class="formats.list === 'bullet' ? 'ql-active' : ''" class="iconfont icon-wuxupailie"
					data-name="list" data-value="bullet"></view>

				<view class="iconfont icon-undo" @tap="undo"></view>
				<view class="iconfont icon-redo" @tap="redo"></view>

				<view class="iconfont icon-outdent" data-name="indent" data-value="-1"></view>
				<view class="iconfont icon-indent" data-name="indent" data-value="+1"></view>
				<view class="iconfont icon-fengexian" @tap="insertDivider"></view>
				<view class="iconfont icon-charutupian" @tap="insertImage"></view>
				<view :class="formats.header === 1 ? 'ql-active' : ''" class="iconfont icon-format-header-1"
					data-name="header" :data-value="1"></view>
				<view :class="formats.script === 'sub' ? 'ql-active' : ''" class="iconfont icon-zitixiabiao"
					data-name="script" data-value="sub"></view>
				<view :class="formats.script === 'super' ? 'ql-active' : ''" class="iconfont icon-zitishangbiao"
					data-name="script" data-value="super"></view>

				<view class="iconfont icon-shanchu" @tap="clear"></view>

				<view :class="formats.direction === 'rtl' ? 'ql-active' : ''" class="iconfont icon-direction-rtl"
					data-name="direction" data-value="rtl"></view>
			</view>

			<view class="editor-wrapper">
				<scroll-view style="height: 100%;" scroll-y="true">
					<editor id="editor" class="ql-container" placeholder="开始输入..." show-img-size show-img-toolbar
						show-img-resize @statuschange="onStatusChange" :read-only="props.readOnly"
						@ready="onEditorReady" @input="change">
					</editor>
				</scroll-view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		watch,
		getCurrentInstance
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import upload from '@/utils/upload'
	import config from '@/config'
	const {
		proxy
	} = getCurrentInstance();

	/** 接收传入参数 */
	const props = defineProps({
		/** 选中的值 */
		modelValue: { //
			type: [String],
		},
		/** 选中的值 */
		readOnly: { //
			type: [Boolean],
			default: false
		},
	})
	/** 定义父组件能接收事件 */
	const emit = defineEmits(['update:modelValue', 'change']);
	const formats = ref({})
	const editorCtx = ref(null);
	onLoad(() => {
		// #ifndef MP-BAIDU
		// uni.loadFontFace({
		// 	family: 'Pacifico',
		// 	source: 'url("https://sungd.github.io/Pacifico.ttf")'
		// })
		// #endif
	})
	watch(() => props.modelValue, (newVal, oldVal) => {
		if (editorCtx.value != null) {
			editorCtx.value.getContents().then(res => {
				if (newVal !== res.html) {
					editorCtx.value.setContents({
						html: newVal
					});
				}
			})
		}
	})

	const onEditorReady = () => {
		// #ifdef MP-BAIDU
		editorCtx.value = requireDynamicLib('editorLib').createEditorContext('editor');
		editorCtx.value.setContents({
			html: props.modelValue
		})
		// #endif

		// #ifdef APP-PLUS || MP-WEIXIN || H5
		uni.createSelectorQuery().in(proxy).select('#editor').context((res) => {
			// console.log(res);
			editorCtx.value = res.context
			// console.log(props.modelValue);
			editorCtx.value.setContents({
				html: props.modelValue
			})
		}).exec()
		// #endif
	}
	const undo = () => {
		editorCtx.value.undo()
	}
	const redo = () => {
		editorCtx.value.redo()
	}
	const format = (e) => {
		let {
			name,
			value
		} = e.target.dataset
		if (!name) return
		// console.log('format', name, value)
		editorCtx.value.format(name, value)
	}
	const onStatusChange = (e) => {
		const _formats = e.detail
		formats.value = _formats
	}
	const insertDivider = () => {
		editorCtx.value.insertDivider({
			success: function() {
				// console.log('insert divider success')
			}
		})
	}
	const clear = () => {
		proxy.$modal.confirm('确定清空编辑器全部内容？').then(res => {
			if (res.confirm) {
				editorCtx.value.clear({
					success: function(res) {
						// console.log("clear success")
					}
				})
			}
		})
	}
	const removeFormat = () => {
		editorCtx.value.removeFormat()
	}
	const insertDate = () => {
		const date = new Date()
		const formatDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
		editorCtx.value.insertText({
			text: formatDate
		})
	}
	const insertImage = () => {
		uni.chooseImage({
			count: 1,
			success: (res) => {
				upload({
					url: '/common/upload',
					name: 'file',
					file: res.tempFiles[0]
				}).then(res => {
					if (config.isCloud) {
						editorCtx.value.insertImage({
							src: res.data.url,
							alt: res.data.name,
							success: function() {}
						})
					} else {
						editorCtx.value.insertImage({
							src: config.baseUrl + res.fileName,
							alt: res.newFileName,
							success: function() {}
						})
					}
				})
			}
		})
	}

	const change = (e) => {
		const data = e.detail.html;
		emit('update:modelValue', data);
	}
</script>

<style>
	@import "./iconfont.css";

	.editor {
		max-height: 44vh;
		border: 1px solid #dcdfe6;
	}

	.page-body {
		/* height: calc(100vh - var(--window-top) - var(--status-bar-height)); */
	}

	.wrapper {
		/* height: 100%; */
	}

	.editor-wrapper {
		/* height: calc(100vh - var(--window-top) - var(--status-bar-height) - 140px); */
		background: #fff;
	}

	.iconfont {
		display: inline-block;
		padding: 8px 8px;
		width: 24px;
		height: 24px;
		cursor: pointer;
		font-size: 20px;
	}

	.toolbar {
		box-sizing: border-box;
		border-bottom: 1px;
		font-family: 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
		height: 10vh;
		overflow-y: auto;
	}

	.ql-container {
		box-sizing: border-box;
		padding: 0px 12px;
		width: 100%;
		height: 30vh;
		/* height: 100%; */
		/* margin-top: 20px; */
		/* font-size: 16px; */
		/* line-height: 1.5; */
		overflow-y: auto;
	}

	.ql-active {
		color: #06c;
	}
</style>