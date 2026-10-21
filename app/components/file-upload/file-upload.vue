<template>
	<view class="component-upload-file">
		<!-- #ifdef MP-WEIXIN || H5 -->
		<uni-file-picker ref="file-upload" v-model="fileList" mode="list" file-mediatype="all" :auto-upload="true"
			:limit="limit" :file-extname="fileType" :title="'最多选择' + limit + '个文件'" @select="handleBeforeUpload"
			@success="handleUploadSuccess" @fail="handleUploadError" @delete="handleDelete">
		</uni-file-picker>
		<!-- #endif -->
		<!-- #ifndef MP-WEIXIN || H5 -->
		<uni-file-picker ref="file-upload" v-model="fileList" mode="list" file-mediatype="video" :auto-upload="true"
			:limit="limit" :file-extname="fileType" :title="'最多选择' + limit + '个文件'" @select="handleBeforeUpload"
			@success="handleUploadSuccess" @fail="handleUploadError" @delete="handleDelete">
		</uni-file-picker>
		<!-- #endif -->
		<!-- 上传提示 -->
		<view v-if="showTip">
			请上传
			<text v-if="fileSize">
				大小不超过 <text style="color: #f56c6c">{{ fileSize }}MB</text>
			</text>
			<text v-if="fileType">
				格式为 <text style="color: #f56c6c">{{ fileType.join("/") }}</text>
			</text>
			的文件
		</view>
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
	import config from '@/config'
	import {
		getToken
	} from "@/utils/auth";
	import {
		isExternal
	} from "@/utils/validate";
	import upload from '@/utils/upload'

	/** 接收传入参数 */
	const props = defineProps({
		modelValue: [String, Object, Array],
		// 数量限制
		limit: {
			type: Number,
			default: 5,
		},
		// 大小限制(MB)
		fileSize: {
			type: Number,
			default: 5,
		},
		// 文件类型, 例如["doc", "xls", "ppt", "txt", "pdf"]
		fileType: {
			type: Array,
			default: () => ["doc", "docx", "xls", "xlsx", "ppt", "txt", "pdf"],
		},
		// 是否显示提示
		isShowTip: {
			type: Boolean,
			default: true
		},
	})

	const {
		proxy
	} = getCurrentInstance();
	const emit = defineEmits();
	const number = ref(0);
	const uploadList = ref([]);
	const dialogImageUrl = ref("");
	const dialogVisible = ref(false);
	const baseUrl = config.baseUrl; // 上传的服务器地址
	const headers = ref({
		Authorization: "Bearer " + getToken()
	});
	const fileList = ref([]);
	const showTip = computed(
		() => props.isShowTip && (props.fileType || props.fileSize)
	);

	watch(() => props.modelValue, val => {
		if (val) {
			// 首先将值转为数组
			const list = Array.isArray(val) ? val : props.modelValue.split(",");
			// 然后将数组转为对象数组
			fileList.value = list.map(item => {
				if (typeof item === "string") {
					if (item.indexOf(baseUrl) === -1 && !isExternal(item)) {
						item = {
							name: item.split('/')[item.split('/').length - 1],
							extname: item.split('.')[item.split('.').length - 1],
							url: baseUrl + item
						};
					} else {
						item = {
							name: item.split('/')[item.split('/').length - 1],
							extname: item.split('.')[item.split('.').length - 1],
							url: item
						};
					}
				}
				return item;
			});
		} else {
			fileList.value = [];
			return [];
		}
	}, {
		deep: true,
		immediate: true
	});

	// 上传前loading加载
	function handleBeforeUpload(data) {
		//不符合条件的数量
		let failNumber = 0;
		for (let [index, item] of data.tempFiles.entries()) {
			let file = item.file
			// 校检文件类型
			if (props.fileType.length) {
				const fileName = file.name.split('.');
				const fileExt = fileName[fileName.length - 1];
				const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
				if (!isTypeOk) {
					proxy.$modal.msgError(`文件格式不正确, 请上传${props.fileType.join("/")}格式文件!`);
					proxy.$refs['file-upload'].clearFiles(fileList.value.length + index - failNumber);
					failNumber++
					continue
				}
			}
			// 校检文件大小
			if (props.fileSize) {
				const isLt = file.size / 1024 / 1024 < props.fileSize;
				if (!isLt) {
					proxy.$modal.msgError(`上传文件大小不能超过 ${props.fileSize} MB!`);
					proxy.$refs['file-upload'].clearFiles(fileList.value.length + index - failNumber);
					failNumber++
					continue
				}
			}
			number.value++;
		}
		if (number.value === 0) return false;
		proxy.$modal.loading("正在上传文件，请稍候...");
		proxy.$refs['file-upload'].upload()
		return true;
	}

	// 文件个数超出
	function handleExceed() {
		proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
	}

	// 上传成功回调
	function handleUploadSuccess(data) {
		//上传失败的数量
		let failNumber = 0;
		let notOverNumber = data.tempFiles.length
		for (let [index, item] of data.tempFiles.entries()) {
			upload({
				url: "/common/upload",
				name: 'file',
				file: item,
			}).then(res => {
				if (res.code === 200) {
					if (config.isCloud) {
						uploadList.value.push({
							name: res.data.name,
							extname: res.data.url.split('.')[res.data.url.split('.').length - 1],
							url: res.data.url
						});
					} else {
						uploadList.value.push({
							name: res.fileName.split('/')[res.fileName.split('/').length - 1],
							extname: res.fileName.split('.')[res.fileName.split('.').length - 1],
							url: res.fileName
						});
					}
				} else {
					number.value--;
					proxy.$modal.msgError(res.msg);
					failNumber++;
				}
				notOverNumber--
				if (notOverNumber === 0) {
					fileList.value.splice(fileList.value.length - data.tempFiles.length, data.tempFiles.length);
					uploadedSuccessfully();
				}
			}).catch(res => {
				number.value--;
				notOverNumber--
				failNumber++;
			})
		}
	}

	// 删除
	function handleDelete(data) {
		fileList.value.splice(data.index, 1);
		emit("update:modelValue", listToString(fileList.value));
	}

	// 上传结束处理
	function uploadedSuccessfully() {
		if (number.value > 0 && uploadList.value.length === number.value) {
			fileList.value = fileList.value.filter(f => f.url !== undefined && f.url !== '').concat(uploadList.value);
			uploadList.value = [];
			number.value = 0;
			emit("update:modelValue", listToString(fileList.value));
		}
		proxy.$modal.closeLoading();
	}

	// 上传失败
	function handleUploadError(data) {
		handleDelete(data)
		proxy.$modal.msgError("上传文件失败");
		proxy.$modal.closeLoading();
	}

	// 对象转成指定字符串分隔
	function listToString(list, separator) {
		let strs = "";
		separator = separator || ",";
		for (let i in list) {
			if (undefined !== list[i].url && list[i].url.indexOf("blob:") !== 0) {
				strs += list[i].url.replace(baseUrl, "") + separator;
			}
		}
		return strs != "" ? strs.substr(0, strs.length - 1) : "";
	}
</script>